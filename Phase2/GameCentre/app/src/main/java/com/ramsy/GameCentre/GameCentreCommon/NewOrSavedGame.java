package com.ramsy.GameCentre.GameCentreCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.FeedTheNanu.MainActivity;
import com.ramsy.GameCentre.MemoryMatrix.MemoryMatrixActivity;
import com.ramsy.GameCentre.SlidingTiles.SlidingTilesSizeActivity;
import com.ramsy.GameCentre.R;


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

    /**
     * References to the New Game button and Saved Games button, in that order
     */

    Button[] group;
    String gameName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_page);
        meUser = FirebaseFuncts.getUser();

        //assign gamename to the value that is from an intent
        // value for sliding tiles game is "SlidingTiles"
        // value for feed the nanu is "FeedTheNanu"
        // value for memory tile is "MemoryMatrix"
        Intent currentIntent = getIntent();
        this.gameName = currentIntent.getStringExtra("GAME_NAME");
        Button newGame = findViewById(R.id.newGame);
        Button savedGames = findViewById(R.id.savedGames);
        this.group = new Button[] {newGame, savedGames};
        setupNewGameListener();
        setupSavedGamesListener();
        setupLogOutListener();

    }

//    @Override
//    public void onBackPressed() {
//
//    }

    /**
     * if the user has 3 saved already, it will send them to the load save activity to delete a save
     * otherwise they will continue to the game options activity
     */
    private void setupNewGameListener(){
        group[0].setOnClickListener((V) ->{
            meUser = FirebaseFuncts.getUser();
            if (meUser.getTheNumberSaved(gameName) == 3){

                    Log.e("check", "meuser not null, inner if block reached");
                    NewGameDialog newGameDialog = new NewGameDialog();
                    newGameDialog.show(getSupportFragmentManager(),"over ride saved");
            }
            else {
                group[1].setBackgroundColor(getColor(R.color.app_button1));
                group[0].setBackgroundColor(getColor(R.color.app_button));

                Intent newActivity;


                if (gameName.equals("SlidingTiles")){
                    newActivity = new Intent(this, SlidingTilesSizeActivity.class);

                }
                else if(gameName.equals("FeedTheNanu")){
                    newActivity = new Intent(this, MainActivity.class);
                }
                else {
                    newActivity = new Intent(this, MemoryMatrixActivity.class);
                }
                startActivity(newActivity);

            }


        });
    }

    /**
     * take the user to the saved game activity
     */
    private void setupSavedGamesListener(){
        group[1].setOnClickListener((V) ->{
            group[0].setBackgroundColor(getColor(R.color.app_button1));
            group[1].setBackgroundColor(getColor(R.color.app_button));
//            Intent tmp = new Intent(this, SavedGamesActivity.class);
//            startActivity(tmp);
            //TODO: NEED TO INTENT TO THE CORRECT SAVED GAMES ACTIVITY
        });
    }

    /**
     * log the user out of the game
     */
    private void setupLogOutListener(){
        Button savedGames = findViewById(R.id.LogOutButton);
        savedGames.setOnClickListener((V) ->{
            LoginPage.signUserOut();
            Intent n = new Intent(this, LoginPage.class);
            startActivity(n);
        });
    }

}
