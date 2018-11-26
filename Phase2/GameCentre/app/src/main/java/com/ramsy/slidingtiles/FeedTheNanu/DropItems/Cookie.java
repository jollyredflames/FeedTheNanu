package com.ramsy.slidingtiles.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ramsy.slidingtiles.R;

public class Cookie extends android.support.v7.widget.AppCompatImageView{
    Bitmap image;
    public Cookie(Context context){
        super(context);
        this.image = BitmapFactory.decodeResource(getResources(), R.drawable.cookie);
        this.setImageBitmap(this.image);
    }
}
