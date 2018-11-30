package com.ramsy.GameCentre.GameCentreCommon;

public class LeaderBoardMemoryMatrix implements LeaderBoardGeneralGame {
    @Override
    public int numberTabs(){
        return 2;
    }
    public String currentTabForDatabase(int position){
        if(position == 0){
            return "MemoryMatrixEasy";
        }
        else{
            return "MemoryMatrixHard";
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
