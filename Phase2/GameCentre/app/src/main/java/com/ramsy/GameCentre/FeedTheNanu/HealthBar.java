package com.ramsy.GameCentre.FeedTheNanu;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import com.ramsy.GameCentre.R;


public class HealthBar extends RelativeLayout {
    View health;


    HealthBar(Context context){

        super(context);

        // Added these lines to have a background view that is white and alpha'd, to mimic the style of the score label
        View b = new View(context);
        b.setBackgroundColor(Color.WHITE);
        b.setAlpha(0.5f);
        this.addView(b);

        health = new View(context);
        health.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//        this.setBackgroundColor(getResources().getColor(R.color.app_button1));
        setHealthTo(1f);
        this.addView(health);



    }


    public void setHealthTo(float f){

        // health bar need to have been laid out before this method is called
        //precondition, float has to be between 0 and 1 inclusive
        int width = this.getWidth();
        this.health.setX((width * f) - width);
    }



}
