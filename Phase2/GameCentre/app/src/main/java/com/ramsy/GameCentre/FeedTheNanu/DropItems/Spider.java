package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ramsy.GameCentre.FeedTheNanu.DropItem;
import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.GameCentre.R;

public class Spider extends DropItem implements Edible {

    static Bitmap texture;

    public Spider(Context context){
        super(context);

        if (Spider.texture == null) {
            Spider.texture = BitmapFactory.decodeResource(getResources(), R.drawable.spider);
        }
        this.setImageBitmap(Spider.texture);

//        this.setMinimumHeight(150);
//        this.setMaxHeight(150);
    }


    @Override
    public int effectOnLife() {
        return -10;
    }

    @Override
    public int effectOnScore() {
        return -200;
    }

    @Override
    public int effectOnSpeed() {
        return 1;
    }
}
