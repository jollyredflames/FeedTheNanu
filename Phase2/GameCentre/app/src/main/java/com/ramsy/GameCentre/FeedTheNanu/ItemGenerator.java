package com.ramsy.GameCentre.FeedTheNanu;

import android.content.Context;
import android.widget.ImageView;
import com.ramsy.GameCentre.FeedTheNanu.DropItems.*;
import java.util.Random;


public class ItemGenerator {
    //        set up drop so that items have a certain probability of dropping
    String[] drop = new String[]{"candy", "candy", "p", "cup", "cup", "p","coffee",
            "d", "d", "p", "d"};

    Context context;

    ItemGenerator(Context context){
        this.context = context;
    }


    public ImageView getItem() {
        Random ran = new Random();
        int i = ran.nextInt(11);
        System.out.println(i + "lala");
        if(drop[i].equals("candy")){
            return new Candy(this.context);
        }
        else if (drop[i].equals("coffee")){
            return new Coffee(this.context);
        }
        else if (drop[i].equals("cup")){
            return new Cupcake(this.context);
        }
        else if (drop[i].equals("d")){
            return new Donut(this.context);
        }
        else{
            return new Spider(this.context);
        }

    }

}
