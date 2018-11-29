package com.ramsy.GameCentre.FeedTheNanu;

import android.graphics.Rect;
import android.view.View;

/**
 * A Utility with static methods for Rect related functionality
 */

class RectUtility {

    /**
     * Takes in a view and returns a bounding rect in the screen's reference frame.
     * @param v the view to calculate a bounding rect for
     * @return the bounding rect of v in the screen's reference frame.
     */

    static Rect boundingBox(View v) {
        int[] a = new int[2];
        v.getLocationOnScreen(a);

        int[] result = new int[4];
        result[0] = a[0];
        result[1] = a[1];
        result[2] = a[0] + v.getWidth();
        result[3] = a[1] + v.getHeight();

        return new Rect(result[0], result[1], result[2], result[3]);
    }


    /**
     * Takes in a rect and an int array of 4 values, representing how much padding to apply on its
     * left, top, right, and bottom, respectively.
     * This modifies the passed in rect.
     * Values can be positive or negative.
     * Positive values increase a rect's size, while negative values decrease it.
     * @param r the rect to pad
     * @param paddingValues an int array of 4 values representing how much padding to apply on the passed in rect's
     * left, top, right, and bottom, respectively.
     */

    static void padRect(Rect r, int[] paddingValues) {
        r.left -= paddingValues[0];
        r.top -= paddingValues[1];
        r.right += paddingValues[2];
        r.bottom += paddingValues[3];
    }

}
