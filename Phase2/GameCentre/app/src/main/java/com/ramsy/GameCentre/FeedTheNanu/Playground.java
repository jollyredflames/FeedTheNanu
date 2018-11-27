package com.ramsy.GameCentre.FeedTheNanu;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class Playground extends AppCompatActivity implements View.OnClickListener {


    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_playground);


        /*
        Figure out how to do collision tests.
        The Nanu needs to provide it's trigger zone in screen coordinates
        (call get location on screen, and it'll give the top left corner's coordinates on screen.
        Create a new rect with the left and top values set at those x and y values.
        Set the right to x + get width
        Set the bottom to y + get height.


         */

        // Create a View Group
        RelativeLayout container = new RelativeLayout(this);

        // Set the Activity's window to the View Group
        setContentView(container);

        // Set Background View
        View b = new View(this);
        container.addView(b);
        b.setOnClickListener(this);



        // Create a View
        View v = new View(this);
        v.setBackgroundColor(Color.BLUE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(250, 250);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        v.setLayoutParams(params);

        container.addView(v);
        this.v = v;


        Rect r1 = new Rect(100, 100, 200, 200);

        System.out.println("XXX   " + r1.intersects(300, 200, 200 + 100, 200 + 100));




    }

    @Override
    public void onClick(View v) {

        Rect r = new Rect();
        this.v.getDrawingRect(r); // this is the rect in its own reference frame. Useless.

        int[] foo = new int[2];
        this.v.getLocationOnScreen(foo);

        System.out.println("XXX x: " + foo[0] + " y: " + foo[1]);

//        System.out.println("XXX   left: " + r.left + " right: " + r.right + " top: " + r.top + " bottom: " + r.bottom);
    }
}
