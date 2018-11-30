package com.ramsy.GameCentre.GameCentreCommon;

public interface LeaderBoardGeneralGame {
    int numberTabs();
    String currentTabForDatabase(int position);
    String displayLeaderBoard(int position);
}

