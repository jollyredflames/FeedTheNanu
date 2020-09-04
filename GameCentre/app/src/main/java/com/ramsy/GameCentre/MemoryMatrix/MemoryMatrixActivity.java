package com.ramsy.GameCentre.MemoryMatrix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.ramsy.GameCentre.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MemoryMatrixActivity extends Activity implements View.OnClickListener {
    int resetDelay = 2000;
    private RelativeLayout container;
    private int tileHeight;
    private int tileWidth;
    private int vertSpacerWidth;
    private int horSpacerWidth;
    private int horSpacerHeight;
    private int numTileX;
    private int numTileY;
    private DisplayMetrics displayMetrics;
    private Set<Integer> underID = new HashSet<>();
    private Set<Integer> rightID = new HashSet<>();
    private Set<Integer> clickable = new HashSet<>();
    private ArrayList<Block> clicker = new ArrayList<>();
    int badIndex;
    private MemoryMatrixManager manager;
    private User meUser = FirebaseFuncts.getUser();

    //NOTE: Views made programitcally resulting in long code. Otherwise short class.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        int slot = bundle.getInt("slot");
        SaveState thisSave = meUser.getGame("MemoryMatrix", slot);
        int life;
        int numUndo;
        int score;
        if (thisSave == null) {
            numTileX = 3;
            numTileY = 3;
            life = 5;
            numUndo = 5;
            score = 0;
        } else {
            life = thisSave.getLife();
            numUndo = thisSave.getNumUndo();
            score = thisSave.getScore();
            numTileX = thisSave.getNumX();
            numTileY = thisSave.getNumY();
        }
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setInstanceVariables();
        setContentView(container);
        Button undo = new Button(this);
        undo.setOnClickListener(this);
        undo.setText("Undo:");
        undo.setBackgroundColor(Color.GREEN);
        undo.setId(-2);
        container.addView(undo);
        RelativeLayout.LayoutParams other = new RelativeLayout.LayoutParams(displayMetrics.widthPixels / 3, 175);
        other.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        undo.setLayoutParams(other);
        TextView gameInfo = new TextView(this);
        gameInfo.setText("Game Info");
        gameInfo.setBackgroundColor(Color.WHITE);
        gameInfo.setId(-999);
        container.addView(gameInfo);
        RelativeLayout.LayoutParams other1 = new RelativeLayout.LayoutParams(displayMetrics.widthPixels / 3, 175);
        other1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        other1.addRule(RelativeLayout.RIGHT_OF, -2);
        gameInfo.setLayoutParams(other1);
        Button quit = new Button(this);
        quit.setText("Quit");
        quit.setId(-500);
        quit.setBackgroundColor(Color.GREEN);
        quit.setOnClickListener(this);
        container.addView(quit);
        RelativeLayout.LayoutParams other2 = new RelativeLayout.LayoutParams(displayMetrics.widthPixels / 3, 175);
        other2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        other2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        quit.setLayoutParams(other2);
        int rightOfCounter = 1;
        int belowOfCounter = -2;
        for (int i = 1; i < 2 * numTileX; i++) {
            Button v = new Button(this);
            v.setOnClickListener(this);
            v.setId(rightOfCounter);
            v.setBackgroundColor(Color.GRAY);
            clickable.add(rightOfCounter);
            container.addView(v);
            clicker.add(new Block(rightOfCounter));
            RelativeLayout.LayoutParams vParam = new RelativeLayout.LayoutParams(tileWidth, tileHeight);
            vParam.addRule(RelativeLayout.RIGHT_OF, rightOfCounter - 1);
            vParam.addRule(RelativeLayout.BELOW, belowOfCounter);
            v.setLayoutParams(vParam);
            rightOfCounter++;
            i++;
            if (i < 2 * numTileX) {
                TextView v1 = new TextView(this);
                v1.setBackgroundColor(getColor(R.color.app_button1));
                v1.setId(rightOfCounter);
                rightID.add(rightOfCounter);
                container.addView(v1);
                RelativeLayout.LayoutParams v1Param = new RelativeLayout.LayoutParams(vertSpacerWidth, displayMetrics.heightPixels);
                v1Param.addRule(RelativeLayout.BELOW, belowOfCounter);
                v1Param.addRule(RelativeLayout.RIGHT_OF, rightOfCounter - 1);
                v1.setLayoutParams(v1Param);
                rightOfCounter++;
            }
        }
        int belowID = 2 * numTileX - 1;
        for (int i = 1; i < numTileY; i++) {
            TextView v = new TextView(this);
            v.setId(belowID + 1);
            underID.add(belowID + 1);
            v.setBackgroundColor(getColor(R.color.app_button1));
            container.addView(v);
            RelativeLayout.LayoutParams vParam = new RelativeLayout.LayoutParams(horSpacerWidth, horSpacerHeight);
            vParam.addRule(RelativeLayout.BELOW, belowID);
            v.setLayoutParams(vParam);
            belowID++;
            Button v1 = new Button(this);
            v1.setOnClickListener(this);
            v1.setId(belowID + 1);
            v1.setBackgroundColor(Color.GRAY);
            container.addView(v1);
            clickable.add(belowID + 1);
            clicker.add(new Block(belowID + 1));
            RelativeLayout.LayoutParams v1Param = new RelativeLayout.LayoutParams(tileWidth, tileHeight);
            v1Param.addRule(RelativeLayout.BELOW, belowID);
            v1.setLayoutParams(v1Param);
            belowID++;
        }
        int viewID = belowID;
        viewID++;
        for (int i : underID) {
            for (int j : rightID) {
                Button v3 = new Button(this);
                v3.setOnClickListener(this);
                v3.setBackgroundColor(Color.GRAY);
                v3.setId(viewID);
                clickable.add(viewID);
                container.addView(v3);
                clicker.add(new Block(viewID));
                RelativeLayout.LayoutParams v3Param = new RelativeLayout.LayoutParams(tileWidth, tileHeight);
                v3Param.addRule(RelativeLayout.BELOW, i);
                v3Param.addRule(RelativeLayout.RIGHT_OF, j);
                v3.setLayoutParams(v3Param);
                viewID++;
            }
        }
        badIndex = Collections.min(underID);
        manager = new MemoryMatrixManager(clicker, clickable, badIndex, life, numUndo, slot, score, numTileX, numTileX);
        Button undoer = (Button) container.getChildAt(0);
        undoer.setText("UNDO: " + String.valueOf(manager.getNumUndo()) + " LEFT");
        TextView info = (TextView) container.getChildAt(1);
        LeaderBoardModel.generateTextViewDesign(info, "LIVES LEFT: " + manager.getLife());
        go();
        resetColor();
    }

    /**
     * If a button is tapped in regular MemoryMatrix, this will handle the appropriate response.
     * @param v the button that was clicked
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == -500) {
            Intent pullChooseGameActivity = new Intent(this, ChooseGame.class);
            startActivity(pullChooseGameActivity);
            return;
        }
        if (v.getId() == -2) {
            if (manager.setUpUndo()) {
                container.getChildAt(manager.performUndo() + 2).setBackgroundColor(Color.GRAY);
                Button undoer = (Button) container.getChildAt(0);
                undoer.setText("UNDO: " + String.valueOf(manager.getNumUndo()) + " LEFT");
                TextView info = (TextView) container.getChildAt(1);
                LeaderBoardModel.generateTextViewDesign(info, "LIVES LEFT: " + manager.getLife());
            }
            return;
        }
        if (!manager.getClick()) {
            return;
        }
        if (manager.checkTileCorrect(v.getId())) {
            v.setBackgroundColor(Color.GREEN);
            if (manager.isGameComplete()) {
                Intent newGame = new Intent(this, MemoryMatrixActivity.class);
                if (numTileY == numTileX) {
                    manager.setX(numTileX + 1);
                } else {
                    manager.setY(numTileY + 1);
                }
                manager.calculateScore();
                manager.save();
                int slot = manager.getSlot();
                newGame.putExtra("slot", slot);
                newGame.putExtra("score", 0);
                startActivity(newGame);
            }
            return;
        } else {
            v.setBackgroundColor(Color.RED);
            TextView info = (TextView) container.getChildAt(1);
            info.setText("LIVES LEFT: " + manager.getLife());
            if (manager.gameOver()) {
                meUser.deleteGame("MemoryMatrix", manager.getSlot());
                Intent finishedGame = new Intent(this, FinishedGameActivity.class);
                finishedGame.putExtra("gameIdentifier", "MemoryMatrix");
                finishedGame.putExtra("gameScore", manager.getScore());
                finishedGame.putExtra("gameName", "Easy");
                startActivity(finishedGame);
            }
        }
    }

    /**
     * Specifies layout attributes of this activity
     * set up the screen height and width and how big/small the view object should be
     */
    private void setInstanceVariables() {
        container = new RelativeLayout(this);
        double heightRatio = 0.80;
        double widthRatio = 0.90;
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        tileHeight = (int) Math.floor(((displayMetrics.heightPixels - 250) * heightRatio) / (numTileY)) - 3;
        tileWidth = (int) Math.ceil((displayMetrics.widthPixels * widthRatio) / (numTileX));
        vertSpacerWidth = (int) Math.ceil((displayMetrics.widthPixels * (1 - widthRatio)) / (numTileX - 1));
        horSpacerHeight = (int) Math.floor(((displayMetrics.heightPixels) * (1 - (heightRatio))) / (numTileY - 1));
        horSpacerWidth = displayMetrics.heightPixels;
    }

    /**
     * set the views that should be clicked to yellow
     */
    public void go() {
        MemoryGameCommon.go(container, manager.getMustBeClicked(), manager,0);
    }

    /**
     * when the reset timer is up make those text views gray so they are hidden
     */
    public void resetColor() {
        MemoryGameCommon.resetColor(container, manager,resetDelay,0);
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