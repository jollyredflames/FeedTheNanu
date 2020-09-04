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

public class MemoryMatrixMovingActivity extends Activity implements View.OnClickListener {
    private DisplayMetrics displayMetrics;
    int screenHeight;
    int screenWidth;
    int resetDelay = 4000;
    RelativeLayout container;
    int dim = 150;
    Handler handler = new Handler();
    ArrayList<Block> blocks = new ArrayList<>();
    int numBalls = 5;
    Set<Integer> clickableID = new HashSet<>();
    MemoryMatrixManager manager;
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
        container = new RelativeLayout(this);
        setContentView(container);
        Button undo = new Button(this);
        undo.setOnClickListener(this);
        undo.setText("Undo");
        undo.setBackgroundColor(Color.GREEN);
        undo.setId(-2);
        container.addView(undo);
        RelativeLayout.LayoutParams other = new RelativeLayout.LayoutParams(displayMetrics.widthPixels/3, 175);
        other.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        undo.setLayoutParams(other);
        TextView gameInfo = new TextView(this);
        gameInfo.setText("Game Info");
        gameInfo.setBackgroundColor(Color.WHITE);
        gameInfo.setId(-999);
        container.addView(gameInfo);
        RelativeLayout.LayoutParams other1 = new RelativeLayout.LayoutParams(displayMetrics.widthPixels/3, 175);
        other1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        other1.addRule(RelativeLayout.RIGHT_OF, -2);
        gameInfo.setLayoutParams(other1);
        Button quit = new Button(this);
        quit.setText("Quit");
        quit.setId(-500);
        quit.setBackgroundColor(Color.GREEN);
        quit.setOnClickListener(this);
        container.addView(quit);
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
            container.addView(ballView);
            RelativeLayout.LayoutParams other11 = new RelativeLayout.LayoutParams(dim, dim);
            ballView.setLayoutParams(other11);
            Block block1 = new Block(i+3, 500, 500, dim, dim);
            blocks.add(block1);
        }
        manager = new MemoryMatrixManager(blocks,clickableID,0,life,numUndo,slot,score,blocks.size(),0);
        collisionDetecter = new CollisionDetecter(screenWidth,screenHeight);
        Button undoer = (Button) container.getChildAt(0);
        undoer.setText("UNDO: "+String.valueOf(manager.getNumUndo())+" LEFT");
        TextView info = (TextView) container.getChildAt(1);
        LeaderBoardModel.generateTextViewDesign(info,"LIVES LEFT: "+ manager.getLife());
        go();
        resetColor();
        beginMoving();
    }

    /**
     * this handler will constantly be running to update the view objects on the screen
     */
    public void beginMoving(){
        Runnable update = new Runnable() {
            @Override
            public void run() {
                for(Block first: manager){
                    for(Block second: manager){
                        collisionDetecter.detectCollision(first,second);
                        View v = container.getChildAt(first.getId());
                        shift(v,first.getX(),first.getY());
                    }
                }
                handler.postDelayed(this, 1);
            }
        };
        handler.post(update);
    }

    /**
     *
     * @param v view object to be updated with a new x and y
     * @param x the new x value for the view
     * @param y the new y value for the view
     */
    public void shift(View v, int x, int y) {
        v.setX(x);
        v.setY(y);
    }

    /**
     * set up the screen height and width and how big/small the view object should be
     */
    private void setInstanceVariables() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        //1080 x 1794
    }

    /**
     *
     * @param v the view that was just clicked by the user
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == -500) {
            Intent pullChooseGameActivity = new Intent (this, ChooseGame.class);
            startActivity(pullChooseGameActivity);
            return;
        }
        if (v.getId() == -2) {
            if (manager.setUpUndo()) {
                container.getChildAt(manager.performUndo()).setBackgroundColor(Color.GRAY);
                Button undoer = (Button) container.getChildAt(0);
                undoer.setText("UNDO: "+String.valueOf(manager.getNumUndo())+" LEFT");
                TextView info = (TextView) container.getChildAt(1);
                LeaderBoardModel.generateTextViewDesign(info,"LIVES LEFT: "+ manager.getLife());
            }
            return;
        }
        if (!manager.getClick()) {
            return;
        }
        if(manager.checkTileCorrect(v.getId())){
            v.setBackgroundColor(Color.GREEN);
            if (manager.isGameComplete()) {
                manager.setX(numBalls+1);
                manager.save();
                manager.calculateScore();
                manager.save();
                Intent newGame = new Intent(this,MemoryMatrixMovingActivity.class);
                int slot = manager.getSlot();
                newGame.putExtra("slot",slot);
                startActivity(newGame);
            }
            return;
        }
        else{
            v.setBackgroundColor(Color.RED);
            TextView info = (TextView) container.getChildAt(1);
            info.setText("LIVES LEFT: "+ manager.getLife());
            if (manager.gameOver()){
                meUser.deleteGame("MemoryMatrix", manager.getSlot());
                Intent finishedGame = new Intent(this,FinishedGameActivity.class);
                finishedGame.putExtra("gameIdentifier","MemoryMatrix");
                finishedGame.putExtra("gameScore", manager.getScore());
                finishedGame.putExtra("gameName","Hard");
                startActivity(finishedGame);
            }
        }
    }

    /**
     *
     *  set the views that should be clicked to yellow
     *
     */
    public void go() {
        MemoryGameCommon.go(container, manager.getMustBeClicked(), manager,2);

    }

    /**
     *  when the reset timer is up make those text views gray so they are hidden
     */
    public void resetColor() {
        MemoryGameCommon.resetColor(container, manager,resetDelay,2);
    }

    /**
     * when the user clicks on the back button take them to choose game
     */
    @Override
    public void onBackPressed() {
        manager.save();
        Intent goToChoose = new Intent(this, ChooseGame.class);
        startActivity(goToChoose);
    }
}
