package com.ramsy.GameCentre.DatabaseSavablesAndFuncts;


import com.ramsy.GameCentre.SlidingTiles.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * A class that represents a condensed version of a Sliding Tile game,
 * or a Feed The Nanu game, or a Memory Matrix game,
 * that stores minimal details needed to recreate the games.
 * This is the object that is saved in the database
 */

public class SaveState {

    // General attributes that are saved for all games
    public int score;
    public String localTime; // so saved games can also be identified by when they were saved.


    // Feed The Nanu specific attributes that need saving
    public float currentLife;


    private int numX;
    private int numY;
    private int numUndo;
    private int life;
    private boolean difficulty;


    // Sliding Tiles specific attributes that need saving
    public ArrayList<Point> positionMap;
    public int size; // although size can be determined from the positionMap,
    // this is to allow the 'load game' phase to easily grab the necessary details needed to display saved games.


    /*
    Init for a save for a Sliding Tiles game
     */

    public SaveState(Map<String, Point> positionMap, Point gap, int score, int size) {

        // Merge positionMap and gap into one point array
        ArrayList<Point> points = new ArrayList<>();

        int n = positionMap.size();

        for (int i = 1; i <= n; i += 1) {
            String key = String.valueOf(i);
            Point p = positionMap.get(key);
            points.add(i - 1, p);
        }

        points.add(n, gap);

        this.positionMap = points;

        this.score = score;
        this.size = size;
        this.localTime = LocalDateTime.now().toString();
    }



    /*
    Init for a save for a Feed The Nanu
     */

    public SaveState(int score, float currentLife) {
        this.score = score;
        this.currentLife = currentLife;
        this.localTime = LocalDateTime.now().toString();
    }


    /*
    Init for a save for a Memory Matrix
     */

    public SaveState(boolean difficulty){
        this.difficulty = difficulty;
    }
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


    public void setDifficulty(boolean difficulty) {
        this.difficulty = difficulty;
    }

    public boolean getDifficulty() {
        return difficulty;
    }


    public SaveState(){}

    public ArrayList<Point> getPositionMap() {
        return positionMap;
    }

    public void setPositionMap(ArrayList<Point> positionMap) {
        this.positionMap = positionMap;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }
}
