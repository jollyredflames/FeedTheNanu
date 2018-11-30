package com.ramsy.GameCentre.DatabaseSavablesAndFuncts;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class UserTest {
    User test;

    UserTest() {
        new FirebaseFuncts("kkNWsAk2jUfqQLsws5bmPZXqkQ72");
        Thread.sleep(200);
        test = FirebaseFuncts.getUser();
    }

    public void resetUser(){
        test.setUsername("testUser");
        test.setEmail("testingtestuser@testedtests.com");
        HashMap<String, ArrayList<String>> myScores = new HashMap<>();
        ArrayList<String> scores = new ArrayList<>();
        scores.add("10214");
        scores.add("10208");
        scores.add("10082");
        myScores.put("2x2", scores);
        test.setMyScores(myScores);
        test.setMeUserID("kkNWsAk2jUfqQLsws5bmPZXqkQ72");
    }

    @Test
    public void getUsername() {
        assertEquals(test.getUsername(), "testUser");
    }

    @Test
    public void setUsername() {
        test.setUsername("UserNameChanged");
        assertEquals(test.getUsername(), "UserNameChanged");
        resetUser();
    }

    @Test
    public void getEmail() {
        assertEquals(test.getEmail(), "testingtestuser@testedtests.com");
    }

    @Test
    public void setEmail() {
        test.setEmail("EmailChanged");
        assertEquals(test.getEmail(), "EmailChanged");
        resetUser();
    }

    @Test
    public void getMyScores() {
        HashMap<String, ArrayList<String>> myScores = new HashMap<>();
        ArrayList<String> scores = new ArrayList<>();
        scores.add("10214");
        scores.add("10208");
        scores.add("10082");
        myScores.put("2x2", scores);
        assertEquals(test.getMyScores(), myScores);
    }

    @Test
    public void setMyScores() {
        HashMap<String, ArrayList<String>> myScores = new HashMap<>();
        ArrayList<String> scores = new ArrayList<>();
        scores.add("10000");
        scores.add("10000");
        scores.add("10000");
        myScores.put("2x2", scores);
        test.setMyScores(myScores);
        assertEquals(test.getMyScores(), myScores);
        resetUser();
    }

    @Test
    public void getMeUserID() {
        assertEquals(test.getMeUserID(), "kkNWsAk2jUfqQLsws5bmPZXqkQ72");
    }

    @Test
    public void setMeUserID() {
        test.setMeUserID("test");
        assertEquals(test.getMeUserID(), "test");
        resetUser();
    }

    @Test
    public void getScoreList() {
        ArrayList<String> scores = new ArrayList<>();
        scores.add("10214");
        scores.add("10208");
        scores.add("10082");
        assertEquals(test.getScoreList("2x2"), scores);
    }

    @Test
    public void setScoreList() {
        ArrayList<String> scores = new ArrayList<>();
        scores.add("10000");
        scores.add("10000");
        scores.add("10000");
        test.setScoreList("2x2", scores);
        assertEquals(test.getMyScores("2x2"), scores);
        resetUser();
    }

    @Test
    public void getSavedGames() {
    }

    @Test
    public void getSavedGamesForGameName() {
    }

    @Test
    public void setTheSavedGames() {
    }

    @Test
    public void getTheNumberSaved() {
    }

    @Test
    public void correctSlot() {
    }

    @Test
    public void getGame() {
    }

    @Test
    public void deleteGame() {
    }

    @Test
    public void saveGame() {
    }

    @Test
    public void addScore() {
    }
}