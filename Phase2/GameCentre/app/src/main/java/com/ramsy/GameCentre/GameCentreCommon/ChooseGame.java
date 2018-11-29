package com.ramsy.GameCentre.GameCentreCommon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.ramsy.GameCentre.R;

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

    public void setNanuOnClickListener(){
        this.nanu.setOnClickListener((v)-> {
            Intent tmp = new Intent(this, NewOrSavedGame.class);
            tmp.putExtra("GAME_NAME", "FeedTheNanu");
            startActivity(tmp);

        });
    }

    public void setMemoryOnClickListener(){
        this.nanu.setOnClickListener((v)-> {
            Intent tmp = new Intent(this, NewOrSavedGame.class);
            tmp.putExtra("GAME_NAME", "MemoryTile");
            startActivity(tmp);

        });
    }

    public void setSlidingOnClickListener(){
        this.nanu.setOnClickListener((v) -> {
            Intent tmp = new Intent(this, NewOrSavedGame.class);
            tmp.putExtra("GAME_NAME", "SlidingTiles");
            startActivity(tmp);

        });
    }
}
