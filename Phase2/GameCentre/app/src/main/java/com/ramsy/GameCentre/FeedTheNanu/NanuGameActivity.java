package com.ramsy.GameCentre.FeedTheNanu;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.Random;
import com.ramsy.GameCentre.FeedTheNanu.DropItems.*;
import com.ramsy.slidingtiles.R;
public class NanuGameActivity extends AppCompatActivity {


    String[] drop;
    RelativeLayout bin;
    boolean isPaused;
    int life;
    int eaten;
    int width;
    int height;
    int dropSpeed;
    View pause;
    // repeat the animation of food falling
    Handler handler = new Handler();
    Runnable repeat = new Runnable() {
        public void run() {
            generateFood();
            handler.postDelayed(this, 1500);
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
        System.out.println(this.height + "height");
        this.isPaused = false;

        //initialize life and eaten
        this.life = 3;
        this.eaten = 0;
        this.dropSpeed = 2500;
//        set up drop so that items have a certain probability of dropping
        this.drop = new String[]{"candy", "candy", "p", "cup", "cup", "p", "coffee", "p", "d", "p", "d"};
        //set up bin
        this.bin = new RelativeLayout(this);
        Resources res = getResources();
        Drawable bg = res.getDrawable(R.drawable.desert);
        bin.setBackground(bg);
        setContentView(bin);
//        Nanu nanu = new Nanu(this);
//        bin.addView(nanu);


        RelativeLayout viewGroup = new RelativeLayout(this);

        // Assign a layout params to viewGroup to position it within the main view group (bin)
        RelativeLayout.LayoutParams toolbar = new RelativeLayout.LayoutParams(width, 80);
        viewGroup.setLayoutParams(toolbar);
        toolbar.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        viewGroup.setBackgroundColor(getColor(R.color.white));

        this.pause = new View(this);
        pause.setBackgroundColor(getColor(R.color.app_theme));

//        setPauseOnClickListener();

        // Customize v1 here
        viewGroup.addView(pause);
        RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams(width / 4, 80);
        pause.setLayoutParams(pa);
        bin.addView(viewGroup);
        handler.post(repeat);
    }

    void generateFood() {
        ItemGenerator ch = new ItemGenerator(this);
        ImageView toDrop = ch.chooseItem(this);
        //display and start dropping item's animation
        //fixing the size of the image view
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) toDrop.getDrawable());
        Bitmap bitmap = bitmapDrawable.getBitmap();
        int y = bitmap.getHeight();
        int x = bitmap.getWidth();

        bin.addView(toDrop);
        //setting the size of the image
        RelativeLayout.LayoutParams foodParam = new RelativeLayout.LayoutParams(125, 125);
        //let it show up in random location of the top of the screen
        Random location = new Random();
        int i = location.nextInt(this.width - 200) + 100;
        toDrop.setX((float) i);
        toDrop.setY(0f);
//        foodParam.setMargins(i,
//                -100, 0, 0);
        toDrop.setLayoutParams(foodParam);
//        toDrop.getLayoutParams().height = 150;
//        toDrop.getLayoutParams().width = 150;

        //start the animation
        ViewPropertyAnimator animation = toDrop.animate().setDuration(this.dropSpeed).
                setInterpolator(new LinearInterpolator());
        if (isPaused) {
            toDrop.setAlpha(0f);
            pause.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        } else {
            animation.y(this.height);
        }
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
        if (drop[i].equals("candy")) {
            return new Candy(this);
        } else if (drop[i].equals("coffee")) {
            return new Coffee(this);
        } else if (drop[i].equals("cup")) {
            return new Cupcake(this);
        } else if (drop[i].equals("d")) {
            return new Donut(this);
        } else {
            return new Spider(this);
        }
    }

    // set a pause button somewhere on the screen
    // once it is press, the game would be pause
//
//    void setPauseOnClickListener(){
//        pause.setOnClickListener((V)-> {
//            if(isPaused){this.isPaused = false;}
//            else{this.isPaused = true;}
//
//        });
//
//    }


//get original location on screen
//                int[] loc = new int[2];
//                toDrop[0].getLocationOnScreen(loc);


}