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
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.ramsy.GameCentre.R;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, NanuDelegate, PauseButtonDelegate {

    // TODO:
    // - Lock to portrait orientation only (app wide?)
    // - Disable status bar
    // - Create pause / resume buttons

    // - Have an interface that requires conforming classes to have a method that returns an ObjectAnimator.
    // - Have all the drop items (which are already subclasses of ImageView, in order to conform to Edible)
    // conform to this interface as well. So when a pause button is pressed, we loop over all views in the hierarchy,
    // and if the cast to the interface type succeeds, we get the animator object, and call pause / resume on it.

    // Instead of it being a method, make it a property.
    // Assign that property in the method that creates the new view, and a new animator object.

    // Have a Pausible interface with a pause and resume method.
    // Classes confrom to the Pausible interface by implementing that method.
    // So we don't even need to have the client grab the animator and pause / resume it.
    // That way we can have the Nanu use it too, and it will implement it differently as it has no animator object.
    // So we can pause everything in the hieracrchy in one fell swoop.
    // So the food items need to have a property that stores their animator object.
    // And the method that creates them and their animation, needs to be working with a type such that that property
    // is visible to the compiler.
    // So don't work with them there as a Pausible, but perhaps as a Fallable instead?

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
    ImageView background;
    ItemGenerator itemGenerator;
    final int padding = 100;




    Handler handler = new Handler();

    Handler handler2 = new Handler();


    Runnable itemDropTest = new Runnable() {
        @Override
        public void run() {
            // Create a box view
//            final FoodItem box = new FoodItem(MainActivity.this);
            DropItem box = (DropItem)itemGenerator.getItem();
            box.setId(10);



//            box.setBackgroundColor(Color.BLUE);

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(125, 125);
//            p.addRule(RelativeLayout.);
            box.setY(-150);
            p.addRule(RelativeLayout.CENTER_HORIZONTAL);
            box.setLayoutParams(p);

            Random random = new Random();
            int dropWidth = 125;
            float positionX = random.nextInt(screenWidth() - padding * 2 - dropWidth) + padding;
            box.setX(positionX);
//            p.addRule(RelativeLayout.CENTER_HORIZONTAL);
//            box.setId(10);

            container.addView(box);




            // Schedule an animation
//            ViewPropertyAnimator anim = box.animate().setDuration(5000).setInterpolator(new LinearInterpolator());
//            anim.y(h);
//            anim.withEndAction(new Runnable() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            container.removeView(box);
//                        }
//                    });
//                }
//            });


            // Trying a pausible animation
            ObjectAnimator anim = ObjectAnimator.ofFloat(box, "y", screenHeight());
            anim.setDuration(5000);
            anim.setInterpolator(null);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    System.out.println("XXX Animation just ended");
                    container.removeView(box);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            box.animator = anim;
            anim.start();

            // But now, how to store all these animations so we can pause them later?


            handler2.postDelayed(this, 1200);
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

            handler.postDelayed(this, 20);
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        this.itemGenerator = new ItemGenerator(this);
        // Create a View Group
        final RelativeLayout container = new RelativeLayout(this);
        this.container = container;

        // Set the Activity's window to the View Group
        setContentView(container);

//        View v = new View(this);
        ImageView v = new ImageView(this);
        Bitmap im = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
        v.setImageBitmap(im);
        v.setScaleType(ImageView.ScaleType.CENTER_CROP);

//        RelativeLayout.LayoutParams backgroundParams = new RelativeLayout.LayoutParams(0, 0);
//        backgroundParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        backgroundParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        backgroundParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        backgroundParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        v.setLayoutParams(backgroundParams);

//        v.setBackgroundColor(Color.RED);

        container.addView(v);
        v.setOnTouchListener(this);
//        v.setOnDragListener(this);
        this.background = v;

//        // Create pause resume toggle button
//        View pauseButton = new View(this);
//        pauseButton.setBackgroundColor(Color.BLUE);
//        pauseButton.setX(100);
//        pauseButton.setY(100);
//        pauseButton.setOnClickListener(this);
//
//        RelativeLayout.LayoutParams pauseButtonParams = new RelativeLayout.LayoutParams(100, 100);
//        pauseButton.setLayoutParams(pauseButtonParams);
//        container.addView(pauseButton);

        PauseButton p = new PauseButton(this);
        p.delegate = this;

        RelativeLayout.LayoutParams pauseButtonParams = new RelativeLayout.LayoutParams(175, 175);
        pauseButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        pauseButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pauseButtonParams.setMargins(0, 10, 10, 0);
        p.setLayoutParams(pauseButtonParams);
        container.addView(p);



        // Create an Image View
//        ImageView imv = new ImageView(this);
//////        imv.setScaleType(ImageView.ScaleType.);
//////        imv.setBackgroundColor(Color.BLUE);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(250, 250);
////        // By default, it seems like the bitmap is set to 'scale aspect fill' on the image view
//        params.addRule(RelativeLayout.CENTER_VERTICAL);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        imv.setLayoutParams(params);
//        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.nanu1);
//        imv.setImageBitmap(image);
//        container.addView(imv);
//        this.imv = imv;

        Nanu n = new Nanu(this);


        RelativeLayout.LayoutParams nanuParams = new RelativeLayout.LayoutParams(250, 250);
        nanuParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        nanuParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        nanuParams.setMargins(0, 0, 0, 50);
        n.setLayoutParams(nanuParams);



        container.addView(n);
        this.nanu = n;
        nanu.delegate = this;
        n.resume();


        handler.post(update);
        handler2.post(itemDropTest);























//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//                // Need quick access to all box views
//
//
////                Set<View> toRemove = new HashSet<View>();
////                System.out.println("XXX number of falling items: " + fallingViews.size());
//
//
//
//
//
//
////                boolean flag = false;
////                for (View each : fallingViews) {
////
////                    // Print it's y coordinate
//////                    int[] a = new int[2];
//////                    each.getLocationOnScreen(a);
//////                    if (a[1] >= 2000) {
////                        // Schedule the view for removal from the set
//////                        toRemove.add(each);
//////                    }
//////                    System.out.println("XXX y: " + a[1]);
////
////                    Rect r1 = nanu.getProximityZone();
////                    Rect r2 = RectUtility.boundingBox(each);
////                    if (r1.intersects(r2.left, r2.top, r2.right, r2.bottom)) {
////                        flag = true;
////                        break;
////                    }
////
//////                    System.out.println("XXX hi");
////                }
//
////                System.out.println("XXX about to remove a view from the container");
////                container.removeView(each);
////                System.out.println("XXX successfully removed a view from the container");
////                fallingViews.remove(each);
//
//
//
////
//
//
//            }
//        }, 0, 100);

    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void lifeDidChangeBy(int amount) {

    }

    @Override
    public void scoreShouldChangeBy(int amount) {

    }

    @Override
    public void aboutToEat(Edible item) {
        View v = (View) item;
        container.removeView(v);
    }


    float oldX;

    boolean isPaused = false;

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
            handler2.removeCallbacks(itemDropTest); // stop new items from falling
            handler.removeCallbacks(update); // pause the game engine loop, for efficiency
        } else {
            handler2.postDelayed(itemDropTest, 1000); // we use post delayed here, to prevent the situation where spamming the pause button
            // caused an item to drop on each resume. But then it's possible to spam the pause button and progress time
            // without any food falling at all. For now, this is enough.
            handler.post(update);
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
