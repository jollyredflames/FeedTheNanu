package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;


import com.ramsy.GameCentre.FeedTheNanu.DropItem;
import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.GameCentre.R;

public class Coffee extends DropItem implements Edible {

    static Bitmap texture = BitmapFactory.decodeResource(getResources(), R.drawable.coffee);

    public Coffee(Context context){
        super(context);
        this.setImageBitmap(Coffee.texture);

    }

    @Override
    public int effectOnLife() {
        return 1;
    }

    @Override
    public int effectOnScore() {
        return 200;
    }

    @Override
    public int effectOnSpeed() {
        return 2;
    }
}


