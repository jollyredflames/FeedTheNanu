package com.ramsy.slidingtiles.FeedTheNanu;

import android.content.Context;
import android.view.View;

class FoodItem extends View implements Edible {

    FoodItem(Context context) {
        super(context);
    }

    @Override
    public int effectOnLife() {
        return 0;
    }

    @Override
    public int effectOnScore() {
        return 0;
    }

    @Override
    public int effectOnSpeed() {
        return 1;
    }
}
