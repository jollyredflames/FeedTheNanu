package com.ramsy.GameCentre.GameCentreCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.FeedTheNanu.MainActivity;
import com.ramsy.GameCentre.MemoryMatrix.ChooseMemoryMatrixGameType;
import com.ramsy.GameCentre.R;
import com.ramsy.GameCentre.SlidingTiles.SlidingTilesSizeActivity;


/**
 * The activity that allow users to choose to start a new game or a save game
 * New Game
 * Saved Games
 * Log Out
 */

public class NewOrSavedGame extends AppCompatActivity {

    /*
    TODO:
    Generalizing NewOrSavedGame activity

    After selecting which of the 3 games to play (Sliding Tiles, Feed The Nanu, or Memory Matrix),
    the user should be taken to an activity that looks just like this one, but with tailored functionality.
    For example, the New Game button should start the game that was chosen in the previous activity.
    Alternatively, we could go to this activity first, and then pick a game after. But it would mean we'd have to show
    all saved games if saved games is tapped, and right now it is set to show only 3. So let's go with the first design,
    which also makes more semantic sense.
     */

    private User meUser;


    Button newGameButton;
    Button savedGamesButton;
    Button logOutButton;

    String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_page);
        meUser = FirebaseFuncts.getUser();

        Intent currentIntent = getIntent();
        this.gameName = currentIntent.getStringExtra("GAME_NAME");

        this.newGameButton = findViewById(R.id.newGame);
        this.savedGamesButton = findViewById(R.id.savedGames);
        this.logOutButton = findViewById(R.id.LogOutButton);

        setupNewGameListener();
        setupSavedGamesListener();
        setupLogOutListener();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Unhighlight all buttons
        savedGamesButton.setBackgroundColor(getColor(R.color.app_button1));
        newGameButton.setBackgroundColor(getColor(R.color.app_button1));
        logOutButton.setBackgroundColor(getColor(R.color.app_button1));
    }

    /**
     * if the user has 3 saved already, it will send them to the load save activity to delete a save
     * otherwise they will continue to the game options activity
     */
    private void setupNewGameListener(){
        this.newGameButton.setOnClickListener((V) ->{

            if (meUser.getTheNumberSaved(gameName) == 3){
                NewGameDialog newGameDialog = new NewGameDialog();
                Bundle importantInfo = new Bundle();
                importantInfo.putString("gameName", gameName);
                newGameDialog.setArguments(importantInfo);
                newGameDialog.show(getSupportFragmentManager(),"over ride saved");
            } else {
                savedGamesButton.setBackgroundColor(getColor(R.color.app_button1));
                newGameButton.setBackgroundColor(getColor(R.color.app_button));

                Intent newActivity;
                if (gameName.equals("SlidingTiles")){
                    newActivity = new Intent(this, SlidingTilesSizeActivity.class);
                }
                else if (gameName.equals("FeedTheNanu")) {
                    int slot = meUser.correctSlot("FeedTheNanu");
                    newActivity = new Intent(this, MainActivity.class);
                    newActivity.putExtra("slot", slot);
                } else {
                    newActivity = new Intent(this, ChooseMemoryMatrixGameType.class);
                }
                startActivity(newActivity);
            }

        });
    }

    /**
     * take the user to the saved game activity
     */
    private void setupSavedGamesListener(){
        savedGamesButton.setOnClickListener((V) ->{
            newGameButton.setBackgroundColor(getColor(R.color.app_button1));
            savedGamesButton.setBackgroundColor(getColor(R.color.app_button));
            Intent tmp = new Intent(this, SavedGamesActivity.class);
            // Put the game name in the intent
            tmp.putExtra("GAME_NAME", gameName);
            startActivity(tmp);
        });
    }

    /**
     * log the user out of the game
     */
    private void setupLogOutListener(){
        logOutButton.setOnClickListener((V) ->{
            LoginPage.signUserOut();
            Intent n = new Intent(this, LoginPage.class);
            startActivity(n);
        });
    }

}
