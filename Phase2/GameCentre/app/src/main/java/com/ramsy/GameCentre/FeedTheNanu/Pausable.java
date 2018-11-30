package com.ramsy.GameCentre.FeedTheNanu;


/**
 * A Pausable interface with a pause and resume method.
 * Classes conform to the Pausable interface by implementing these methods.
 * So for pausing falling food items, we don't even need to have the client grab the animator and pause / resume it.
 * That way we can have the Nanu use it too, and it will implement it differently as it has no animator object.
 * So we can pause everything in the hierarchy in one fell swoop.
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
