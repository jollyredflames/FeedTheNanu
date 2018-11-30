package com.ramsy.GameCentre.GameCentreCommon;

import org.junit.Test;

import static org.junit.Assert.*;

public class LeaderBoardFactoryTest {

    @Test
    public void getGame() throws Exception {
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