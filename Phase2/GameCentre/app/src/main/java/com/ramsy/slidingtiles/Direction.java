package com.ramsy.slidingtiles;

/**
 * An enum that represents one of 4 directions.
 */

enum Direction {
    LEFT,
    RIGHT,
    ABOVE,
    BELOW;


    /**
     * Checks if self is a horizontal direction (LEFT or RIGHT)
     * @return true if self is horizontal, and false if vertical (ABOVE or BELOW)
     */

    boolean isHorizontal() {
        return (this == LEFT || this == RIGHT);
    }


    /**
     * Creates a new instance that is the reverse of self along its plane.
     * RIGHT is returned if self was LEFT, and vice versa. ABOVE is returned if self was BELOW, and vice versa.
     * @return a new Direction that is the reverse of self along its plane.
     */

    Direction reverse() {
        if (this == LEFT) {
            return RIGHT;
        } else if (this == RIGHT) {
            return LEFT;
        } else if (this == ABOVE) {
            return BELOW;
        } else {
            return ABOVE;
        }
    }
}
