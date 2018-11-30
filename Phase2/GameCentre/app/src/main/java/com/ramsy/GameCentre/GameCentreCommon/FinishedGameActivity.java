package com.ramsy.GameCentre.GameCentreCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.R;

/**
 * set the view of the activity for when the user finishes a game
 */
public class FinishedGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_game);
        new FirebaseFuncts(LoginPage.uid);
        Intent intent = getIntent();
        String gameScore = intent.getExtras().getString("gameScore");
        String gameName = intent.getExtras().getString("gameName");
        //String gameIdentifier = intent.getExtras().getString("gameIdentifier");
        TextView scoreText = findViewById(R.id.score);
        scoreText.setText(gameScore);
        User meUser = FirebaseFuncts.getUser();
//        GlobalLeaderBoard glb = FirebaseFuncts.getGlobalLeaderBoard();
        meUser.addScore(gameName, gameScore);
//        meUser = FirebaseFuncts.getUser();
//        Toast.makeText(this, glb.getGameGlobalLeaderBoard(gameName).toString() + meUser.getUsername(), Toast.LENGTH_LONG).show();
        //scoreText.setText("9000");

        Button button = findViewById(R.id.leaderboardbutton);

        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                Intent pullLeaderBoard = new Intent (v.getContext(), LeaderBoardActivity.class);
                pullLeaderBoard.putExtra("lastScore", gameScore);
                pullLeaderBoard.putExtra("lastGame", gameName);
                pullLeaderBoard.putExtra("gameIdentifier", "SlidingTiles");
                startActivity(pullLeaderBoard);
            }
        });
    }
}
