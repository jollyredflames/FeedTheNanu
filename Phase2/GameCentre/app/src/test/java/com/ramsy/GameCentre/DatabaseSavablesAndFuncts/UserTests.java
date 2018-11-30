package com.ramsy.GameCentre.DatabaseSavablesAndFuncts;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class UserTests {

    UserTestable test = new UserTestable();

    @Before
    public void setUp() {
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
        HashMap<String, ArrayList<SaveState>> savedGames = new HashMap<>();
        ArrayList<SaveState> gamesSaved = new ArrayList<>();
        gamesSaved.add(new SaveState(23, (float)40.23));
        gamesSaved.add(new SaveState(57, (float)55.23));
        savedGames.put("2x2", gamesSaved);
        test.setTheSavedGames(savedGames);
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
        HashMap<String, ArrayList<SaveState>> savedGames = new HashMap<>();
        ArrayList<SaveState> gamesSaved = new ArrayList<>();
        gamesSaved.add(new SaveState(23, (float)40.23));
        gamesSaved.add(new SaveState(57, (float)55.23));
        savedGames.put("2x2", gamesSaved);
        test.setTheSavedGames(savedGames);
    }

    @Test
    public void getUsername() {
        assertEquals( "testUser", test.getUsername());
    }

    @Test
    public void setUsername() {
        test.setUsername("UserNameChanged");
        assertEquals("UserNameChanged", test.getUsername());
        resetUser();
    }

    @Test
    public void getEmail() {
        assertEquals("testingtestuser@testedtests.com", test.getEmail());
    }

    @Test
    public void setEmail() {
        test.setEmail("EmailChanged");
        assertEquals("EmailChanged", test.getEmail());
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
        assertEquals(myScores, test.getMyScores());
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
        assertEquals(myScores, test.getMyScores());
        resetUser();
    }

    @Test
    public void getMeUserID() {
        assertEquals("kkNWsAk2jUfqQLsws5bmPZXqkQ72", test.getMeUserID());
    }

    @Test
    public void setMeUserID() {
        test.setMeUserID("test");
        assertEquals("test", test.getMeUserID());
        resetUser();
    }

    @Test
    public void getScoreList() {
        ArrayList<String> scores = new ArrayList<>();
        scores.add("10214");
        scores.add("10208");
        scores.add("10082");
        assertEquals(scores, test.getScoreList("2x2"));
    }

    @Test
    public void setScoreList() {
        ArrayList<String> scores = new ArrayList<>();
        scores.add("10000");
        scores.add("10000");
        scores.add("10000");
        test.setScoreList("2x2", scores);
        assertEquals(scores, test.getScoreList("2x2"));
        resetUser();
    }

    @Test
    public void getSavedGames() {
        assert(test.getSavedGames() != null);
    }

    @Test
    public void getSavedGamesForGameName() {
        ArrayList<SaveState> saves = test.getSavedGamesForGameName("2x2");
        assert(test.getSavedGames() != null);
    }

    @Test
    public void setTheSavedGames() {
        ArrayList<SaveState> saves = new ArrayList<>();
        HashMap<String, ArrayList<SaveState>> savedGames = new HashMap<>();
        savedGames.put("2x2", saves);
        test.setTheSavedGames(savedGames);
        assertEquals(savedGames, test.getSavedGames());
        resetUser();
    }

    @Test
    public void getTheNumberSaved() {
        assertEquals(2, test.getTheNumberSaved("2x2"));
    }

    @Test
    public void correctSlot() {
        assertEquals(2, test.correctSlot("SlidingTiles"));
    }

    @Test
    public void getGame() {
        assertEquals(null, test.getGame("2x2", 2));
        assert(test.getGame("2x2", 0) != null);
    }

    @Test
    public void deleteGame() {
        int beforeDel = test.getTheNumberSaved("2x2");
        test.deleteGame("2x2", 0);
        assertEquals(beforeDel-1, test.getTheNumberSaved("2x2"));
        resetUser();
    }

    @Test
    public void saveGame() {
        SaveState newSave = test.getGame("2x2", 0);
        test.saveGame("2x2", newSave, 2);
        assertEquals(3, test.getTheNumberSaved("2x2"));
        assertEquals(2, test.correctSlot("SlidingTiles"));
        resetUser();
    }

    @Test
    public void addScore() {
        test.addScore("test", "9");
        test.addScore("test", "10");
        test.addScore("test", "7");
        assertEquals("10", test.getScoreList("test").get(0));
        assertEquals("9", test.getScoreList("test").get(1));
        assertEquals("7", test.getScoreList("test").get(2));
    }
}