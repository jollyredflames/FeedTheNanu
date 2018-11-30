package com.ramsy.GameCentre.GameCentreCommon;

/**
 * Leader board for memory matrix game
 */
public class LeaderBoardMemoryMatrix extends LeaderBoardGeneralGame {
    int numberTabs = 2;

    /**
     *
     * @param position the current tab the user is on
     * @return a string of what to ask the database for
     */
    public String currentTabForDatabase(int position){
        if(position == 0){
            return "Easy";
        }
        else{
            return "Hard";
        }

    }

    /**
     *
     * @param position the tab that the current user is on
     * @return  the name of the leader board based on the current tab
     */
    public String displayLeaderBoard(int position){
        if(position == 0){
            return "LeaderBoard for Easy MemoryMatrix";
        }
        else {
            return "LeaderBoard for Hard MemoryMatrix";
        }
    }
}
