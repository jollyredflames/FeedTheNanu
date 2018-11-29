package com.ramsy.GameCentre.FeedTheNanu;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import com.ramsy.GameCentre.R;


public class HealthBar extends RelativeLayout {
    View health;


    HealthBar(Context context){

        super(context);
        health = new View(context);
        health.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        this.setBackgroundColor(getResources().getColor(R.color.app_button1));
        setHealthTo(1f);
        this.addView(health);

    }


    public void setHealthTo(float f){
        // health bar need to have been laid out before this method is called
        //precondition, float has to be between 0 and 1 inclusive
        int height = this.getHeight();
        int width = this.getWidth();
        System.out.println(height + "lala nanu height");
        System.out.println(width + "lala nanu width");
        RelativeLayout.LayoutParams h = new RelativeLayout.LayoutParams(Math.round(width * f), height);

        this.health.setLayoutParams(h);
    }


}
