package com.ramsy.slidingtiles;

public class MemoryMaxtrixGame {
    private int undo = 5;
    private int hp = 5;

    public MemoryMaxtrixGame(){

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
