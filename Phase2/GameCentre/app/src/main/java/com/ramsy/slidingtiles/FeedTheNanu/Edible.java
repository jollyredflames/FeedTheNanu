package com.ramsy.slidingtiles.FeedTheNanu;


/**
 * An interface that the subclasses of ImageView can conform to,
 * to set their effect on various aspects of the Nanu when they are eaten.
 * So if there are 10 different kinds of items the Nanu can eat,
 * there will be 10 subclasses of ImageView, and each one can tailor their effects.
 */

interface Edible {

    /**
     * How much the Edible instance affects the life by.
     * Return 1 for an item that increments the life (special health increase item)
     * Return -1 for an item that decrements the life (bad item)
     * Return 0 for an item that doesn't effect the life
     * @return the amount this Edible changes the life by, if eaten.
     */

    int effectOnLife();


    /**
     * How much the Edible instance affects the score by.
     * Return 100 for an item that increases the score by 100.
     * Return -100 for an item that decreases the score by 100.
     * Return 0 for an item that doesn't affect the score.
     * @return The amount this edible changes the score by, if eaten.
     */

    int effectOnScore();


    /**
     * How much the Edible instance affects the speed by.
     * Return 2 to double the default speed.
     * Return 0.5 to half the default speed.
     * Return 1 for an item that doesn't affect the speed.
     * Values that are not 1 will have an effect that lasts for a default amount of time,
     * specified in the Nanu class.
     * @return The amount this Edible changes the speed by, if eaten.
     */

    int effectOnSpeed();


}
