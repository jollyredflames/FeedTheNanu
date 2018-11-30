package com.ramsy.GameCentre.GameCentreCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//private TextView mTxtView;
//int height = 50; //your textview height
//mTxtView.getLayoutParams().height = height;
public class LeaderBoardView extends Fragment {
    int mainHeight;
    int mainWidth;
    RelativeLayout activityView;
    RelativeLayout scrollContainer;
    int numTextViews;
    int buttonID;

    String lastGame;
    String lastScore;
    String gameIdentifier;
    int position;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        lastGame = bundle.getString("lastGame");
        lastScore = bundle.getString("lastScore");
        gameIdentifier = bundle.getString("gameIdentifier");
        position = bundle.getInt("position");
        setInstanceVariables();
        addLeaderBoardTextView();
        addUserNameTextView();
        addScoreTextView();
        addTextViewsNamesAndScores();
        addScrollViewToActivityContainer();
        addUpdaterButton();
        addPrevScore();
        Button changer = (Button)activityView.getChildAt(4);
        changer.setOnClickListener(new LeaderBoardAndroidController(activityView,position,gameIdentifier,scrollContainer,mainHeight/10,lastGame,lastScore));
        return activityView;
    }

    private void addTextViewsNamesAndScores(){
        for(int i = 3;i<2*numTextViews+3; i=i+2){
            TextView tempUserName = new TextView(getActivity());
            tempUserName.setText(String.valueOf(i));
            tempUserName.setId(i);
            TextView tempScore = new TextView(getActivity());
            tempScore.setText(String.valueOf(i+1));
            tempScore.setId(i+1);
            scrollContainer.addView(tempUserName);
            scrollContainer.addView(tempScore);
            defineDimensionsOfTextViews(tempUserName,tempScore,i);
        }
    }

    private void defineDimensionsOfTextViews(TextView tempUserName, TextView tempScore, int index){
        RelativeLayout.LayoutParams uParam = new RelativeLayout.LayoutParams(mainWidth,300);
        uParam.addRule(RelativeLayout.BELOW,index-1);
        tempUserName.setLayoutParams(uParam);
        RelativeLayout.LayoutParams sParam = new RelativeLayout.LayoutParams(mainWidth,300);
        sParam.addRule(RelativeLayout.BELOW,index-1);
        sParam.addRule(RelativeLayout.RIGHT_OF,index);
        tempScore.setLayoutParams(sParam);
    }
    private void addLeaderBoardTextView(){
        TextView leaderBoardName = new TextView(getActivity());
        leaderBoardName.setId(1);
        activityView.addView(leaderBoardName);
        RelativeLayout.LayoutParams leaderBoardNameParam = new RelativeLayout.LayoutParams(mainWidth*2,200);
        leaderBoardNameParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        leaderBoardNameParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leaderBoardName.setLayoutParams(leaderBoardNameParam);
    }

    private void addUserNameTextView(){
        TextView userNameTextView = new TextView(getActivity());
        userNameTextView.setId(2);
        activityView.addView(userNameTextView);
        RelativeLayout.LayoutParams userNameParam = new RelativeLayout.LayoutParams(mainWidth,mainHeight/20);
        userNameParam.addRule(RelativeLayout.BELOW,1);
        userNameTextView.setLayoutParams(userNameParam);
    }

    private void addScoreTextView(){
        TextView scoreTextView = new TextView(getActivity());
        activityView.addView(scoreTextView);
        RelativeLayout.LayoutParams scoreParam = new RelativeLayout.LayoutParams(mainWidth,mainHeight/20);
        scoreParam.addRule(RelativeLayout.BELOW,1);
        scoreParam.addRule(RelativeLayout.RIGHT_OF,2);
        scoreTextView.setLayoutParams(scoreParam);
    }

    private void addUpdaterButton(){
        Button updaterButton = new Button(getActivity());
        updaterButton.setText("View other");
        updaterButton.setId(2*numTextViews+4);
        activityView.addView(updaterButton);
        RelativeLayout.LayoutParams upDaterParam = new RelativeLayout.LayoutParams(mainWidth, 200);
        upDaterParam.addRule(RelativeLayout.BELOW,2*numTextViews+3);
        updaterButton.setLayoutParams(upDaterParam);
        buttonID = 2*numTextViews+4;
    }

    private void addPrevScore(){
        TextView prevScore = new TextView(getActivity());
        prevScore.setText("Your score was 1000");
        activityView.addView(prevScore);
        RelativeLayout.LayoutParams prevScoreParam = new RelativeLayout.LayoutParams(mainWidth, 200);
        prevScoreParam.addRule(RelativeLayout.BELOW,2*numTextViews+3);
        prevScoreParam.addRule(RelativeLayout.RIGHT_OF,2*numTextViews+4);
        prevScore.setLayoutParams(prevScoreParam);
    }

    private void addScrollViewToActivityContainer(){
        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.addView(scrollContainer);
        scrollView.setId(2*numTextViews+3);
        RelativeLayout.LayoutParams items = new RelativeLayout.LayoutParams(mainWidth*2,mainHeight/2);
        items.addRule(RelativeLayout.BELOW,2);
        scrollView.setLayoutParams(items);
        activityView.addView(scrollView);
    }

    private void setInstanceVariables(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mainHeight = displaymetrics.heightPixels;
        mainWidth = displaymetrics.widthPixels/2;
        activityView = new RelativeLayout(getActivity());
        scrollContainer = new RelativeLayout(getActivity());
        numTextViews =10;
    }
}