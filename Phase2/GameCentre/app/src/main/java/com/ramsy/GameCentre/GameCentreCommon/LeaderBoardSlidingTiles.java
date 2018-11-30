package com.ramsy.GameCentre.GameCentreCommon;

public class LeaderBoardSlidingTiles extends LeaderBoardGeneralGame {
    public int numberTabs = 9;
    @Override
    public String displayLeaderBoard(int position) {
        return "Leader Board for \n"+currentTabForDatabase(position);
    }

    public String currentTabForDatabase(int position){
        return String.valueOf(position+2)+"x"+String.valueOf(position+2);
    }

}
