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

public class LeaderBoardAndroidController implements Button.OnClickListener{
    RelativeLayout container;
    LeaderBoardController controller;
    RelativeLayout scrollView;
    private User meUser;
    private int height;
    private GlobalLeaderBoard leaderBoard;
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String lastGame;
    private String lastScore;
    private boolean showScore;

    /**
     * Constructor for LeaderBoardAndroidController which sets the views beautifully with the scores.
     * @param container where to display the scores
     * @param position fragment position (which page of the leaderboard are you on)
     * @param gameName which game we are looking at
     * @param scroll contains textviews in which scores and usernames will be held
     * @param height height of an individual row in scroll
     * @param lastGame Name of the last game you played
     * @param lastScore score of your last game
     */
    public LeaderBoardAndroidController(RelativeLayout container,int position,String gameName,RelativeLayout scroll,int height,String lastGame,String lastScore){
        meUser = FirebaseFuncts.getUser();
        this.lastScore = lastScore;
        this.lastGame = lastGame;
        this.container = container;
        String USERNAME = meUser.getUsername();
        this.scrollView =scroll;
        this.height = height;
        meUser = FirebaseFuncts.getUser();
        leaderBoard = FirebaseFuncts.getGlobalLeaderBoard();
        this.controller = new LeaderBoardController(gameName,USERNAME,position);
        LeaderBoardGeneralGame hi = controller.getGame();
        int pos = controller.getPosition();
        String value = hi.currentTabForDatabase(pos);
        ArrayList<String> temp = leaderBoard.getGameGlobalLeaderBoard(value);
        controller.setScores(temp);
        showScore = lastGame.equals(value);
        setTextForUserNames();
        setTextForScores();
        setLeaderBoardTextView();
        upDateButton();
        updateUserNameAndScoreTextViews();
        lastScoreTextView();
    }

    /**
     * Gets the globalleaderboard or private leaderboard on tap
     * @param v the "update" button that is clicked.
     */
    @Override
    public void onClick(View v){
        controller.setGlobal(!controller.getIsGlobal());
        if(controller.getIsGlobal()){
            LeaderBoardGeneralGame hi = controller.getGame();
            int pos = controller.getPosition();
            controller.setScores(leaderBoard.getGameGlobalLeaderBoard(hi.currentTabForDatabase(pos)));
        }
        else{
            LeaderBoardGeneralGame hi = controller.getGame();
            int pos = controller.getPosition();
            ArrayList<String> temp = meUser.getScoreList(hi.currentTabForDatabase(pos));
            controller.setScores(temp);
        }
        updateUserNameAndScoreTextViews();
        upDateButton();
    }

    /**
     * Bolds the title text view and centers the text.
     */
    public void setLeaderBoardTextView(){
        TextView v = (TextView) container.getChildAt(0);
        LeaderBoardModel.generateTextViewDesign(v,controller.leaderBoardTextView());
        LeaderBoardModel.makeTextViewBold(v);
    }

    /**
     * Bolds all usernames
     */
    public void setTextForUserNames(){
        TextView v = (TextView) container.getChildAt(1);
        LeaderBoardModel.generateTextViewDesign(v,controller.userNameTextView());
        LeaderBoardModel.makeTextViewBold(v);
    }

    /**
     * bolds all scores
     */
    public void setTextForScores(){
        TextView v = (TextView) container.getChildAt(2);
        LeaderBoardModel.generateTextViewDesign(v,controller.scoresTextView());
        LeaderBoardModel.makeTextViewBold(v);
    }

    /**
     * Runs through all the scrollable and adds appropriate scores in relevant indexes
     */
    public void updateUserNameAndScoreTextViews(){
        TextView v;
        int smallHeight = 0;
        int x = 0;
        for(String s:controller){
            v = (TextView) scrollView.getChildAt(x);
            LeaderBoardModel.generateTextViewDesign(v,s);
            v.getLayoutParams().height = height;
            x++;
        }
        for(;x< scrollView.getChildCount();x++){
            v = (TextView) scrollView.getChildAt(x);
            v.getLayoutParams().height = smallHeight;
            v.setText("---");
        }
    }

    /**
     * Gets the globalleaderboard or private leaderboard on tap
     */
    public void upDateButton(){
        Button b = (Button) container.getChildAt(4);
        if(controller.getIsGlobal()){
            LeaderBoardModel.upDateButton(b,"View \nYour LeaderBoard");
        }
        else{
            LeaderBoardModel.upDateButton(b,"View \nPublic LeaderBoard");
        }
    }

    /**
     * The very last element in the scroller shows your score
     */
    public void lastScoreTextView(){
        TextView t = (TextView) container.getChildAt(5);
        if(showScore){
            LeaderBoardModel.generateTextViewDesign(t,"Your Score Was: \n"+lastScore);
        }
        else{
            LeaderBoardModel.generateTextViewDesign(t,"");
        }
    }
}
