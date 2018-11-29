package com.ramsy.GameCentre.FeedTheNanu;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import com.ramsy.GameCentre.R;


public class HealthBar extends RelativeLayout {
    View health;
    int time;


    HealthBar(Context context, float percent, int width, int height){

        super(context);
        health = new View(context);
        health.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        this.time = 1500;
        this.setBackgroundColor(getResources().getColor(R.color.app_button1));
        setHealthTo(percent, width, height);
        this.addView(health);

    }


    public void setHealthTo(float f, int barHeight, int barWidth){
        System.out.println(barHeight + "nanu nanu height");
        System.out.println(barWidth + "nanu nanu width");
        RelativeLayout.LayoutParams h = new LayoutParams(Math.round(barHeight * f), barWidth);
        this.health.setLayoutParams(h);
    }



}
