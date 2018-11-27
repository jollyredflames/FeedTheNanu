package com.ramsy.GameCentre.FeedTheNanu;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
    boolean pause;
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
        this.pause = false;

        //initialize life and eaten
        this.life = 3;
        this.eaten = 0;
        this.dropSpeed = 2500;
//        set up drop so that items have a certain probability of dropping
        this.drop = new String[]{"candy", "candy", "p", "cup", "cup", "p","coffee", "p", "d", "p", "d"};
        //set up bin
        this.bin = new RelativeLayout(this);
        Resources res = getResources();
        Drawable bg = res.getDrawable(R.drawable.desert);
        bin.setBackground(bg);
        setContentView(bin);
        handler.post(repeat);
    }

    void generateFood(){

        ImageView toDrop = chooseItem();
        //display and start dropping item's animation
        //fixing the size of the image view
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) toDrop.getDrawable());
        Bitmap bitmap = bitmapDrawable .getBitmap();
//        int y = bitmap.getHeight();
//        int x = bitmap.getWidth();

        bin.addView(toDrop);
        //setting the size of the image
        RelativeLayout.LayoutParams foodParam = new RelativeLayout.LayoutParams(150,150);
//        foodParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        foodParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        foodParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //let it show up in random location of the top of the screen
        Random location = new Random();
        int i = location.nextInt(this.width - 200) + 150;
        toDrop.setX((float)i);
        toDrop.setY(0f);
//        foodParam.setMargins(i,
//                -100, 0, 0);
        toDrop.setLayoutParams(foodParam);
//        toDrop.getLayoutParams().height = 150;
//        toDrop.getLayoutParams().width = 150;

        ViewPropertyAnimator animation = toDrop.animate().setDuration(this.dropSpeed).
                setInterpolator(new LinearInterpolator());
        animation.y(this.height).withEndAction(new Runnable() {
            @Override
            public void run() { bin.removeView(toDrop);
            }
        });

    }


    ImageView chooseItem() {
        Random ran = new Random();
        int i = ran.nextInt(11);
        System.out.println(i + "lala");
        if (drop[i].equals("candy")){
            return new Candy(this);
        }
        else if (drop[i].equals("coffee")){
            return new Coffee(this);
        }
        else if (drop[i].equals("cup")){
            return new Cupcake(this);
        }
        else if (drop[i].equals("d")){
            return new Donut(this);
        }
        else{
            return new Spider(this);
        }



    }








}



//get original location on screen
//                int[] loc = new int[2];
//                toDrop[0].getLocationOnScreen(loc);


