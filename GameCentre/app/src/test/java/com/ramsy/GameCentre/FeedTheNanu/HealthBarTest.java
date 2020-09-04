//package com.ramsy.GameCentre.FeedTheNanu;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class HealthBarTest {
//    Context context;
//    RelativeLayout t = new RelativeLayout(context);
//    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(10, 20);
//
//
//
//    @Test
//    public void setHealthTo() {
//        HealthBar hb = new HealthBar(context);
//        t.addView(hb);
//        int width = hb.health.getWidth();
//        hb.health.setX(1f);
//        hb.setHealthTo(0.2f);
//        assertEquals(Color.WHITE, ((ColorDrawable) hb.getBackground()).getColor());
//        assertEquals(Math.round((width * 0.2f)-width), Math.round(hb.health.getX()));
//
//
//
//
//    }
//}