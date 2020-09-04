package com.ramsy.GameCentre.SlidingTiles;

import android.view.View;


/**
 * A class that represents a single move in the game.
 * Namely, the view that was moved, and the direction it was moved.
 */

public class Move {

    /**
     * The view that was moved
     */

    public View v;

    /**
     * The direction the view was moved
     */

    public Direction direction;

    Move(View v, Direction direction) {
        this.v = v;
        this.direction = direction;
    }

    /**
     * Reverses the direction of the move
     */

    public void reverse() {
        this.direction = this.direction.reverse();
    }


}
