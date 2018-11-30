package com.ramsy.GameCentre.GameCentreCommon;

import org.junit.Test;

import static org.junit.Assert.*;

public class LeaderBoardFactoryTest {
    /**
     * testing LeaderBoatdFactory and make sure that the method getgame return the correct class
     * based on the input string. also test if null is return if the input string is not valid.
     */
    @Test
    public void getGame() {
        String inputOne = "SlidingTiles";
        String inputTwo = "FeedThenanu";
        String inputThree = "MemoryMatrix";
        String inputFour = "BOOBOOBIAH";
        LeaderBoardSlidingTiles expectedOne = new LeaderBoardSlidingTiles();
        LeaderBoardFeedTheNanu expectedTwo = new LeaderBoardFeedTheNanu();
        LeaderBoardMemoryMatrix expectedThree = new LeaderBoardMemoryMatrix();
        LeaderBoardFactory test = new LeaderBoardFactory();

        assertEquals(expectedOne.getClass(), test.getGame(inputOne).getClass());
        assertEquals(expectedTwo.getClass(), test.getGame(inputTwo).getClass());
        assertEquals(expectedThree.getClass(), test.getGame(inputThree).getClass());
        assertEquals(null, test.getGame(inputFour));

    }

}