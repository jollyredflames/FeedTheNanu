package com.ramsy.GameCentre.GameCentreCommon;

/**
 * Leader board for the sliding tiles game
 */
public class LeaderBoardSlidingTiles extends LeaderBoardGeneralGame {
    public int numberTabs = 9;

    /**
     *
     * @param position the tab that the current user is on
     * @return the name of the leader board based on the current position
     */
    @Override
    public String displayLeaderBoard(int position) {
        return "Leader Board for \n"+currentTabForDatabase(position);
    }


    /**
     *
     * @param position the current tab the user is on
     * @return return a string of what leader board to ask the database for
     */
    public String currentTabForDatabase(int position){
        return String.valueOf(position+2)+"x"+String.valueOf(position+2);
    }

}
