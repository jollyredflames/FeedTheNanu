package com.ramsy.GameCentre.MemoryMatrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.R;

public class ChooseMemoryMatrixGameType extends AppCompatActivity {
    private User meUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_memory_matrix_game_type);
        meUser = FirebaseFuncts.getUser();
    }

    public void onMovingClicked(View view){
        Intent playMovingGame = new Intent(this, MemoryMatrixMovingActivity.class);
        playMovingGame.putExtra("X",3);
        playMovingGame.putExtra("Y",3);
        playMovingGame.putExtra("life",5);
        playMovingGame.putExtra("numUndo",5);
        int slot = meUser.correctSlot("MemoryMatrix");
        playMovingGame.putExtra("slot",slot);
        playMovingGame.putExtra("score","0");
        startActivity(playMovingGame);

    }

    public void onNonMovingClicked(View view){
        Intent playNonMovingGame = new Intent(this, MemoryMatrixActivity.class);
        playNonMovingGame.putExtra("numBlocks",3);
        playNonMovingGame.putExtra("life",5);
        playNonMovingGame.putExtra("numUndo",5);
        int slot = meUser.correctSlot("MemoryMatrix");
        playNonMovingGame.putExtra("slot",slot);
        playNonMovingGame.putExtra("score","0");
        startActivity(playNonMovingGame);
    }
}
