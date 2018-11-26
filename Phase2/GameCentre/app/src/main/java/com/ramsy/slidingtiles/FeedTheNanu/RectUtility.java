package com.ramsy.slidingtiles.FeedTheNanu;

import android.graphics.Rect;
import android.view.View;

/**
 * A Utility with static methods for Rect related functionality
 */

class RectUtility {

    static Rect boundingBox(View v) {
        // Takes in a view and returns a bounding rect in the screen's reference frame.

        int[] a = new int[2];
        v.getLocationOnScreen(a);

        int[] result = new int[4];
        result[0] = a[0];
        result[1] = a[1];
        result[2] = a[0] + v.getWidth();
        result[3] = a[1] + v.getHeight();

        return new Rect(result[0], result[1], result[2], result[3]);
    }

    static void padRect(Rect r, int[] paddingValues) {
        // Takes in a rect and an int array of 4 values,
        // representing how much to pad the rect by.
        // The order is left padding, top padding, right padding, and bottom padding.
        // Pads the passed in rect accordingly
        // Values can be positive or negative.

        r.left -= paddingValues[0];
        r.top -= paddingValues[1];
        r.right += paddingValues[2];
        r.bottom += paddingValues[3];

    }

}
