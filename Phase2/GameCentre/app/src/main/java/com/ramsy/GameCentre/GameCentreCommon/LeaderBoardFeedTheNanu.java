package com.ramsy.GameCentre.GameCentreCommon;

public class LeaderBoardFeedTheNanu implements LeaderBoardGeneralGame {
    @Override
    public int numberTabs() {
        return 1;
    }

    public String currentTabForDatabase(int position) {
        return "FeedTheNanu";

    }

    public String displayLeaderBoard(int position) {
        return "LeaderBoard for FeedTheNanu";
    }
}
