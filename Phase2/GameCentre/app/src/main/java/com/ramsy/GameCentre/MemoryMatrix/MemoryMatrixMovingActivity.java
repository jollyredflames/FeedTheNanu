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
import android.widget.Toast;

import com.ramsy.GameCentre.GameCentreCommon.FinishedGameActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        numBalls = getIntent().getExtras().getInt("numBlocks");
        int life = getIntent().getExtras().getInt("life");
        int numUndo = getIntent().getExtras().getInt("numUndo");
        int slot = getIntent().getExtras().getInt("slot");
        int score = getIntent().getExtras().getInt("score");
        setInstanceVariables();
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        test = new RelativeLayout(this);
        setContentView(test);
        Button undo = new Button(this);
        undo.setOnClickListener(this);
        undo.setText("Undo");
        undo.setBackgroundColor(Color.YELLOW);
        undo.setId(-2);
        test.addView(undo);
        RelativeLayout.LayoutParams other = new RelativeLayout.LayoutParams(200, 100);
        other.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        undo.setLayoutParams(other);
        Button gameInfo = new Button(this);
        gameInfo.setText("Game Info");
        gameInfo.setBackgroundColor(Color.BLUE);
        gameInfo.setId(-999);
        test.addView(gameInfo);
        RelativeLayout.LayoutParams other1 = new RelativeLayout.LayoutParams(200, 100);
        other1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        other1.addRule(RelativeLayout.RIGHT_OF, -2);
        gameInfo.setLayoutParams(other1);
        Button quit = new Button(this);
        quit.setText("Quit");
        quit.setId(-500);
        quit.setBackgroundColor(Color.GREEN);
        quit.setOnClickListener(this);
        test.addView(quit);
        RelativeLayout.LayoutParams other2 = new RelativeLayout.LayoutParams(200, 100);
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
            Intent pullChooseGameActivity = new Intent (this, MemoryMatrixMovingActivity.class);
            startActivity(pullChooseGameActivity);
            return;
        }
        if (v.getId() == -2) {
            if (person.setUpUndo()) {
                test.getChildAt(person.performUndo()).setBackgroundColor(Color.GRAY);
            }
            return;
        }
        if (!person.getClick()) {
            return;
        }
        if(person.checkTileCorrect(v.getId())){
            v.setBackgroundColor(Color.GREEN);
            if (person.isGameComplete()) {
                Intent newGame = new Intent(this,MemoryMatrixMovingActivity.class);
                newGame.putExtra("numBlocks",blocks.size()+1);
                newGame.putExtra("life",person.getLife());
                newGame.putExtra("numUndo",person.getNumUndo());
                int slot = person.getSlot();
                newGame.putExtra("slot",slot);
                newGame.putExtra("score",0);
                startActivity(newGame);
            }
            return;
        }
        else{
            v.setBackgroundColor(Color.RED);
            if (person.gameOver()){
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
}
