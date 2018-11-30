package com.ramsy.GameCentre.GameCentreCommon;

public class LeaderBoardSlidingTiles implements LeaderBoardGeneralGame {
    @Override
    public int numberTabs() {
        return 9;
    }

    @Override
    public String currentTabForDatabase(int position) {
        return "SlidingTiles"+gameIdentifier(position);
    }

    @Override
    public String displayLeaderBoard(int position) {
        return "Leader Board for \n"+gameIdentifier(position);
    }

    public String gameIdentifier(int position){
        return String.valueOf(position+2)+"x"+String.valueOf(position+2);
    }
}
