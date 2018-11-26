package com.ramsy.GameCentre.FeedTheNanu;


/**
 * An interface that the delegate of the Nanu can conform to,
 * in order receive information about events.
 */

interface NanuDelegate {

    /**
     * @param amount The amount life changed by
     */

    void lifeDidChangeBy(int amount);

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
