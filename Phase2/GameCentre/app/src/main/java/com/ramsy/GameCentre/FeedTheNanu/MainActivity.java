package com.ramsy.GameCentre.FeedTheNanu;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramsy.GameCentre.GameCentreCommon.ChooseGame;
import com.ramsy.GameCentre.GameCentreCommon.FinishedGameActivity;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.SaveState;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
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
    PauseButton pauseButton;
    HealthBar healthBar;
    ImageView backButton;
    int score;
    TextView scoreLabel;

    boolean isPaused = false;

    /**
     * The time interval in milliseconds between food item drops.
     */

    long itemDropInterval = 1200;


    /**
     * The time it takes in milliseconds for a falling food item to reach the bottom.
     */

    long itemDropDuration = 5000;


    Handler updateHandler = new Handler();
    Handler itemDropHandler = new Handler();
    Handler saveHandler = new Handler();

    Runnable autoSave = new Runnable() {
        @Override
        public void run() {
            save();
            saveHandler.postDelayed(this, saveInterval);
        }
    };


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
            anim.setDuration(itemDropDuration);
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

            itemDropHandler.postDelayed(this, itemDropInterval);
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


            /*
            This next line can eventually call a delegate method,
            which we had set to call the pauseButtonWasTapped(true) method,
            But it wasn't actually stopping this handler,
            Because the call to stop it is made within the scope of the handler itself.
            The pause button taps worked because that code wasn't running within the context of the handler.
            What's more is, only the itemDrop handler was stopped, which makes sense because
            we weren't trying to stop it from within its own context.
            So, keep the delegate method as a chance to call normal pausing functionality,
            But then have a method that returns true if the Nanu is still alive,
            And only schedule the next run if the Nanu is still alive.
             */

            nanu.timeDidElapse();


            healthBar.setHealthTo(nanu.getCurrentLifePercent());

            if (!nanu.currentLifeIsZero()) {
                updateHandler.postDelayed(this, gameLoopInterval);
            }
        }
    };


    /**
     * Period in milliseconds of the game loop
     */

    long gameLoopInterval = 20;

    /**
     * Period in milliseconds to save the game automatically
     */

    long saveInterval = 5000;


    /**
     * The slot number to save in.
     * Retrieved from the Intent.
     */

    int slot;


    /**
     * Saves the game
     */

    private void save() {
        // Save logic here
        SaveState s = new SaveState(score, nanu.currentLife);
        // Throw this to the backend function
        meUser.saveGame("FeedTheNanu", s, this.slot);
    }

    /**
     * The user that is currently logged in.
     */

    private User meUser;

    /**
     * The name of the game.
     */

    String gameName = "FeedTheNanu";


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
        setupBackground();
        setupScoreLabel();
        setupPauseButton();
        setupBackButton();
        setBackButtonListener();
        setupHealthBar();
        setupNanu();

        // Grab the user
        this.meUser = FirebaseFuncts.getUser();

        // Retrieve Slot Number from Intent
        Bundle b = getIntent().getExtras();
        this.slot = b.getInt("slot");

        System.out.println("XXX this is the slot to save into: " + slot);

        SaveState save = meUser.getGame(gameName, slot);

        if (save != null) {
            this.score = save.score;
            this.nanu.currentLife = save.currentLife;
            this.healthBar.setHealthTo(nanu.currentLife);
            this.scoreLabel.setText(String.valueOf(score));
        } else {
            this.score = 0;
        }

        this.save();

        nanu.setTimeSliceInterval(gameLoopInterval);
        nanu.resume();

        // Start running tasks
        updateHandler.post(update);
        itemDropHandler.post(itemDrop);
        saveHandler.postDelayed(autoSave, saveInterval);

    }

    /**
     * set the game background
     */
    private void setupBackground() {
        ImageView v = new ImageView(this);
        Bitmap im = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
        v.setImageBitmap(im);
        v.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(v);
        v.setOnTouchListener(this);
    }

    /**
     * set up the score label on the top of the game screen
     */
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

    /**
     * set up the BackButton on the screen, set it size smaller than pauseButton for aesthetic and
     * place it in the right position
     */
    public void setupBackButton(){
        // set up a new back button
        ImageView backIcon = new ImageView(this);
        Bitmap im = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        backIcon.setImageBitmap(im);
        RelativeLayout.LayoutParams backParams = new RelativeLayout.LayoutParams(130, 130);
        backParams.addRule(RelativeLayout.LEFT_OF, this.pauseButton.getId());
//        backParams.addRule(RelativeLayout., );
        backParams.setMargins(0, 20, 0, 0);
        backIcon.setLayoutParams(backParams);
//        backIcon.setX(screenWidth()/2);
//        backIcon.setY(10);
        this.backButton = backIcon;
        container.addView(backButton);
    }

    /**
     * Set up backButtonListener so that once the backbutton is clicked, it takes the user back to the
     * chooseGame page.
     */
    public void setBackButtonListener() {
        this.backButton.setOnClickListener((v)->{
            pauseButtonWasTapped(true);
            Intent tmp = new Intent(this, ChooseGame.class);
            startActivity(tmp);
        });
    }


    /**
     * set up pause button on the top right corner of the game screen
     */
    private void setupPauseButton() {
        PauseButton p = new PauseButton(this);
        p.delegate = this;

        RelativeLayout.LayoutParams pauseButtonParams = new RelativeLayout.LayoutParams(175, 175);
        pauseButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        pauseButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pauseButtonParams.setMargins(0, 10, 10, 0);
        p.setLayoutParams(pauseButtonParams);
        p.setId(9);
        container.addView(p);
        this.pauseButton = p;
    }

    /**
     * set up a health bar representing the nanu's hp on the top left corner of the screen
     */
    private void setupHealthBar() {
        int h = 100;
        int w = 500;
        HealthBar hb = new HealthBar(this);
        RelativeLayout.LayoutParams healthBarParams = new RelativeLayout.
                LayoutParams(w, h);
        healthBarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        healthBarParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        healthBarParams.setMargins(10, 20, 0, 0);
        hb.setLayoutParams(healthBarParams);
        container.addView(hb);
        this.healthBar = hb;
    }

    /**
     * set up a nanu creature eating at the bottom of the game screen
     */
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

    /**
     * disable the back button on the phone
     */
    @Override
    public void onBackPressed() {

    }

    /**
     * Method to increase the rate of food item falls and their animation speed
     */

    @Override
    public void didGetBoosted() {
//        itemDropInterval /= 2;
//        itemDropDuration /= 2;
    }

    /**
     * Method to set the rate of food item falls and their animation speed back to normal
     */

    @Override
    public void boostWoreOff() {
//        itemDropInterval *= 2;
//        itemDropDuration *= 2;
    }

    /**
     * Method to end game when nanu's life reach zero, bring users to the FinishGameActivity when
     * Game is over.
     */
    @Override
    public void lifeReachedZero() {
        // game over functionality
        pauseButtonWasTapped(true);


        // Delete the saved game
        meUser.deleteGame(gameName, slot);


        String s = "" + this.score;
        Intent tmp = new Intent(this, FinishedGameActivity.class);
        tmp.putExtra("gameName", "FeedTheNanu");
        tmp.putExtra("gameScore", s);
        tmp.putExtra("gameIdentifier", "FeedTheNanu");
        tmp.putExtra("slot", slot);
        startActivity(tmp);

    }

    /**
     * Method update the score label to the current score
     * @param amount The amount the score should change by
     */
    @Override
    public void scoreShouldChangeBy(int amount) {
        score += amount;
        this.scoreLabel.setText(String.valueOf(score));
    }

    /**
     * Method to remove the edible item when the Nanu creature eats it, such that it
     * appears to the users that the creature had the food in its mouth.
     * @param item The Edible that is about to be eaten.
     */
    @Override
    public void aboutToEat(Edible item) {
        View v = (View) item;
        container.removeView(v);
    }

    /**
     * Method contains logic that moves the nanu creature
     */
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

    /**
     * method to return the width of the screen
     * @return width of the screen
     */
    private int screenWidth() {
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        return display.widthPixels;
    }

    /**
     * method to return to height of the screen
     * @return height of the screen
     */
    private int screenHeight() {
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        return display.heightPixels;
    }

    /**
     * While this is the delegate method called by the Pause Button,
     * we will also call this method ourselves in lifeDidReachZero and onBackPressed
     * @param paused true if the button has just been set to paused, false if the button has just been set to resumed.
     */
    @Override
    public void pauseButtonWasTapped(boolean paused) {

        this.isPaused = paused;

        // Pause/Resume non view stuff (like stop the handlers that generate new food, prevent Nanu from being moved)
        if (paused) {
            // Do a save
            this.save();

            itemDropHandler.removeCallbacks(itemDrop); // stop new items from falling
            updateHandler.removeCallbacks(update); // pause the game engine loop, for efficiency
            saveHandler.removeCallbacks(autoSave);
        } else {
            itemDropHandler.postDelayed(itemDrop, 1000); // we use post delayed here, to prevent the situation where spamming the pause button
            // caused an item to drop on each resume. But then it's possible to spam the pause button and progress time
            // without any food falling at all. For now, this is enough.
            updateHandler.post(update);
            saveHandler.post(autoSave);
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
