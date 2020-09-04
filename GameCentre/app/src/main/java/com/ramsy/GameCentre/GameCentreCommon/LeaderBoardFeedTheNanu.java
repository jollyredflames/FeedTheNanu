package com.ramsy.GameCentre.GameCentreCommon;

/**
 * A leader board game for feed the nanu
 */
public class LeaderBoardFeedTheNanu extends LeaderBoardGeneralGame {
    int numberTabs = 1;

    /**
     *
     * @param position the current tab the user is on
     * @return a string of what to ask the user for based on the position
     */
    public String currentTabForDatabase(int position) {
        return "FeedTheNanu";

    }

    /**
     *
     * @param position the tab that the current user is on
     * @return return the name of the leader board for this tab
     */
    public String displayLeaderBoard(int position) {
        return "LeaderBoard for FeedTheNanu";
    }
}
