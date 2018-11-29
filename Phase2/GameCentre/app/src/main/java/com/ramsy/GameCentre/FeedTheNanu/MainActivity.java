package com.ramsy.GameCentre.FeedTheNanu;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ramsy.GameCentre.R;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, NanuDelegate, PauseButtonDelegate {

    // TODO:
    // - Lock to portrait orientation only (app wide?)
    // - Disable status bar

    /*
    Note: Java interfaces evidently cannot contain attributes, so either make an abstract subclass of ImageView
    that provides the new attribute. Or have interface getAnimator and setAnimator methods,
    which will involve the conforming class needing to store the attribute anyway. So go with an abstract class design instead.
    Maybe we can make the abstract class subclass ImageView, declare the animator property, and also conform to
    Pausable, with pause and resume implementations that directly manage that animator object.
    Instead of the subclasses all needing to provide the same implementations.
    However, if it's an abstract class, we cant subclass AND call super.
    So just don't make it abstract then.
     */


    Nanu nanu;
    RelativeLayout container;
    ItemGenerator itemGenerator;
    final int padding = 100;

    HealthBar healthBar;

    int score;
    TextView scoreLabel;

    boolean isPaused = false;


    Handler updateHandler = new Handler();
    Handler itemDropHandler = new Handler();


    Runnable itemDrop = new Runnable() {
        @Override
        public void run() {

            /*
             The food items need to have a property that stores their animator object.
             That's why we made them all subclass DropItem, which provides that item, and implements Pausable.
             And the method that creates them and their animation (here), needs to be working with a type such that
             that property is visible to the compiler (case them as a DropItem).
             */

            // Vend an item
            DropItem newItem = (DropItem)itemGenerator.getItem();
            newItem.setId(10);

            int w = 125;
            int h = 125;
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, h);
            newItem.setLayoutParams(p);

            // Set X position so the left edges and right edges of the items can only get so close to the screen edges.
            Random random = new Random();
            float x = random.nextInt(screenWidth() - padding * 2 - w) + padding;
            newItem.setX(x);
            newItem.setY(-(h + 25)); // an extra 25 pixels just for good measure.

            container.addView(newItem);


            // Create a new animation
            ObjectAnimator anim = ObjectAnimator.ofFloat(newItem, "y", screenHeight());
            anim.setDuration(5000);
            anim.setInterpolator(null);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    container.removeView(newItem);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            newItem.animator = anim;
            anim.start();

            itemDropHandler.postDelayed(this, 1200);
        }
    };

    Runnable update = new Runnable() {
        @Override
        public void run() {

            boolean foodIsNearby = false;
            Edible foodItemForEating = null;

            int childCount = container.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View v = container.getChildAt(i);
                if (v.getId() != 10) {
                    continue;
                }

                Rect r1 = nanu.getProximityZone();
                Rect r2 = nanu.getMouthZone();
                Rect r3 = RectUtility.boundingBox(v);


                // Detect if an item is in the mouth zone
                // First get midpoint of the item
                if (r2.contains(r3.centerX(), r3.centerY())) {
                    foodItemForEating = (Edible) v;
                    break;
                }


                // Detect if an item is in the trigger zone
                if (r1.intersects(r3.left, r3.top, r3.right, r3.bottom)) {
                    foodIsNearby = true;
                    break;
                }
            }

            nanu.foodItemForEating = foodItemForEating;
            nanu.foodIsNearby = foodIsNearby;
            nanu.timeDidElapse();

            healthBar.setHealthTo(nanu.getCurrentLifePercent());

            updateHandler.postDelayed(this, gameLoopInterval);
        }
    };


    /**
     * Period in milliseconds of the game loop
     */

    long gameLoopInterval = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the Item Generator
        this.itemGenerator = new ItemGenerator(this);

        // Create a View Group
        final RelativeLayout container = new RelativeLayout(this);
        this.container = container;

        // Set the Activity's window to the View Group
        setContentView(container);

        this.score = 0;

        setupBackground();
        setupScoreLabel();
        setupPauseButton();
        setupHealthBar();
        setupNanu();

        nanu.setTimeSliceInterval(gameLoopInterval);
        nanu.resume();

        // Start running tasks
        updateHandler.post(update);
        itemDropHandler.post(itemDrop);

    }

    private void setupBackground() {
        ImageView v = new ImageView(this);
        Bitmap im = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
        v.setImageBitmap(im);
        v.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(v);
        v.setOnTouchListener(this);
    }

    private void setupScoreLabel() {
        // Setup Score label
        TextView label = new TextView(this);
        label.setText(String.valueOf(this.score));
        label.setTextColor(Color.BLACK);
        label.setTextSize(30);
        label.setBackgroundColor(Color.WHITE);
        label.setGravity(Gravity.CENTER);
        label.setAlpha(0.5f);
        this.container.addView(label);
        this.scoreLabel = label;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,200);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.setMargins(0, 140, 0, 0);
        label.setLayoutParams(params);
    }

    private void setupPauseButton() {
        PauseButton p = new PauseButton(this);
        p.delegate = this;

        RelativeLayout.LayoutParams pauseButtonParams = new RelativeLayout.LayoutParams(175, 175);
        pauseButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        pauseButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pauseButtonParams.setMargins(0, 10, 10, 0);
        p.setLayoutParams(pauseButtonParams);
        container.addView(p);
    }

    private void setupHealthBar() {
        int h = 120;
        int w = 500;
        HealthBar hb = new HealthBar(this);
        RelativeLayout.LayoutParams healthBarParams = new RelativeLayout.
                LayoutParams(w, h);
        healthBarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        healthBarParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        healthBarParams.setMargins(10, 10, 0, 0);
        hb.setLayoutParams(healthBarParams);
        container.addView(hb);
        this.healthBar = hb;
    }

    private void setupNanu() {
        Nanu n = new Nanu(this);
        n.delegate = this;
        this.nanu = n;

        RelativeLayout.LayoutParams nanuParams = new RelativeLayout.LayoutParams(250, 250);
        nanuParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        nanuParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        nanuParams.setMargins(0, 0, 0, 50);
        n.setLayoutParams(nanuParams);

        container.addView(n);
    }


    @Override
    public void lifeReachedZero() {
        // TODO:
        // game over functionality
    }

    @Override
    public void scoreShouldChangeBy(int amount) {
        score += amount;
        this.scoreLabel.setText(String.valueOf(score));
    }

    @Override
    public void aboutToEat(Edible item) {
        View v = (View) item;
        container.removeView(v);
    }

    float oldX;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // Prevent the Nanu from being moved if isPaused is true.
        if (isPaused) { return false; }

        // Map the Nanu's center to the touch location
//        nanu.setX(event.getX() - (nanu.getWidth() / 2));

        // Alternatively, track delta changes
        float newX = event.getX();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                oldX = newX;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = newX - oldX;
                nanu.setX(nanu.getX() + deltaX);
                oldX = event.getX();

                // Adjust position to keep it from being moved out of the screen.
                if (nanu.getX() < 0) {
                    nanu.setX(0);
                } else if (nanu.getX() + nanu.getWidth() > screenWidth()) {
                    nanu.setX(screenWidth() - nanu.getWidth());
                }
                break;

            default:
                // Called on new finger touch downs, and all finger releases.
                break;
        }




        return true;
    }


    private int screenWidth() {
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        return display.widthPixels;
    }

    private int screenHeight() {
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        return display.heightPixels;
    }

    @Override
    public void pauseButtonWasTapped(boolean paused) {

        this.isPaused = paused;

        // Pause/Resume non view stuff (like stop the handlers that generate new food, prevent Nanu from being moved)
        if (paused) {
            itemDropHandler.removeCallbacks(itemDrop); // stop new items from falling
            updateHandler.removeCallbacks(update); // pause the game engine loop, for efficiency
        } else {
            itemDropHandler.postDelayed(itemDrop, 1000); // we use post delayed here, to prevent the situation where spamming the pause button
            // caused an item to drop on each resume. But then it's possible to spam the pause button and progress time
            // without any food falling at all. For now, this is enough.
            updateHandler.post(update);
        }

        // Pause/Resume child views
        int childCount = container.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View someChildView = container.getChildAt(i);
            if (someChildView instanceof Pausable) {
                Pausable p = (Pausable) someChildView;
                if (paused) {
                    p.pause();
                } else {
                    p.resume();
                }
            }
        }
    }
}
