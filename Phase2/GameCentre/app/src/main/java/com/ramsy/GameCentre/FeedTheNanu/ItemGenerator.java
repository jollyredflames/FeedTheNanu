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


    public ImageView chooseItem(Context con) {
        Random ran = new Random();
        int i = ran.nextInt(11);
        System.out.println(i + "lala");
        if(drop[i].equals("candy")){
            return new Candy(con);
        }
        else if (drop[i].equals("coffee")){
            return new Coffee(con);
        }
        else if (drop[i].equals("cup")){
            return new Cupcake(con);
        }
        else if (drop[i].equals("d")){
            return new Donut(con);
        }
        else{
            return new Spider(con);
        }

    }

}
