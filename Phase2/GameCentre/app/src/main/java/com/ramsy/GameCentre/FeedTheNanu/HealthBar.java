package com.ramsy.GameCentre.FeedTheNanu;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import com.ramsy.GameCentre.R;


public class HealthBar extends RelativeLayout {
    View health;
    int time;


    HealthBar(Context context){

        super(context);

        // Added these lines to have a background view that is white and alpha'd, to mimic the style of the score label
        View b = new View(context);
        b.setBackgroundColor(Color.WHITE);
        b.setAlpha(0.5f);
        this.addView(b);

        health = new View(context);
        health.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        this.time = 1500;
//        this.setBackgroundColor(getResources().getColor(R.color.app_button1));
        setHealthTo(1f);
        this.addView(health);



    }

    // health bar need to have been laid out before this method is called
    public void setHealthTo(float f){
        int width = this.getWidth();
        this.health.setX((width * f) - width);
    }



}
