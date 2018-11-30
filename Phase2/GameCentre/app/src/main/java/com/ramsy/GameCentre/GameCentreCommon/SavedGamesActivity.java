package com.ramsy.GameCentre.GameCentreCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.SaveState;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.FeedTheNanu.MainActivity;
import com.ramsy.GameCentre.MemoryMatrix.ChooseMemoryMatrixGameType;
import com.ramsy.GameCentre.MemoryMatrix.MemoryMatrixActivity;
import com.ramsy.GameCentre.MemoryMatrix.MemoryMatrixMovingActivity;
import com.ramsy.GameCentre.R;
import com.ramsy.GameCentre.SlidingTiles.SlidingTilesGameActivity;
import com.ramsy.GameCentre.SlidingTiles.SlidingTilesSizeActivity;

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
    String gameName;
    Intent newActivity;
    int correctSlot;

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

        // Get game name from the intent
        Intent currentIntent = getIntent();
        this.gameName = currentIntent.getStringExtra("GAME_NAME");

        //get the gamelist from meuser using the GAME_NAME passed in
        ss = meUser.getSavedGamesForGameName(gameName);
        tv.setText("Select Game");
        setSlot();


        // set up the Intent to avoud all the duplicated code
        if (gameName == null) {
            System.out.println("XXX OMG game name was null");
        }
        else if(gameName.equals("SlidingTiles")){
            this.correctSlot = meUser.correctSlot("SlidingTiles");
            this.newActivity = new Intent(this, SlidingTilesGameActivity.class);
        }
        else if(gameName.equals("FeedTheNanu")){
            this.correctSlot = meUser.correctSlot("FeedTheNanu");
            this.newActivity = new Intent(this, MainActivity.class);
        }
        else {
            // TODO: Change this to the Memory Matrix game
            this.correctSlot = meUser.correctSlot("MemoryMatrix");
            this.newActivity = new Intent(this, MainActivity.class);
        }

        this.newActivity.putExtra("slot", this.correctSlot);
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
            newActivity.putExtra("slot", 0);
            startActivity(newActivity);

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
            newActivity.putExtra("slot", 1);
            startActivity(newActivity);

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
            newActivity.putExtra("slot", 2);
            startActivity(newActivity);
        });
    }

    /**
     * Detect if the delete button is clicked, if clicked, delete the game in slot 1
     */

    private void setupDelete1Listener() {
        delete1.setOnClickListener((V) -> {
            meUser.deleteGame(gameName,0);
            ss = meUser.getSavedGamesForGameName(gameName);
            setSlot();
        });
    }

    /**
     * Detect if the delete button is clicked, if clicked, delete the game in slot 2
     */

    private void setupDelete2Listener() {
        delete2.setOnClickListener((V) -> {
            meUser.deleteGame(gameName, 1);
            ss = meUser.getSavedGamesForGameName(gameName);
            setSlot();

        });
    }

    /**
     * Detect if the delete button is clicked, if clicked, delete the game in slot 3
     */

    private void setupDelete3Listener() {
        delete3.setOnClickListener((V) -> {
            meUser.deleteGame(gameName,2);
            ss = meUser.getSavedGamesForGameName(gameName);
            setSlot();
        });
    }

    /**
     * Set up the look of all buttons, put non-exist slot buttons invisible, set up
     * text message on buttons, and set listeners to the slots with games in it.
     * */

private void setSlot(){
        if (meUser.getTheNumberSaved(gameName) == 1){
            slot1.setText(ss.get(0).localTime);
            slot2.setAlpha(0f);
            slot3.setAlpha(0f);
            delete2.setAlpha(0f);
            delete3.setAlpha(0f);
            setupSlot1Listener();
            setupDelete1Listener();
        }
        else if(meUser.getTheNumberSaved(gameName) == 2){
            slot1.setText(ss.get(0).localTime);
            slot2.setText(ss.get(1).localTime);
            slot3.setAlpha(0f);
            delete3.setAlpha(0f);
            setupSlot1Listener();
            setupSlot2Listener();
            setupDelete1Listener();
            setupDelete2Listener();

        }
        else if(meUser.getTheNumberSaved(gameName) == 3){
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




