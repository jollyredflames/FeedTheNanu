package com.ramsy.GameCentre.GameCentreCommon;

public class LeaderBoardMemoryMatrix extends LeaderBoardGeneralGame {
    int numberTabs = 2;
    public String currentTabForDatabase(int position){
        if(position == 0){
            return "Easy";
        }
        else{
            return "Hard";
        }

    }
    public String displayLeaderBoard(int position){
        if(position == 0){
            return "LeaderBoard for Easy MemoryMatrix";
        }
        else {
            return "LeaderBoard for Hard MemoryMatrix";
        }
    }
}
