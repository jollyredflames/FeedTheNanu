package com.ramsy.GameCentre.FeedTheNanu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import com.ramsy.slidingtiles.R;

class FoodItem extends ImageView implements Edible {

    FoodItem(Context context) {
        super(context);
        Bitmap im = BitmapFactory.decodeResource(getResources(), R.drawable.candy);
        this.setImageBitmap(im);
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
