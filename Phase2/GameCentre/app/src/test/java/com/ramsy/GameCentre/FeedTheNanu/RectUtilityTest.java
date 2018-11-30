package com.ramsy.GameCentre.FeedTheNanu;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.RelativeLayout;

import org.junit.Test;

import static org.junit.Assert.*;

public class RectUtilityTest {
    Context context;

    RectUtility t = new RectUtility();

//    @Test
//    public void boundingBox() {
//        NanuPlayground nanuPlayground = new NanuPlayground();
//        View input = new View(context);
//        nanuPlayground.container.addView(input);
//        Rect output;
//        output = t.boundingBox(input);
//        Rect expected = new Rect(0, 0, 0,0);
//        assertEquals(expected, output);
//
//    }

    @Test
    public void padRect() {
        Rect inputRect = new Rect(0,0,0,0);
        int[] inputList = new int[] {3,4,5,6};
        int expectedLeft = -3;
        int expectedTop = -4;
        int expectedRight = 5;
        int expectedBottom = 6;
        t.padRect(inputRect, inputList);
        assertEquals(expectedLeft, inputRect.left);
        assertEquals(expectedTop, inputRect.top);
        assertEquals(expectedRight, inputRect.right);
        assertEquals(expectedBottom, inputRect.bottom);
    }
}



