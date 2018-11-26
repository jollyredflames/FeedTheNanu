package com.ramsy.slidingtiles.FeedTheNanu;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.Random;


public class NanuGameActivity extends AppCompatActivity {


    String[] drop;
    ImageView toDrop;
    RelativeLayout bin;
    int life;
    int eaten;
    int width;
    int height;
    int dropSpeed;
    // repeat the animation of food falling
    Handler handler = new Handler();
    Runnable repeat = new Runnable() {
        public void run() {
            generateFood();
            handler.postDelayed(this, 1000);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getting width and height of the screen
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        this.width = display.widthPixels;
        this.height = display.heightPixels;

        //initialize life and eaten
        this.life = 3;
        this.eaten = 0;
        this.dropSpeed = 2000;
//        set up drop
        this.drop = new String[]{"cookie", "cookie", "p", "t", "t", "p","coffee", "p", "m", "p", "m"};
        //set up bin
        this.bin = new RelativeLayout(this);
        setContentView(bin);
        System.out.println(bin.getWidth() + "nana Width");
        System.out.println(bin.getHeight() + "nana Height");
        handler.post(repeat);
    }

    void generateFood(){

        toDrop = chooseItem();
        //display and start dropping item's animation
        bin.addView(toDrop);
        //setting the size of the image
        RelativeLayout.LayoutParams foodParam = new RelativeLayout.LayoutParams(200,
                200);
        Random location = new Random();
        foodParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        foodParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        //let it showup in random location of the top of the screen
        foodParam.setMargins(location.nextInt(width-100) + 100,
                100, 0, 0);
        toDrop.setLayoutParams(foodParam);

        ViewPropertyAnimator animation = toDrop.animate().setDuration(dropSpeed).
                setInterpolator(new LinearInterpolator());
        animation.y(height + height/10);
        animation.withEndAction(new Runnable() {
            @Override
            public void run() {
                bin.removeView(toDrop);
            }
        });


    }


    ImageView chooseItem() {
        Random ran = new Random();
        int i = ran.nextInt(11);
        System.out.println(i + "lala");
        if (drop[i].equals("p")){
            return new Poison(this);
        }
        else if (drop[i].equals("coffee")){
            return new Coffee(this);
        }
        else if (drop[i].equals("cookie")){
            return new Cookie(this);
        }
        else if (drop[i].equals("m")){
            return new Melon(this);
        }
        else{
            return new Tomato(this);
        }


    }

    //        Cookie cookie1;
//        Cookie cookie2;
//        Tomato tomato1;
//        Tomato tomato2;
//        Coffee coffee;
//        Poison poison1;
//        Poison poison2;
//        Poison poison3;
//        Poison poison4;
//        Melon melon1;
//        Melon melon2;
//        cookie1 = new Cookie(this);
//        cookie2 = new Cookie(this);
//        tomato1 = new Tomato(this);
//        tomato2 = new Tomato(this);
//        coffee = new Coffee(this);
//        poison1 = new Poison(this);
//        poison2 = new Poison(this);
//        poison3 = new Poison(this);
//        poison4 = new Poison(this);
//        melon1 = new Melon(this);
//        melon2 = new Melon(this);
//        drop = new ImageView[] {cookie1, cookie2, poison1, tomato1, tomato2,
//            poison2, coffee, poison3, melon2, poison4, melon1};

    //get original location on screen
//                int[] loc = new int[2];
//                toDrop[0].getLocationOnScreen(loc);


}

