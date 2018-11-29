package com.ramsy.GameCentre.GameCentreCommon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.ramsy.GameCentre.R;

/**
 * a class that allow users to choose the game they want to play from the
 * three provided, after they log in
 */

public class ChooseGame extends AppCompatActivity {
    Button nanu;
    Button memory;
    Button sliding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
        nanu = findViewById(R.id.nanu);
        memory = findViewById(R.id.memory);
        sliding = findViewById(R.id.sliding);
        setMemoryOnClickListener();
        setNanuOnClickListener();
        setSlidingOnClickListener();


    }

    /**
     * function to set listener to the feed the nanu button, so that once the button is clicked,
     * the newOrSacedGame page would show up, with extra information indicating that a feed the nanu
     * game is selected by the user.
     */
    public void setNanuOnClickListener(){
        this.nanu.setOnClickListener((v)-> {
            Intent tmp = new Intent(this, NewOrSavedGame.class);
            tmp.putExtra("GAME_NAME", "FeedTheNanu");
            startActivity(tmp);

        });
    }

    /**
     * function to set listener to the memory matrix button, so that once the button is clicked,
     * the newOrSacedGame page would show up, with extra information indicating that a memory matrix
     * game is selected by the user.
     */
    public void setMemoryOnClickListener(){
        this.nanu.setOnClickListener((v)-> {
            Intent tmp = new Intent(this, NewOrSavedGame.class);
            tmp.putExtra("GAME_NAME", "MemoryMatrix");
            startActivity(tmp);

        });
    }

    /**
     * function to set listener to the sliding tile button, so that once the button is clicked,
     * the newOrSavedGame page would show up, with extra information indicating that a sliding tile
     * game is selected by the user.
     */
    public void setSlidingOnClickListener(){
        this.nanu.setOnClickListener((v) -> {
            Intent tmp = new Intent(this, NewOrSavedGame.class);
            tmp.putExtra("GAME_NAME", "SlidingTiles");
            startActivity(tmp);

        });
    }
}
