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

import java.util.ArrayList;

/**
 * this class acts as the controller in the MVC, and when the user clicks the text view to switch
 * between private and public leader boards, it will tell the model to update
 */
public class LeaderBoardController implements TextView.OnClickListener {

    private User meUser;
    private GlobalLeaderBoard leaderBoard;
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    //DO NOT TOUCH
    private Boolean publicLeaderBoard = true;
    private int numScores;
    private RelativeLayout mainContainer;
    private String lastScore;
    private Boolean showScore;
    private String currentGame;

    /**
     *
     * @param container this has all the text views and buttons from leader board view
     * @param numScores the number of scores to display
     * @param showScore a bool to determine if the users last score should be displayed on this tab
     * @param lastScore the users last score
     * @param currentGame a string of the game played ex "3x3"
     */
    public LeaderBoardController(RelativeLayout container, int numScores, Boolean showScore,String lastScore,String currentGame) {
        this.mainContainer = container;
        this.numScores = numScores;
        this.showScore = showScore;
        this.lastScore = lastScore;
        this.currentGame = currentGame;
        meUser = FirebaseFuncts.getUser();
        leaderBoard = FirebaseFuncts.getGlobalLeaderBoard();
        changeUserNameAndScoreTextViews();
        setTextForTopTextViews();
        setLastScore();
        Button changer = (Button) mainContainer.getChildAt(2*numScores+2);
        buttonText(changer);
        LeaderBoardModel.setButtonDetails(changer);
        changeUserNameAndScoreTextViews();
    }
    @Override
    public void onClick(View v) {
        meUser = FirebaseFuncts.getUser();
        leaderBoard = FirebaseFuncts.getGlobalLeaderBoard();
        this.publicLeaderBoard = !publicLeaderBoard;
        Button changer = (Button) mainContainer.getChildAt(2*numScores+2);
        buttonText(changer);
        changeUserNameAndScoreTextViews();
    }

    /**
     * set the text of the text views objects that need to display the leader board scores
     * and user names
     */
    private void changeUserNameAndScoreTextViews() {
        meUser = FirebaseFuncts.getUser();
        leaderBoard = FirebaseFuncts.getGlobalLeaderBoard();
        //ArrayList<String> leaderScores = getLeaderScores();
        ArrayList <String> leaderScores = new ArrayList<String>();
        leaderScores = leaderBoard.getGameGlobalLeaderBoard(currentGame);
        if (publicLeaderBoard == true) { //if public leaderboard
            leaderScores = leaderBoard.getGameGlobalLeaderBoard(currentGame);
        }
        else if (publicLeaderBoard == false) { //if own user's leaderboard
            leaderScores = meUser.getScoreList(currentGame);
        }
        for(int i = 2, x = 0;i<numScores*2+2; i=i+2,x=x+1){
            String current = leaderScores.size()-1 >= x ? leaderScores.get(x): "--- ---";
            String[] parts = current.split(" ",2);
            String score = parts[0].substring(0,Math.min(16,parts[0].length()));
            String username;
            if (parts.length == 2){
                username = parts[1].substring(0,Math.min(24,parts[1].length()));
            }
            else{
                username = meUser.getUsername();
            }
            TextView left = (TextView) mainContainer.getChildAt(i);
            LeaderBoardModel.generateTextViewDesign(left, username);
            TextView right = (TextView) mainContainer.getChildAt(i+1);
            LeaderBoardModel.generateTextViewDesign(right, score);
        }
    }

    /**
     * Set the text of the top two text views to say what leader board is being displayed and
     * Scores: the top left text view will say Scores:
     */
    private void setTextForTopTextViews(){
        TextView UserName = (TextView) mainContainer.getChildAt(0);
        TextView Scores = (TextView) mainContainer.getChildAt(1);
        LeaderBoardModel.generateTextViewDesign(UserName,"LeaderBoard for "+currentGame+"\n\nUserNames:");
        LeaderBoardModel.makeTextViewBold(UserName);
        LeaderBoardModel.generateTextViewDesign(Scores,"\n\n Scores:");
        LeaderBoardModel.makeTextViewBold(Scores);
    }

    /**
     * if the user is viewing the tab for the last game they played it will show their score for
     * comparison, otherwise display nothing
     */
    private void setLastScore() {
        TextView scoreTextView = (TextView) mainContainer.getChildAt(2*numScores+3);
        String text;
        if (showScore){
            text = " Your score was: \n "+ lastScore;
        }
        else{
            text = "";
        }
        LeaderBoardModel.generateTextViewDesign(scoreTextView,text);
    }

    /**
     *
     * @param changer the button that is to be changed
     *                the button text will change between show private and show your local scores
     */
    private void buttonText(Button changer){
        String text;
        if (publicLeaderBoard) {
            text = "View Your \n LeaderBoard";
        }
        else {
            text = "View Public \n LeaderBoard";
        }
        LeaderBoardModel.upDateButton(changer,text);
    }

}