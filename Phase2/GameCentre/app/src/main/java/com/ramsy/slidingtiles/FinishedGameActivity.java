package com.ramsy.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

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
        TextView scoreText = findViewById(R.id.score);
        scoreText.setText(gameScore);
        User meUser = FirebaseFuncts.getUser();
        meUser.addScore(gameName, gameScore);
        //scoreText.setText("9000");

        Button button = findViewById(R.id.leaderboardbutton);

        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                Intent pullLeaderBoard = new Intent (v.getContext(), LeaderBoardActivity.class);
                pullLeaderBoard.putExtra("lastScore", gameScore);
                pullLeaderBoard.putExtra("lastGame", gameName);
                startActivity(pullLeaderBoard);
            }
        });
    }
}
