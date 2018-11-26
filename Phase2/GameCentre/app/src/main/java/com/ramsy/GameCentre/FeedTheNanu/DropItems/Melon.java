package com.ramsy.GameCentre.FeedTheNanu.DropItems;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ramsy.slidingtiles.R;

public class Melon extends android.support.v7.widget.AppCompatImageView {

    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.melon);
    public Melon(Context context){
        super(context);
        this.setImageBitmap(this.image);
    }

}
