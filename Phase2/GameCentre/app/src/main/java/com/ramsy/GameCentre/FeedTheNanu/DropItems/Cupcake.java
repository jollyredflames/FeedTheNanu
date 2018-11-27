package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.GameCentre.R;

public class Cupcake extends android.support.v7.widget.AppCompatImageView implements Edible{

    Bitmap image;

    public Cupcake(Context context){
        super(context);
        this.image = BitmapFactory.decodeResource(getResources(), R.drawable.cupcake);
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
