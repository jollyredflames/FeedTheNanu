package com.ramsy.GameCentre.FeedTheNanu;

import android.content.Context;
import android.widget.ImageView;
import com.ramsy.GameCentre.FeedTheNanu.DropItems.*;
import java.util.Random;

/**
 * A class randomly return an item to drop Edible item from the top of the screen such that the
 * Nanu creature can be fed.
 */
public class ItemGenerator {
    //        set up drop so that items have a certain probability of dropping
    String[] drop = new String[]{
            "candy", "candy",
            "coffee",
            "cupcake", "cupcake",
            "spider", "donut", "donut",
            "spider", "spider", "spider"
    };

    Context context;

    ItemGenerator(Context context){
        this.context = context;
    }

    /**
     * A function choose a random edible item and return it.
     * @return a class representing the Edible item that is going to fall from the screen
     */
    public ImageView getItem() {
        Random ran = new Random();
        int i = ran.nextInt(drop.length);

        if (drop[i].equals("candy")) {
            return new Candy(this.context);
        }
        else if (drop[i].equals("coffee")) {
            return new Coffee(this.context);
        }
        else if (drop[i].equals("cupcake")) {
            return new Cupcake(this.context);
        }
        else if (drop[i].equals("donut")) {
            return new Donut(this.context);
        }
        else {
            return new Spider(this.context);
        }

    }

}
