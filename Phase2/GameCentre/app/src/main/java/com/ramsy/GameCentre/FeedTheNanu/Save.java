package com.ramsy.GameCentre.FeedTheNanu;


import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.SaveState;

/**
 * A class encapsulating the necessary information to save a Feed The Nanu game
 */

public class Save extends SaveState {

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getCurrentLife() {
        return currentLife;
    }

    public void setCurrentLife(float currentLife) {
        this.currentLife = currentLife;
    }

    public int score;
    public float currentLife;

    public Save(int score, float currentLife) {
        super();
        this.score = score;
        this.currentLife = currentLife;
    }

}
