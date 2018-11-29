package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.ramsy.GameCentre.FeedTheNanu.DropItem;
import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.GameCentre.R;

public class Donut extends DropItem implements Edible{

    static Bitmap texture;

    public Donut(Context context){
        super(context);

        if (Donut.texture == null) {
            Donut.texture = BitmapFactory.decodeResource(getResources(), R.drawable.donut);
        }
        this.setImageBitmap(Donut.texture);

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
