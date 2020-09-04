package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.ramsy.GameCentre.FeedTheNanu.DropItem;
import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.GameCentre.R;

/**
 * A class representing donut extending Drop Item and implementing Edible
 */
public class Donut extends DropItem implements Edible{

    static Bitmap texture;

    public Donut(Context context){
        super(context);

        if (Donut.texture == null) {
            Donut.texture = BitmapFactory.decodeResource(getResources(), R.drawable.donut);
        }
        this.setImageBitmap(Donut.texture);

    }

    /**
     * conforming the Interface Edible
     * @return the effect on the Nanu's life
     */
    @Override
    public int effectOnLife() {
        return 10;
    }

    /**
     * conforming to the Interface Edible
     * @return the effect on the Game score caused by the item
     */
    @Override
    public int effectOnScore() {
        return 100;
    }

    /**
     * conforming to the Interface Edible
     * @return the effect on the Nanu's moving speed caused by this item
     */
    @Override
    public int effectOnSpeed() {
        return 1;
    }
}
