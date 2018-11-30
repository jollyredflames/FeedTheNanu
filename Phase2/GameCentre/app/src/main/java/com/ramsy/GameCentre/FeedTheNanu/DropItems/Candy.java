package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.ramsy.GameCentre.FeedTheNanu.DropItem;
import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.GameCentre.R;

public class Candy extends DropItem implements Edible {

    static Bitmap texture;

    public Candy(Context context){
        super(context);

        if (Candy.texture == null) {
            Candy.texture = BitmapFactory.decodeResource(getResources(), R.drawable.candy);
        }
        this.setImageBitmap(Candy.texture);


    }

    @Override
    public int effectOnLife() {
        return 10;
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