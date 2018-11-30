package com.ramsy.GameCentre.GameCentreCommon;

/**
 * this class is a factory in the factory design pattern
 * and will return an instance of the correct game given you give correct input
 */
public class LeaderBoardFactory {

    /**
     *
     * @param gameName the game type that is to be returned
     * @return a subclass of leaderboard general game based on what the user inputted
     */
    public LeaderBoardGeneralGame getGame(String gameName){
        if(gameName == null){
            return null;
        }
        if (gameName.equals("SlidingTiles")){
            return new LeaderBoardSlidingTiles();
        }
        if (gameName.equals("FeedTheNanu")){
            return new LeaderBoardFeedTheNanu();
        }
        if (gameName.equals("MemoryMatrix")){
            return new LeaderBoardMemoryMatrix();
        }
        return null;
    }
}
