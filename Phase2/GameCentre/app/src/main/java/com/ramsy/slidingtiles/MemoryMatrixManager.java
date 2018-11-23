package com.ramsy.slidingtiles;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryMatrixManager {
    private RelativeLayout container;
    private Set<Integer> clickableID = new HashSet<>();
    private Set<Integer> mustBeClicked;
    private static int no =1;
    private static int numToBeClicked;
    private int undo = 5;
    private int hp = 5;
    private int currentClicked;

    public MemoryMatrixManager(RelativeLayout container, Set<Integer> clickableID,int numTileX,int numTileY,int badIndex) {
        this.container = container;
        this.clickableID = clickableID;
        this.numToBeClicked = (int) Math.ceil(numTileX*numTileY*0.25);
        MemoryMatrixRandomizer randomer = new MemoryMatrixRandomizer(clickableID,numToBeClicked,badIndex);
        mustBeClicked = randomer.randomizer();
        this.go();
    }

    public boolean isComplete(){
        return currentClicked == mustBeClicked.size();
    }

    public void go() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                for(Integer i :mustBeClicked){
                    container.getChildAt(i+2).setBackgroundColor(Color.YELLOW);
                }
            }
        }, 1000, 1000);
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

}
