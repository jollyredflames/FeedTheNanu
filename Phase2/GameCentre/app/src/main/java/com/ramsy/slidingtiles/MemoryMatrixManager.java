package com.ramsy.slidingtiles;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryMatrixManager implements View.OnClickListener {
    private RelativeLayout container;
    private Set<Integer> clickableID = new HashSet<>();
    private Set<Integer> mustBeClicked;
    private static int no =1;
    private static int numToBeClicked;
    private int undo = 5;
    private int hp = 5;
    private int currentClicked;
    private int correctClick = 0;
    private int resetDelay = 5000;
    private boolean canClick = false;
    private Set<Integer> correctClicks = new HashSet<>();

    public MemoryMatrixManager(RelativeLayout container, Set<Integer> clickableID,int numTileX,int numTileY,int badIndex) {
        this.container = container;
        this.clickableID = clickableID;
        this.numToBeClicked = (int) Math.ceil(numTileX*numTileY*0.25);
        MemoryMatrixRandomizer randomer = new MemoryMatrixRandomizer(clickableID,numToBeClicked,badIndex);
        mustBeClicked = randomer.randomizer();
        this.go();
        this.resetColor();
    }

    public boolean isComplete(){
        return currentClicked == mustBeClicked.size();
    }

    @Override
    public void onClick(View v) {
        checkTileCorrect(v);
    }

    public void go() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Integer i :mustBeClicked) {
                    container.getChildAt(i+2).setBackgroundColor(Color.YELLOW);
                }
                t.cancel();
            }
        }, 1000, 1000);
    }

    public void resetColor() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Integer i :mustBeClicked) {
                    container.getChildAt(i+2).setBackgroundColor(Color.GRAY);
                }
                t.cancel();
                canClick = true;
            }
        }, resetDelay, 1000);
    }

    public void checkTileCorrect(View v) {
        if (mustBeClicked.contains(v.getId())) {
            if (!correctClicks.contains(v.getId())) {
                v.setBackgroundColor(Color.GREEN);
                correctClick++;
            }
        }
        else {
            v.setBackgroundColor(Color.RED);
        }
    }
    public void loseHP(){
        hp--;
    }
    public void gainHP(){
        hp++;
    }

    public void clickedUndo(){
        if (undo > 0){
            this.gainHP();
        }
    }
    public int calculateScore(int prevScore){
        return 0;
    }

    public int calculateScore(){
        return 0;
    }

    public boolean isGameComplete () {
        return correctClick == mustBeClicked.size();
    }

    public boolean getClick () {
        return canClick;
    }
}
