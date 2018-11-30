package com.ramsy.GameCentre.GameCentreCommon;

public class LeaderBoardSlidingTiles implements LeaderBoardGeneralGame {
    @Override
    public int numberTabs() {
        return 9;
    }

    @Override
    public String displayLeaderBoard(int position) {
        return "Leader Board for \n"+currentTabForDatabase(position);
    }

    public String currentTabForDatabase(int position){
        return String.valueOf(position+2)+"x"+String.valueOf(position+2);
    }
}
