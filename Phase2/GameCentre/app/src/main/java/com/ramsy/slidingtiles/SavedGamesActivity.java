package com.ramsy.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

public class SavedGamesActivity extends AppCompatActivity {

    Button slot1;
    Button slot2;
    Button slot3;
    Button delete1;
    Button delete2;
    Button delete3;
    ArrayList<SaveState> ss;
    TextView tv;
    User meUser;
    Button[] group= new Button[3];

    /**
     * defined all buttons and set the screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);
        meUser = FirebaseFuncts.getUser();
        slot1 = findViewById(R.id.slot1);
        slot2 = findViewById(R.id.slot2);
        slot3 = findViewById(R.id.slot3);
        delete1 = findViewById(R.id.delete1);
        delete2 = findViewById(R.id.delete2);
        delete3 = findViewById(R.id.delete3);
        group[0]=slot1;
        group[1]=slot2;
        group[2]=slot3;

        tv = findViewById(R.id.textView3);
        ss = meUser.getSavedGames();
        tv.setText("Select Game");
        setSlot();

    }

    /**
     * Detect if slot 1 is clicked, if clicked, bring the user to the game saved i slot 1.
     */

    private void setupSlot1Listener() {
        slot1.setOnClickListener((V) -> {
            for (Button each : group) {
                each.setBackgroundColor(getColor(R.color.app_theme));
            }
            slot1.setBackgroundColor(getColor(R.color.app_button));
            Intent tmp = new Intent(this, MemoryGameActivity.class);
            tmp.putExtra("slot", 0);
            startActivity(tmp);

        });
    }

    /**
     * Detect if slot2 is clicked, if clicked, bring the user to the game saved in slot 2
     */

    private void setupSlot2Listener() {
        slot2.setOnClickListener((V) -> {
            for (Button each : group) {
                each.setBackgroundColor(getColor(R.color.app_theme));
            }
            slot2.setBackgroundColor(getColor(R.color.app_button));
            Intent tmp = new Intent(this, MemoryGameActivity.class);
            tmp.putExtra("slot", 1);
            startActivity(tmp);

        });
    }

    /**
     * Detect if slot 2 is clicked, brig the user to the game saved i slot 3
     */

    private void setupSlot3Listener() {
        slot3.setOnClickListener((V) -> {
            for (Button each : group) {
                each.setBackgroundColor(getColor(R.color.app_theme));
            }
            slot3.setBackgroundColor(getColor(R.color.app_button));
            Intent tmp = new Intent(this, MemoryGameActivity.class);
            tmp.putExtra("slot", 2);
            startActivity(tmp);
        });
    }

    /**
     * Detect if the delete button is clicked, if clicked, delete the game in slot 1
     */

    private void setupDelete1Listener() {
        delete1.setOnClickListener((V) -> {
            meUser.deleteGame(0);
            ss = meUser.getSavedGames();
            setSlot();
        });
    }

    /**
     * Detect if the delete button is clicked, if clicked, delete the game in slot 2
     */

    private void setupDelete2Listener() {
        delete2.setOnClickListener((V) -> {
            meUser.deleteGame(1);
            ss = meUser.getSavedGames();
            setSlot();

        });
    }

    /**
     * Detect if the delete button is clicked, if clicked, delete the game in slot 3
     */

    private void setupDelete3Listener() {
        delete3.setOnClickListener((V) -> {
            meUser.deleteGame(2);
            ss = meUser.getSavedGames();
            setSlot();
        });
    }

    /**
     * Set up the look of all buttons, put non-exist slot buttons invisible, set up
     * text message on buttons, and set listeners to the slots with games in it.
     * */

private void setSlot(){
        if (meUser.getTheNumberSaved() == 1){
            slot1.setText(ss.get(0).localTime);
            slot2.setAlpha(0f);
            slot3.setAlpha(0f);
            delete2.setAlpha(0f);
            delete3.setAlpha(0f);
            setupSlot1Listener();
            setupDelete1Listener();
        }
        else if(meUser.getTheNumberSaved() == 2){
            slot1.setText(ss.get(0).localTime);
            slot2.setText(ss.get(1).localTime);
            slot3.setAlpha(0f);
            delete3.setAlpha(0f);
            setupSlot1Listener();
            setupSlot2Listener();
            setupDelete1Listener();
            setupDelete2Listener();

        }
        else if(meUser.getTheNumberSaved() == 3){
            slot1.setText(ss.get(0).localTime);
            slot2.setText(ss.get(1).localTime);
            slot3.setText(ss.get(2).localTime);
            setupSlot1Listener();
            setupSlot2Listener();
            setupSlot3Listener();
            setupDelete1Listener();
            setupDelete2Listener();
            setupDelete3Listener();
        }

        else{
            tv.setText("No Saved Games");
            slot1.setAlpha(0f);
            slot2.setAlpha(0f);
            slot3.setAlpha(0f);
            delete1.setAlpha(0f);
            delete2.setAlpha(0f);
            delete3.setAlpha(0f);
        }
    }

}




