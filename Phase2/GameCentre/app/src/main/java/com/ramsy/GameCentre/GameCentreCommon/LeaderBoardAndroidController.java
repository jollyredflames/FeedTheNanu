package com.ramsy.GameCentre.GameCentreCommon;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;
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


    public LeaderBoardAndroidController(RelativeLayout container,int position,String gameName,RelativeLayout scroll,int height){
        meUser = FirebaseFuncts.getUser();
        this.container = container;
        String USERNAME = meUser.getUsername();
        this.scrollView =scroll;
        this.height = height;
        meUser = FirebaseFuncts.getUser();
        leaderBoard = FirebaseFuncts.getGlobalLeaderBoard();
        this.controller = new LeaderBoardController(gameName,USERNAME,position);
        LeaderBoardGeneralGame hi = controller.getGame();
        int pos = controller.getPosition();
        controller.setScores(leaderBoard.getGameGlobalLeaderBoard(hi.currentTabForDatabase(pos)));
//        ArrayList<String> alex = new ArrayList<>();
//        alex.add("12345 nani");
//        alex.add("167 yuh");
//        alex.add("DFJFFH czxfasdfads");
//        alex.add("1234567 FUCKING WORKS");
//        alex.add("2 yuh");
//        alex.add("17 ya boi");
//        controller.setScores(alex);
        setTextForUserNames();
        setTextForScores();
        setLeaderBoardTextView();
        upDateButton();
        updateUserNameAndScoreTextViews();
    }

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
            controller.setScores(meUser.getScoreList(hi.currentTabForDatabase(pos)));
        }
        updateUserNameAndScoreTextViews();
        upDateButton();
    }

    public void setLeaderBoardTextView(){
        TextView v = (TextView) container.getChildAt(0);
        LeaderBoardModel.generateTextViewDesign(v,controller.leaderBoardTextView());
        LeaderBoardModel.makeTextViewBold(v);
    }

    public void setTextForUserNames(){
        TextView v = (TextView) container.getChildAt(1);
        LeaderBoardModel.generateTextViewDesign(v,controller.userNameTextView());
        LeaderBoardModel.makeTextViewBold(v);
    }

    public void setTextForScores(){
        TextView v = (TextView) container.getChildAt(2);
        LeaderBoardModel.generateTextViewDesign(v,controller.scoresTextView());
        LeaderBoardModel.makeTextViewBold(v);
    }

    public void updateUserNameAndScoreTextViews(){
        TextView v;
        int smallHeight = 0;
        int x = 0;
        for(String s:controller){
            v = (TextView) scrollView.getChildAt(x);
            LeaderBoardModel.generateTextViewDesign(v,s);
            v.getLayoutParams().height =height;
            x++;
        }
        for(;x< scrollView.getChildCount();x++){
            v = (TextView) scrollView.getChildAt(x);
            v.getLayoutParams().height = smallHeight;
        }
    }

    public void upDateButton(){
        Button b = (Button) container.getChildAt(4);
        if(controller.getIsGlobal()){
            LeaderBoardModel.upDateButton(b,"View \nYour LeaderBoard");
        }
        else{
            LeaderBoardModel.upDateButton(b,"View \nPublic LeaderBoard");
        }
    }
}
