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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
public class MemoryMatrixActivity extends Activity implements View.OnClickListener {
    int resetDelay = 5000;
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
    private MemoryMatrixManager person;
    private User meUser = FirebaseFuncts.getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        int slot = bundle.getInt("slot");
        SaveState thisSave = meUser.getGame("MemoryMatrix", slot);
        int life;
        int numUndo;
        int score;
        if (thisSave == null){
            numTileX = 3;
            numTileY = 3;
            life = 5;
            numUndo = 5;
            score = 0;
        } else{
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
        undo.setText("Undo");
        undo.setBackgroundColor(Color.YELLOW);
        undo.setId(-2);
        container.addView(undo);
        RelativeLayout.LayoutParams other = new RelativeLayout.LayoutParams(200, 100);
        other.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        undo.setLayoutParams(other);
        Button gameInfo = new Button(this);
        gameInfo.setText("Game Info");
        gameInfo.setBackgroundColor(Color.BLUE);
        gameInfo.setId(-999);
        container.addView(gameInfo);
        RelativeLayout.LayoutParams other1 = new RelativeLayout.LayoutParams(200, 100);
        other1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        other1.addRule(RelativeLayout.RIGHT_OF, -2);
        gameInfo.setLayoutParams(other1);
        Button quit = new Button(this);
        quit.setText("Quit");
        quit.setId(-500);
        quit.setBackgroundColor(Color.GREEN);
        quit.setOnClickListener(this);
        container.addView(quit);
        RelativeLayout.LayoutParams other2 = new RelativeLayout.LayoutParams(200, 100);
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
                v1.setBackgroundColor(Color.RED);
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
            v.setBackgroundColor(Color.RED);
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
            clicker.add(new Block(belowID+1));
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
        System.out.println("XXXXXXX");
       badIndex = Collections.min(underID);

        System.out.println("yuh");
        person = new MemoryMatrixManager(clicker,clickable, badIndex,life,numUndo,slot,score,numTileX,numTileX);
        go();
        resetColor();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == -500) {
            //person.se
            // tUpQuit();
//            Intent pullChooseGameActivity = new Intent (this, NewOrSavedGame.class);
//            startActivity(pullChooseGameActivity);
//            return;
        }
        if (v.getId() == -2) {
            if (person.setUpUndo()) {
                container.getChildAt(person.performUndo() + 2).setBackgroundColor(Color.GRAY);
            }
            return;
        }
        if (!person.getClick()) {
            return;
        }
        if(person.checkTileCorrect(v.getId())){
            v.setBackgroundColor(Color.GREEN);
            if (person.isGameComplete()) {
                Intent newGame = new Intent(this,MemoryMatrixActivity.class);
                if(numTileY == numTileX){
                    person.setX(numTileX+1);
                }
                else{
                    person.setY(numTileY+1);
                }
                person.save();
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
                meUser.deleteGame("MemoryMatrix",person.getSlot());
                Intent finishedGame = new Intent(this,FinishedGameActivity.class);
                finishedGame.putExtra("gameIdentifier","MemoryMatrix");
                finishedGame.putExtra("gameScore","100");
                finishedGame.putExtra("gameName","Easy");
                startActivity(finishedGame);
            }
        }
    }

    private void setInstanceVariables() {
        container = new RelativeLayout(this);
        double heightRatio = 0.80;
        double widthRatio = 0.90;
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        tileHeight = (int) Math.floor(((displayMetrics.heightPixels) * heightRatio) / (numTileY)) - 3;
        tileWidth = (int) Math.ceil((displayMetrics.widthPixels * widthRatio) / (numTileX));
        vertSpacerWidth = (int) Math.ceil((displayMetrics.widthPixels * (1 - widthRatio)) / (numTileX - 1));
        horSpacerHeight = (int) Math.floor((displayMetrics.heightPixels * (1 - heightRatio)) / (numTileY - 1));
        horSpacerWidth = displayMetrics.heightPixels;
        //1080 x 1794
    }

    public void go() {
        Set<Integer> clickers = person.getMustBeClicked();
        for (Block item : person) {
            if (clickers.contains(item.getId())) {
                container.getChildAt(item.getId() + 2).setBackgroundColor(Color.YELLOW);
            }
        }

    }

    public void resetColor() {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Block item : person) {
                    container.getChildAt(item.getId() + 2).setBackgroundColor(Color.GRAY);
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