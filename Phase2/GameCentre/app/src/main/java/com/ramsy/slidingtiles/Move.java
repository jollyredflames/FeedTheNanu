package com.ramsy.slidingtiles;

import android.view.View;


/**
 * A class that represents a single move in the game.
 * Namely, the view that was moved, and the direction it was moved.
 */

class Move {

    /**
     * The view that was moved
     */

    View v;

    /**
     * The direction the view was moved
     */

    Direction direction;

    Move(View v, Direction direction) {
        this.v = v;
        this.direction = direction;
    }

    /**
     * Reverses the direction of the move
     */

    void reverse() {
        this.direction = this.direction.reverse();
    }


}
