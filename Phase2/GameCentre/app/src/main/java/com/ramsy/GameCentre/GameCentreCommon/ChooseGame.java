package com.ramsy.GameCentre.GameCentreCommon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

import com.ramsy.slidingtiles.R;

public class ChooseGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_game_activity);
        NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(0);
        picker.setMaxValue(2);
        picker.setDisplayedValues( new String[] { "Sliding Tiles", "Save The Nanu", "Memory Matrix" } );
    }
}
