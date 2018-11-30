package com.ramsy.GameCentre.GameCentreCommon;

public class LeaderBoardFeedTheNanu extends LeaderBoardGeneralGame {
    int numberTabs = 1;

    public String currentTabForDatabase(int position) {
        return "FeedTheNanu";

    }

    public String displayLeaderBoard(int position) {
        return "LeaderBoard for FeedTheNanu";
    }
}
