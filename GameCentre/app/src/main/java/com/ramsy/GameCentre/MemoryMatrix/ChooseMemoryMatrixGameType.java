package com.ramsy.GameCentre.MemoryMatrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.R;

/**
 * take the user to easy or hard memory matrix depending on which button they click on
 */
public class ChooseMemoryMatrixGameType extends AppCompatActivity {
    private User meUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_memory_matrix_game_type);
        meUser = FirebaseFuncts.getUser();
    }

    /**
     *
     * @param view not used
     *             if they clicked the moving version then send them to the moving memory matrix
     */
    public void onMovingClicked(View view){
        Intent playMovingGame = new Intent(this, MemoryMatrixMovingActivity.class);
        int slot = meUser.correctSlot("MemoryMatrix");
        playMovingGame.putExtra("slot",slot);
        startActivity(playMovingGame);
    }

    /**
     *
     * @param view not user
     *             if they clicked the easy memory matrix take them there
     */
    public void onNonMovingClicked(View view){
        Intent playNonMovingGame = new Intent(this, MemoryMatrixActivity.class);
        int slot = meUser.correctSlot("MemoryMatrix");
        playNonMovingGame.putExtra("slot",slot);
        startActivity(playNonMovingGame);
    }
}
