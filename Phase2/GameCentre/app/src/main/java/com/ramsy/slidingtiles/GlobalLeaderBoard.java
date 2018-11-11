package com.ramsy.slidingtiles;


import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GlobalLeaderBoard{

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private static HashMap<String, ArrayList<String>> globalLeaderBoard = new HashMap<String, ArrayList<String>>();

    public GlobalLeaderBoard(){
        mDatabase.keepSynced(true);
    }

    public GlobalLeaderBoard(HashMap<String, ArrayList<String>> globalLeaderBoard){
        mDatabase.keepSynced(true);
        this.globalLeaderBoard = globalLeaderBoard;
    }

    public HashMap<String, ArrayList<String>> getGlobalLeaderBoard() {
        return globalLeaderBoard;
    }

    public void setGlobalLeaderBoard(HashMap<String, ArrayList<String>> globalLeaderBoard) {
        this.globalLeaderBoard = globalLeaderBoard;
    }

    public ArrayList<String> getGameGlobalLeaderBoard(String gameName){
        mDatabase.keepSynced(true);
        return globalLeaderBoard.containsKey(gameName) ? globalLeaderBoard.get(gameName) : new ArrayList<String>();
    }

    public void setGameGlobalLeaderBoard(String gameName, ArrayList<String> gameGlobalLeaderBoard){globalLeaderBoard.put(gameName, gameGlobalLeaderBoard);}

    public void addScore(String gameName, String newScore, String username){
        sortAndPlaceNewScore(gameName, newScore, username);
        stateChange();
    }

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

    private void stateChange(){
        mDatabase.child("GlobalLeaderBoard").setValue(this);
    }

}