package com.ramsy.GameCentre.FeedTheNanu;

import android.content.Context;
import android.graphics.Rect;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RectUtilityTest {
    Context context;

    RectUtility t = new RectUtility();

//    @Test
//    public void boundingBox() {
//        View input = new View(context);
//        nanuPlayground.container.addView(input);
//        Rect output;
//        output = t.boundingBox(input);
//        Rect expected = new Rect(0, 0, 0,0);
//        assertEquals(expected, output);
//
//    }

    /**
     * unit test to check if padRect return the desire Rect
     */
    @Test
    public void padRect() {
        Rect inputRect = new Rect(0,0,0,0);
        int[] inputList = new int[] {3,4,5,6};
        int expectedLeft = -3;
        int expectedTop = -4;
        int expectedRight = 5;
        int expectedBottom = 6;
        RectUtility.padRect(inputRect, inputList);
        assertEquals(expectedLeft, inputRect.left);
        assertEquals(expectedTop, inputRect.top);
        assertEquals(expectedRight, inputRect.right);
        assertEquals(expectedBottom, inputRect.bottom);
    }
}



