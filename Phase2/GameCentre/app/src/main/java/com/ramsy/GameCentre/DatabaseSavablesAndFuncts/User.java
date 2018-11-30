package com.ramsy.GameCentre.DatabaseSavablesAndFuncts;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class User {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String username;
    private String email;
    private static HashMap<String, ArrayList<String>> myScores = new HashMap<>();

    private static HashMap<String, ArrayList<SaveState>> savedGames = new HashMap<>();

    private String meUserID;

    /**
     * Create a user with No Attributes (used to implement Serializable for FireBase)
     */
    public User() {
        mDatabase.keepSynced(true);
        this.email = "";
        this.username = "";
    }

    /**
     * Return a USer with all attributes
     * @param username
     * @param email
     * @param meUserID
     * @param myScores
     * @param savedGames
     */
    public User(String username, String email, String meUserID, HashMap<String, ArrayList<String>> myScores, HashMap<String, ArrayList<SaveState>> savedGames) {
        mDatabase.keepSynced(true);
        this.email = email;
        this.username = username;
        User.myScores = myScores;
        this.meUserID = meUserID;
        User.savedGames = savedGames;
    }

    /**
     * Return this user's username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set this user's username
     * @param username string of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Return this user's email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set this user's email
     * @param  email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Return a Dictionary with all scoreboards of all games
     * @return myScores
     */
    public HashMap<String, ArrayList<String>> getMyScores() {
        return myScores == null ? new HashMap<String, ArrayList<String>>() : myScores;
    }

    /**
     * set scores for all games of this user
     * @param myScores
     */
    public void setMyScores(HashMap<String, ArrayList<String>> myScores) {
        User.myScores = myScores;
    }

    /**
     * return this user's UID
     * @return UID : String unique ID
     */
    public String getMeUserID() {
        return meUserID;
    }

    /**
     * set this user's UID
     * @param meUserID : String this user's unique ID
     */
    public void setMeUserID(String meUserID) {
        this.meUserID = meUserID;
    }

    /**
     * Returns top 10 scores if the scores exist, else returns an empty new ArrayList
     * @param gameName : String name of game you are indexing for
     * @return scoreList : top 10 scores of this game
     */
    public ArrayList<String> getScoreList(String gameName){return myScores.containsKey(gameName) ? myScores.get(gameName) : new ArrayList<String>();}

    /**
     * set the top 10 scores for a game
     * @param gameName
     * @param myScoresList
     */
    public void setScoreList(String gameName, ArrayList<String> myScoresList){myScores.put(gameName, myScoresList);}

    /**
     * return a list of 3 saved games
     * @return savedGame : SaveState if a list of saved game exists, else return null
     */
    public HashMap<String, ArrayList<SaveState>> getSavedGames() {
        return savedGames == null ? new HashMap<String, ArrayList<SaveState>>() : savedGames;
    }

    /**
     * return a list of 3 saved games
     * @return savedGame : SaveState if a list of saved game exists, else return null
     */
    public ArrayList<SaveState> getSavedGamesForGameName(String gameName) {
        return savedGames.containsKey(gameName) ? savedGames.get(gameName) : new ArrayList<>();
    }

    /**
     * Set the savedGames of this user
     * @param savedGames : A list of all the saved games of this user.
     */
    public void setTheSavedGames(HashMap<String, ArrayList<SaveState>> savedGames) {
        User.savedGames = savedGames;
    }

    /**
     * Returns the number of saved games on file
     * @return savedGames.size()
     */
    public int getTheNumberSaved(String gameName){
        if(savedGames.containsKey(gameName)){
            return savedGames.get(gameName).size();
        }
        savedGames.put(gameName, new ArrayList<SaveState>());
        return 0;
    }

    /**
     * Returns the first available slot available for save. Defaults to slot 3 if all slots taken.
     * @return slotAvailable
     */
    public int correctSlot(String gameName){
        ArrayList<SaveState> savedGamesForName = savedGames.containsKey(gameName) ? savedGames.get(gameName) : new ArrayList<>();
        if(savedGamesForName.size() > 3){
            deleteGame(gameName,2);
            stateChange();
            return 2;
        }else{
            return savedGamesForName.size();
        }
    }

    /**
     * Return the game saved at this index
     * @param index of game slot
     * @return SaveState
     */
    public SaveState getGame(String gameName, int index){
        if (savedGames.containsKey(gameName)) {
            return savedGames.get(gameName).size() > index ? savedGames.get(gameName).get(index): null;
        } else {
            return null;
        }
    }

    /**
     * Delete the game at this slot
     * @param index of the slot to be wiped
     */
    public void deleteGame(String gameName, int index){
        savedGames.get(gameName).remove(index);
        stateChange();
    }

    /**
     * save the game and then upload the new data to FireBase
     * @param state of the game to be saved
     * @param index the slot to save the game at
     */
    public void saveGame(String gameName, SaveState state, int index){
        ArrayList<SaveState> savedGamesForName = savedGames.containsKey(gameName) ? savedGames.get(gameName) : new ArrayList<>();
        if(savedGamesForName.size()>index){
            savedGamesForName.remove(index);
        }
        savedGamesForName.add(index, state);
        savedGames.put(gameName, savedGamesForName);
        stateChange();
    }

    /**
     * Add newScore to your scores and check if it should be included in any leaderboard
     * @param gameName the game name
     * @param newScore the score of the game
     */
    public void addScore(String gameName, String newScore){
        GlobalLeaderBoard glb = new GlobalLeaderBoard();
        glb.addScore(gameName, newScore, getUsername());

        sortAndPlaceNewScore(gameName, newScore);

        stateChange();
    }

    /**
     * add the new score, sort the leader board, trim to the first 10, update the gameScoreList
     * @param subGameName the game name
     * @param newScore the score of the game
     */
    private void sortAndPlaceNewScore(String subGameName, String newScore){
        ArrayList<String> myScoresList = getScoreList(subGameName);
        myScoresList.add(newScore);
        ArrayList<Integer> intScoresList = new ArrayList<>();
        for(String temp: myScoresList){
            intScoresList.add(Integer.parseInt(temp));
        }
        Collections.sort(intScoresList);
        Collections.reverse(intScoresList);
        while (intScoresList.size() > 10){
            intScoresList.remove(intScoresList.size()-1);
        }
        myScoresList.clear();
        for(int temp: intScoresList){
            myScoresList.add(String.valueOf(temp));
        }
        myScores.put(subGameName, myScoresList);
    }

    /**
     * Inform FireBase of a change in representation of this user
     */
    private void stateChange(){
        mDatabase.child("users").child(meUserID).setValue(this);
    }


}

