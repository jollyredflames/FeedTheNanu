package com.ramsy.GameCentre.FeedTheNanu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ramsy.GameCentre.GameCentreCommon.NewOrSavedGame;
import com.ramsy.GameCentre.R;

public class NanuNewOrSavedActivity extends NewOrSavedGame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_page);
//        super(this);

    }
}
