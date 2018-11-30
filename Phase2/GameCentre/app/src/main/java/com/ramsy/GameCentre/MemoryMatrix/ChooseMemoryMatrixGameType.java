package com.ramsy.GameCentre.MemoryMatrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ramsy.GameCentre.R;

public class ChooseMemoryMatrixGameType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_memory_matrix_game_type);
    }

    public void onMovingClicked(View view){
        Intent playMovingGame = new Intent(this, MemoryMatrixMovingActivity.class);
        startActivity(playMovingGame);

    }

    public void onNonMovingClicked(View view){
        Intent playNonMovingGame = new Intent(this, MemoryMatrixActivity.class);
        startActivity(playNonMovingGame);
    }
}
