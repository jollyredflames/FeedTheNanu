package com.ramsy.GameCentre.FeedTheNanu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

public class NanuPlayground extends AppCompatActivity {
    HealthBar hb;
    int healthBarW;
    int healthBarH;
    public RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        healthBarW = screenWidth()/3;
        healthBarH = screenHeight()/15;
        container = new RelativeLayout(this);
        hb = new HealthBar(this);
        RelativeLayout.LayoutParams healthBarParams = new RelativeLayout.
                LayoutParams(healthBarW, healthBarH);
        healthBarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        healthBarParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        healthBarParams.setMargins(10, 10, 0, 0);
        hb.setLayoutParams(healthBarParams);
        container.addView(hb);
        setContentView(container);
        hb.setHealthTo(0.5f);

    }

    private int screenWidth() {
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        return display.widthPixels;
    }

    private int screenHeight() {
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        return display.heightPixels;
    }


}

