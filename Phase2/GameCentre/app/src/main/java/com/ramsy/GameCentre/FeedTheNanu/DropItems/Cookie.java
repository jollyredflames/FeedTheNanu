package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.slidingtiles.R;

public class Cookie extends android.support.v7.widget.AppCompatImageView implements Edible{

    Bitmap image;

    public Cookie(Context context){
        super(context);
        this.image = BitmapFactory.decodeResource(getResources(), R.drawable.cookie);
        this.setImageBitmap(this.image);

    }

    @Override
    public int effectOnLife() {
        return 1;
    }

    @Override
    public int effectOnScore() {
        return 100;
    }

    @Override
    public int effectOnSpeed() {
        return 1;
    }
}
