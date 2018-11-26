package com.ramsy.slidingtiles.FeedTheNanu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ramsy.slidingtiles.R;

public class Poison extends android.support.v7.widget.AppCompatImageView {
    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.poison);
    public Poison(Context context){
        super(context);
        this.setImageBitmap(this.image);
    }


}
