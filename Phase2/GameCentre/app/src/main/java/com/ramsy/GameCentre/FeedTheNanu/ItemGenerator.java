package com.ramsy.GameCentre.FeedTheNanu;

import android.content.Context;
import android.widget.ImageView;
import com.ramsy.GameCentre.FeedTheNanu.DropItems.*;
import java.util.Random;

/**
 * A class 
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
