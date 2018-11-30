package com.ramsy.GameCentre.MemoryMatrix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.SaveState;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.GameCentreCommon.ChooseGame;
import com.ramsy.GameCentre.GameCentreCommon.FinishedGameActivity;
import com.ramsy.GameCentre.GameCentreCommon.LeaderBoardModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryMatrixMovingActivity extends Activity implements View.OnClickListener {
    private DisplayMetrics displayMetrics;
    int screenHeight;
    int screenWidth;
    int resetDelay = 5000;
    RelativeLayout test;
    int dim = 150;
    Handler handler = new Handler();
    ArrayList<Block> blocks = new ArrayList<>();
    int numBalls = 5;
    Set<Integer> clickableID = new HashSet<>();
    MemoryMatrixManager person;
    private CollisionDetecter collisionDetecter;
    private User meUser = FirebaseFuncts.getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int slot = getIntent().getExtras().getInt("slot");
        SaveState thisSave = meUser.getGame("MemoryMatrix", slot);
        int life;
        int numUndo;
        int score;
        if (thisSave == null){
            numBalls = 3;
            life = 5;
            numUndo = 5;
            score = 0;
        } else{
            life = thisSave.getLife();
            numUndo = thisSave.getNumUndo();
            score = thisSave.getScore();
            numBalls = thisSave.getNumX();
        }
        setInstanceVariables();
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        test = new RelativeLayout(this);
        setContentView(test);
        Button undo = new Button(this);
        undo.setOnClickListener(this);
        undo.setText("Undo");
        undo.setBackgroundColor(Color.GREEN);
        undo.setId(-2);
        test.addView(undo);
        RelativeLayout.LayoutParams other = new RelativeLayout.LayoutParams(displayMetrics.widthPixels/3, 175);
        other.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        undo.setLayoutParams(other);
        TextView gameInfo = new TextView(this);
        gameInfo.setText("Game Info");
        gameInfo.setBackgroundColor(Color.WHITE);
        gameInfo.setId(-999);
        test.addView(gameInfo);
        RelativeLayout.LayoutParams other1 = new RelativeLayout.LayoutParams(displayMetrics.widthPixels/3, 175);
        other1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        other1.addRule(RelativeLayout.RIGHT_OF, -2);
        gameInfo.setLayoutParams(other1);
        Button quit = new Button(this);
        quit.setText("Quit");
        quit.setId(-500);
        quit.setBackgroundColor(Color.GREEN);
        quit.setOnClickListener(this);
        test.addView(quit);
        RelativeLayout.LayoutParams other2 = new RelativeLayout.LayoutParams(displayMetrics.widthPixels/3, 175);
        other2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        other2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        quit.setLayoutParams(other2);
        for (int i = 0; i < numBalls; i++) {
            View ballView = new View(this);
            ballView.setBackgroundColor(Color.GRAY);
            ballView.setOnClickListener(this);
            ballView.setId(i+3);
            clickableID.add(i+3);
            test.addView(ballView);
            RelativeLayout.LayoutParams other11 = new RelativeLayout.LayoutParams(dim, dim);
            ballView.setLayoutParams(other11);
            Block block1 = new Block(i+3, 500, 500, dim, dim);
            blocks.add(block1);
        }
        person = new MemoryMatrixManager(blocks,clickableID,0,life,numUndo,slot,score,blocks.size(),0);
        collisionDetecter = new CollisionDetecter(screenWidth,screenHeight);
        Button undoer = (Button) test.getChildAt(0);
        undoer.setText("UNDO: "+String.valueOf(person.getNumUndo())+" LEFT");

        TextView info = (TextView) test.getChildAt(1);
        LeaderBoardModel.generateTextViewDesign(info,"LIVES LEFT: "+person.getLife());
        go();
        resetColor();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                for(Block first:person){
                    for(Block second:person){
                        collisionDetecter.detectCollision(first,second);
                        View v = test.getChildAt(first.getId());
                        shift(v,first.getX(),first.getY());
                    }
                }
                handler.postDelayed(this, 1);
            }
        };
        handler.post(update);
    }


    public void shift(View v, int x, int y) {
        v.setX(x);
        v.setY(y);
    }

    private void setInstanceVariables() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        //1080 x 1794
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == -500) {
            Intent pullChooseGameActivity = new Intent (this, ChooseGame.class);
            startActivity(pullChooseGameActivity);
            return;
        }
        if (v.getId() == -2) {
            if (person.setUpUndo()) {
                test.getChildAt(person.performUndo()).setBackgroundColor(Color.GRAY);
                Button undoer = (Button) test.getChildAt(0);
                undoer.setText("UNDO: "+String.valueOf(person.getNumUndo())+" LEFT");
                TextView info = (TextView) test.getChildAt(1);
                LeaderBoardModel.generateTextViewDesign(info,"LIVES LEFT: "+person.getLife());
            }
            return;
        }
        if (!person.getClick()) {
            return;
        }
        if(person.checkTileCorrect(v.getId())){
            v.setBackgroundColor(Color.GREEN);
            if (person.isGameComplete()) {
                person.setX(numBalls+1);
                person.save();
                person.calculateScore();
                person.save();
                Intent newGame = new Intent(this,MemoryMatrixMovingActivity.class);
                int slot = person.getSlot();
                newGame.putExtra("slot",slot);
                startActivity(newGame);
            }
            return;
        }
        else{
            v.setBackgroundColor(Color.RED);
            TextView info = (TextView) test.getChildAt(1);
            info.setText("LIVES LEFT: "+person.getLife());
            if (person.gameOver()){
                meUser.deleteGame("MemoryMatrix",person.getSlot());
                Intent finishedGame = new Intent(this,FinishedGameActivity.class);
                finishedGame.putExtra("gameIdentifier","MemoryMatrix");
                finishedGame.putExtra("gameScore","100");
                finishedGame.putExtra("gameName","Hard");
                startActivity(finishedGame);
            }
        }
    }

    public void go() {
        Set<Integer> clickers = person.getMustBeClicked();
        for (Block item : person) {
            if (clickers.contains(item.getId())) {
                test.getChildAt(item.getId()).setBackgroundColor(Color.YELLOW);
            }
        }

    }

    public void resetColor() {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Block item : person) {
                    test.getChildAt(item.getId()).setBackgroundColor(Color.GRAY);
                }
                person.setCanClick(true);
                t.cancel();
            }
        }, resetDelay, 500);
    }

    @Override
    public void onBackPressed() {
        person.save();
        Intent goToChoose = new Intent(this, ChooseGame.class);
        startActivity(goToChoose);
    }
}
