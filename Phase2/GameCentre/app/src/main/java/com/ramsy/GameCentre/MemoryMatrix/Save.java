package com.ramsy.GameCentre.MemoryMatrix;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.SaveState;

public class Save extends SaveState {
    private int numX;
    private int numY;
    private String score;
    private int numUndo;
    private int life;
    private boolean difficulty;

    public int getNumX() {
        return numX;
    }

    public void setNumX(int numX) {
        this.numX = numX;
    }

    public int getNumY() {
        return numY;
    }

    public void setNumY(int numY) {
        this.numY = numY;
    }


    public void setScore(String score) {
        this.score = score;
    }

    public int getNumUndo() {
        return numUndo;
    }

    public void setNumUndo(int numUndo) {
        this.numUndo = numUndo;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Save(){
        super();
    }

    public void setDifficulty(boolean difficulty) {
        this.difficulty = difficulty;
    }

    public boolean getDifficulty() {
        return difficulty;
    }
}
