package com.ramsy.GameCentre.GameCentreCommon;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.GlobalLeaderBoard;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.MemoryMatrix.Block;
import com.ramsy.GameCentre.MemoryMatrix.MemoryMatrixManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * this class acts as the controller in the MVC, and when the user clicks the text view to switch
 * between private and public leader boards, it will tell the model to update
 */
public class LeaderBoardController implements Iterable<String>{
    private ArrayList<String> scores = new ArrayList<>();
    private boolean isGlobal = true;
    private LeaderBoardGeneralGame game;
    private String userName;
    private int position;



    public LeaderBoardController(String game,String userName,int position){
        LeaderBoardFactory factory = new LeaderBoardFactory();
        this.game = factory.getGame(game);
        this.userName = userName;
        this.position = position;
    }

    public String topLeaderBoardTextView(){
        return game.displayLeaderBoard(position);
    }

    public String scoresTextView(){
        return "Scores:";
    }

    public String userNameTextView(){
        return "Usernames:";
    }

    public String leaderBoardTextView(){
        return game.displayLeaderBoard(position);
    }


   public void setScores(ArrayList<String> newScore){
        scores = newScore;
   }

   public void setGlobal(boolean isGlobal){
        this.isGlobal = isGlobal;
   }

   public boolean getIsGlobal(){
        return isGlobal;
   }

   public String getButtonText(){
        if (isGlobal){
            return "View \nYour Scores";
        }
        return "View \nPublic Scores";
   }

   public String getLastScoreText(int value){
        return "Your score was: \n"+String.valueOf(value);
   }


    @Override
    public Iterator iterator() {
        return new LeaderBoardIterator();
    }

    private class LeaderBoardIterator implements Iterator<String> {
        /**
         * The index of the next item in the class list.
         */
        private int nextIndex = 0;
        private boolean name = true;

        @Override
        public boolean hasNext() {
            return nextIndex != scores.size();
        }

        @Override
        public String next() {
            String current = scores.get(nextIndex);
            String[] parts = current.split(" ",2);
            String score = parts[0].substring(0,Math.min(16,parts[0].length()));
            String username;
            if (parts.length == 2){
                username = parts[1].substring(0,Math.min(24,parts[1].length()));
            }
            else{
                username = userName;
            }
            if(name){
                name = false;
                return username;
            }else{
                nextIndex++;
                name = true;
                return score;
            }
        }
    }

    public LeaderBoardGeneralGame getGame(){
        return game;
    }

    public int getPosition(){
        return position;
    }
}