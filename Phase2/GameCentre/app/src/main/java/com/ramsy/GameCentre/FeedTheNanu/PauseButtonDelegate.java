package com.ramsy.GameCentre.FeedTheNanu;


/**
 * An interface that the delegate of a PauseButton can conform to,
 * in order receive information about events.
 */

interface PauseButtonDelegate {

    /**
     * Called whenever the PauseButton has been tapped.
     * @param paused true if the button has just been set to paused, false if the button has just been set to resumed.
     */

    void pauseButtonWasTapped(boolean paused);

}
