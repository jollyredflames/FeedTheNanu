package com.ramsy.GameCentre.FeedTheNanu;


/**
 * A Pausable interface with a pause and resume method.
 * Classes conform to the Pausable interface by implementing these methods.
 */

interface Pausable {


    /**
     * What to do when told to pause.
     */

    void pause();


    /**
     * What to do when told to resume.
     */

    void resume();
}
