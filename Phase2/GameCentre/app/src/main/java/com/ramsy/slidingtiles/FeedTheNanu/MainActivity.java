package com.ramsy.slidingtiles.FeedTheNanu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowId;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NanuDelegate {


    Nanu nanu;
    RelativeLayout container;
    View background;




    Handler handler = new Handler();

    Handler handler2 = new Handler();

    Runnable itemDropTest = new Runnable() {
        @Override
        public void run() {
            // Create a box view
            final FoodItem box = new FoodItem(MainActivity.this);
            box.setBackgroundColor(Color.BLUE);

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(50, 50);
            p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            p.addRule(RelativeLayout.CENTER_HORIZONTAL);
            box.setLayoutParams(p);
            box.setId(10);

            container.addView(box);


            // Get screen height
            DisplayMetrics display = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(display);
            int h = display.heightPixels;

            // Schedule an animation
            ViewPropertyAnimator anim = box.animate().setDuration(5000).setInterpolator(new LinearInterpolator());
            anim.y(h);
            anim.withEndAction(new Runnable() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            container.removeView(box);
                        }
                    });



                }
            });

            handler2.postDelayed(this, 800);
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

        // Create a View Group
        final RelativeLayout container = new RelativeLayout(this);
        this.container = container;

        // Set the Activity's window to the View Group
        setContentView(container);

        View v = new View(this);
//        v.setBackgroundColor(Color.RED);
        container.addView(v);
        v.setOnClickListener(this);
        this.background = v;



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
        nanuParams.addRule(RelativeLayout.CENTER_VERTICAL);
        nanuParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        n.setLayoutParams(nanuParams);



        container.addView(n);
        this.nanu = n;
        nanu.delegate = this;
        n.start();


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

//        this.nanu.foodIsNearby = !this.nanu.foodIsNearby;

//        System.out.println("XXX  TAP!");
//
//
        // Create a box view
        final FoodItem box = new FoodItem(this);
        box.setBackgroundColor(Color.BLUE);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(50, 50);
        p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);
        box.setLayoutParams(p);
        box.setId(10);

        this.container.addView(box);


        // Get screen height
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int h = display.heightPixels;

        // Schedule an animation
        ViewPropertyAnimator anim = box.animate().setDuration(5000).setInterpolator(new LinearInterpolator());
        anim.y(h);
        anim.withEndAction(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        container.removeView(box);
                    }
                });



            }
        });


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
}
