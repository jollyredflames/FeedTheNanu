package com.ramsy.GameCentre.DatabaseSavablesAndFuncts;


import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GlobalLeaderBoard{
    //TODO: MADE BOTTOM 2 non-static
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private static HashMap<String, ArrayList<String>> globalLeaderBoard = new HashMap<String, ArrayList<String>>();

    /**
     * Default constructor for GlobalLeaderBoard. Makes sure database stored offline for qquick query also.
     */
    public GlobalLeaderBoard(){
        mDatabase.keepSynced(true);
    }

    /**
     * Constructor for globalleaderboard required for FireBase initialization
     * @param globalLeaderBoard representing gameNames with scores
     */
    public GlobalLeaderBoard(HashMap<String, ArrayList<String>> globalLeaderBoard){
        mDatabase.keepSynced(true);
        GlobalLeaderBoard.globalLeaderBoard = globalLeaderBoard;
    }

    /**
     * getter method for globalLeaderBoard HashMap
     * @return globalLeaderBoard containing all games and all top scores
     */
    public HashMap<String, ArrayList<String>> getGlobalLeaderBoard() {
        return globalLeaderBoard;
    }

    /**
     * set globalLeaderBoard containing all games and all top scores. Required for Database.
     * @param globalLeaderBoard representing gameNames with scores
     */
    public void setGlobalLeaderBoard(HashMap<String, ArrayList<String>> globalLeaderBoard) {
        GlobalLeaderBoard.globalLeaderBoard = globalLeaderBoard;
    }

    /**
     * Get global Leader Board for a particular game
     * @param gameName name of game of interest
     * @return ArrayList of "score username" Strings of particular game
     */
    public ArrayList<String> getGameGlobalLeaderBoard(String gameName){
        mDatabase.keepSynced(true);
        return globalLeaderBoard.containsKey(gameName) ? globalLeaderBoard.get(gameName) : new ArrayList<String>();
    }

    /**
     * set global Leader Board for a particular game
     * @param gameName name of game of interest
     * @param gameGlobalLeaderBoard ArrayList<String> to set gameGlobalLeaderBoard to
     */
    public void setGameGlobalLeaderBoard(String gameName, ArrayList<String> gameGlobalLeaderBoard){globalLeaderBoard.put(gameName, gameGlobalLeaderBoard);}

    /**
     * Add newScore to the leaderboard if it belongs. Inform database of change after adding the score (if it needs to be added)
     * @param gameName represents the game of interest
     * @param newScore String representing score. must be able to cast to int
     * @param username represents who made this score
     */
    public void addScore(String gameName, String newScore, String username){
        sortAndPlaceNewScore(gameName, newScore, username);
        stateChange();
    }

    /**
     * Determine if newScore belongs in top 10 leaderboard
     * @param gameName represents the game this score came from
     * @param newScore represents the core. must be castable to int
     * @param username represent user to attach this score to.
     */
    private void sortAndPlaceNewScore(String gameName, String newScore, String username){
        ArrayList<String> gameScoresList = getGameGlobalLeaderBoard(gameName);
        ArrayList<Integer> intScoresList = new ArrayList<>();
        for(String temp: gameScoresList){
            intScoresList.add(Integer.parseInt(temp.split(" ")[0]));
        }
        intScoresList.add(Integer.parseInt(newScore));
        Collections.sort(intScoresList);
        Collections.reverse(intScoresList);
        Log.e("index Lists:","intScores: " + String.valueOf(intScoresList) + " gameScoreList: " + String.valueOf(gameScoresList));
        boolean currentScoreAdded = false;
        for(int i = 0; i <= 9; i++){
            Log.e("index: ",String.valueOf(i));
            if(i > gameScoresList.size() - 1){
                if (!currentScoreAdded) {
                    gameScoresList.add(newScore + " " + username);
                }
                i=10;
                Log.e("index problem: ",String.valueOf(i));
                break;
            }
            Log.e("index after first if: ",String.valueOf(i));
            if(intScoresList.get(i) != Integer.parseInt(gameScoresList.get(i).split(" ")[0]) && !currentScoreAdded) {
                gameScoresList.add(i, String.valueOf(intScoresList.get(i)) + " " + username);
                currentScoreAdded = true;
            }
        }
        while(gameScoresList.size() > 10){gameScoresList.remove(gameScoresList.size()-1);}
        globalLeaderBoard.put(gameName, gameScoresList);
    }

    /**
     * Private method that informs FireBase about a change in this instances properties. Updates data on database appropriately.
     */
    private void stateChange(){
        mDatabase.child("GlobalLeaderBoard").setValue(this);
    }

}