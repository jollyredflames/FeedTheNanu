package com.ramsy.GameCentre.SlidingTiles;

/**
 *  An interface that the delegate of the sldingtiles can conform to,
 *   in order receive information about events.
 */
public interface SlidingTileGameDelegate {
    void scoreDidChange(int newScore);
    void didComplete();
}
