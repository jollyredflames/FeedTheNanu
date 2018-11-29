package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.ramsy.GameCentre.FeedTheNanu.DropItem;
import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.GameCentre.R;

public class Cupcake extends DropItem implements Edible{

    static Bitmap texture;

    public Cupcake(Context context){
        super(context);

        if (Cupcake.texture == null) {
            Cupcake.texture = BitmapFactory.decodeResource(getResources(), R.drawable.cupcake);
        }
        this.setImageBitmap(Cupcake.texture);

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
