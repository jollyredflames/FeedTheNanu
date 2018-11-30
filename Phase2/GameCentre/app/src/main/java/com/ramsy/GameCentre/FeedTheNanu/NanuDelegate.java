package com.ramsy.GameCentre.FeedTheNanu;


/**
 * An interface that the delegate of the Nanu can conform to,
 * in order receive information about events.
 */

interface NanuDelegate {


    /**
     * Called when the Nanu consumed a boost item (coffee)
     */

    void didGetBoosted();


    /**
     * Called when the effects of a boost item (coffee) wear off
     */

    void boostWoreOff();



    /**
     * Called when the Nanu's life reaches 0
     */

    void lifeReachedZero();


    /**
     * @param amount The amount the score should change by
     */

    void scoreShouldChangeBy(int amount);

    /**
     * Called when the Nanu is about to eat an item.
     * This is opportunity for the implenting class to perform cleanup on the item,
     * such as removing the View from the interface.
     * @param item The Edible that is about to be eaten.
     */

    void aboutToEat(Edible item);


}
