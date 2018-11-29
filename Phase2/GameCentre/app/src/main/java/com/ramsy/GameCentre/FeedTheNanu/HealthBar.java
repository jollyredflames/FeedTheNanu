package com.ramsy.GameCentre.FeedTheNanu;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.os.Handler;
import java.util.logging.LogRecord;


public class HealthBar extends RelativeLayout {
    View health;
    int barWidth;
    int barHeight;
    int time;
    Handler handle = new Handler();
    Runnable decreaseByTime = new Runnable() {
        @Override
        public void run() {
            //decrease heath
            decreaseHealth();
            handle.postDelayed(this, time);
        }
    };

    HealthBar(Context context){

        super(context);
        health = new View(context);
        this.addView(health);
        this.time = 1500;
        this.barHeight = this.getHeight();
        this.barWidth = this.getWidth();
        handle.post(decreaseByTime);

    }


    void decreaseHealth(){
        int healthValue = health.getWidth();
        RelativeLayout.LayoutParams h = new LayoutParams(healthValue - barWidth/10, barHeight);
        health.setLayoutParams(h);

    };

    void increaseHealth(){
        int healthValue = health.getWidth();
        RelativeLayout.LayoutParams h = new LayoutParams(healthValue + barWidth/10, barHeight);
        health.setLayoutParams(h);

    }


}
