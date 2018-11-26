package com.ramsy.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class ChooseGameActivity extends AppCompatActivity {
    Context context;
    private User meUser;
    Button[] group;
    //TODO: GAMENAME MUST BE PASSED
    String gameName = "SlidingTiles";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_page);
        meUser = FirebaseFuncts.getUser();
        Button newGame = findViewById(R.id.newGame);
        Button savedGames = findViewById(R.id.savedGames);
        this.group = new Button[] {newGame, savedGames};
        setupNewGameListener();
        setupSavedGamesListener();
        setupLogOutListener();

    }

    @Override
    public void onBackPressed() {

    }

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
            else{group[1].setBackgroundColor(getColor(R.color.app_button1));
                group[0].setBackgroundColor(getColor(R.color.app_button));
                Intent tmp = new Intent(this, ChooseGameSizeActivity.class);
                startActivity(tmp);}


        });
    }

    /**
     * take the user to the saved game activity
     */
    private void setupSavedGamesListener(){
        group[1].setOnClickListener((V) ->{
            group[0].setBackgroundColor(getColor(R.color.app_button1));
            group[1].setBackgroundColor(getColor(R.color.app_button));
            Intent tmp = new Intent(this, SavedGamesActivity.class);
            startActivity(tmp);
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
