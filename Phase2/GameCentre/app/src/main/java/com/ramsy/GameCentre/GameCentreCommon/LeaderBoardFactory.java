package com.ramsy.GameCentre.GameCentreCommon;

public class LeaderBoardFactory {

    public LeaderBoardGeneralGame getGame(String gameName){
        return new LeaderBoardSlidingTiles();
//        if(gameName == null){
//            return null;
//        }
//        if (gameName.equals("SlidingTiles")){
//            return new LeaderBoardSlidingTiles();
//        }
//        if (gameName.equals("FeedThenanu")){
//            return new LeaderBoardFeedTheNanu();
//        }
//        if (gameName.equals("MemoryMatrix")){
//            return new LeaderBoardMemoryMatrix();
//        }
//        return null;
    }
}
