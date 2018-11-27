package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.slidingtiles.R;

public class Spider extends android.support.v7.widget.AppCompatImageView implements Edible {

    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.spider);
    public Spider(Context context){
        super(context);
        this.setImageBitmap(this.image);
//        this.setMinimumHeight(150);
//        this.setMaxHeight(150);
    }


    @Override
    public int effectOnLife() {
        return -1;
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

