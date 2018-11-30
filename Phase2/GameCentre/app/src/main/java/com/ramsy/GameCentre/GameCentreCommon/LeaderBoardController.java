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


    /**
     *
     * @param game a string game that will be turned into a general game using the
     *             leaderboard factory
     * @param userName  a username to be used for the private leader board
     * @param position  the current fragment you are on
     */
    public LeaderBoardController(String game,String userName,int position){
        LeaderBoardFactory factory = new LeaderBoardFactory();
        this.game = factory.getGame(game);
        this.userName = userName;
        this.position = position;
    }

    /**
     *
     * @return the string that should be displayed for scores text view
     */
    public String scoresTextView(){
        return "Scores:";
    }

    /**
     *
     * @return the string that should be display for the username text view
     */
    public String userNameTextView(){
        return "Usernames:";
    }

    /**
     *
     * @return a string of the current leader boards name
     */
    public String leaderBoardTextView(){
        return game.displayLeaderBoard(position);
    }

    /**
     *
     * @param newScore set the scores array list to the new score array list
     */
   public void setScores(ArrayList<String> newScore){
        scores = newScore;
   }

    /**
     *
     * @param isGlobal set the global leader board to public if true and priavte
     *                 if false
     */
   public void setGlobal(boolean isGlobal){
        this.isGlobal = isGlobal;
   }

    /**
     * @return true is the leader board is in public mode and false
     *                      if the leader board is in private mode
     */
   public boolean getIsGlobal(){
        return isGlobal;
   }


    /**
     *
     * @return a iterator of the leader board
     */
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

        /**
         *
         * @return true if there are still more strings to display in the leader board
         */
        @Override
        public boolean hasNext() {
            return nextIndex != scores.size();
        }

        /**
         *
         * @return the next string in the leader board
         */
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

    /**
     *
     * @return the game that is being displayed on the leader board
     */
    public LeaderBoardGeneralGame getGame(){
        return game;
    }

    /**
     *
     * @return the position of this fragment in the leader board
     */
    public int getPosition(){
        return position;
    }
}