package com.ramsy.GameCentre.FeedTheNanu.DropItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.ramsy.GameCentre.FeedTheNanu.DropItem;
import com.ramsy.GameCentre.FeedTheNanu.Edible;
import com.ramsy.GameCentre.R;

/**
 * A class representing coffee extending Drop Item and implementing Edible
 */
public class Coffee extends DropItem implements Edible {

    static Bitmap texture;

    public Coffee(Context context){
        super(context);

        if (Coffee.texture == null) {
            Coffee.texture = BitmapFactory.decodeResource(getResources(), R.drawable.coffee);
        }
        this.setImageBitmap(Coffee.texture);


    }

    /**
     * conforming to the Interface Edible
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
        return 200;
    }

    /**
     * conforming to the Interface Edible
     * @return the effect on the Nanu's moving speed caused by this item
     */
    @Override
    public int effectOnSpeed() {
        return 2;
    }
}


