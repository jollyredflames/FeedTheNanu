package com.ramsy.GameCentre.FeedTheNanu;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import com.ramsy.GameCentre.R;


public class HealthBar extends RelativeLayout {
    View health;
    int time;


    HealthBar(Context context){

        super(context);
        health = new View(context);
        health.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        this.time = 1500;
        this.setBackgroundColor(getResources().getColor(R.color.app_button1));
        setHealthTo(1f);
        this.addView(health);

    }

    // health bar need to have been laid out before this method is called
    public void setHealthTo(float f){
        int height = this.getHeight();
        int width = this.getWidth();
        System.out.println(height + "lala nanu height");
        System.out.println(width + "lala nanu width");
        RelativeLayout.LayoutParams h = new RelativeLayout.LayoutParams(Math.round(width * f), height);

        this.health.setLayoutParams(h);
    }



}
