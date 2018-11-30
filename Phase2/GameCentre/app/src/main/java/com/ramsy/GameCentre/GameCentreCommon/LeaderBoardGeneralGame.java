package com.ramsy.GameCentre.GameCentreCommon;

/**
 * abstract class for games to be displayed on the leader board
 */
public abstract class LeaderBoardGeneralGame {
    int numberTabs = 9;

    /**
     *
     * @param position the current tab the user is on
     * @return  a string of what game to ask the database for
     */
    String currentTabForDatabase(int position){
        return "";
    }

    /**
     *
     * @param position the tab that the current user is on
     * @return  a string of the what the leader board name is ex "Sliding Tiles 2x2"
     */
    String displayLeaderBoard(int position){
        return "";
    }
}

