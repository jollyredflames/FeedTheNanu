package com.ramsy.GameCentre.GameCentreCommon;

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
import android.widget.TextView;

/**
 * This class is the view in the MVC design pattern. It will create the text views that the user
 * sees in the fragments of the activity.
 */
public class LeaderBoardView extends Fragment {

    private int numScores = 10;
    private int[] TOP_TEXT_VIEWS_ID = {1,2,2*numScores+3,2*numScores+4};


    public LeaderBoardView() {}

    private RelativeLayout mainContainer;
    private int mainHeight;
    private  int mainWidth;

    /**
     * @param inflater not used
     * @param container not used
     * @param savedInstanceState not used
     * @return Relative Layout that the user will see and interact with
     *         return the fragment that the user will see
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String lastScore = getArguments().getString("lastScore");
        Boolean showScore = getArguments().getBoolean("showTheirScore");
        String currentGame = getArguments().getString("currentGame");
        //new FirebaseFuncts(LeaderBoardActivity.userUID);
        setInstanceVariables();
        addUserNameTopTextView();
        addScoreTopTextView();
        addTextViewsNamesAndScores();
        addUpdateLeaderBoardButton();
        Button upDaterButton = (Button) mainContainer.getChildAt(TOP_TEXT_VIEWS_ID[3]-2);
        upDaterButton.setOnClickListener(new LeaderBoardController(mainContainer, numScores, showScore,lastScore,currentGame));
        return mainContainer;
    }
    private void setInstanceVariables(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mainHeight = displaymetrics.heightPixels/(2*numScores-numScores/3);
        mainWidth = displaymetrics.widthPixels/2;
        mainContainer = new RelativeLayout(getActivity());
    }
    /**
     *  this method adds the username text views and scores text views that will be updated
     *  later with proper information.
     */
    private void addTextViewsNamesAndScores(){
        for(int i = 0;i<2*numScores; i=i+2){
            TextView tempUserName = new TextView(getActivity());
            tempUserName.setId(i+3);
            TextView tempScore = new TextView(getActivity());
            tempScore.setId(i+4);
            mainContainer.addView(tempUserName);
            mainContainer.addView(tempScore);
            defineDimensionsOfTextViews(tempUserName,tempScore,i);
        }
    }
    /**
     * @param tempUserName The current username text view
     * @param tempScore the current score text view
     * @param index the id for the text views that they should be below and to the right of
     * this method will set the dimensions of each text view and where to place it on the fragment
     */
    private void defineDimensionsOfTextViews(TextView tempUserName, TextView tempScore, int index){
        RelativeLayout.LayoutParams uParam = new RelativeLayout.LayoutParams(mainWidth,mainHeight);
        uParam.addRule(RelativeLayout.BELOW,index+1);
        tempUserName.setLayoutParams(uParam);
        RelativeLayout.LayoutParams sParam = new RelativeLayout.LayoutParams(mainWidth,mainHeight);
        sParam.addRule(RelativeLayout.BELOW,index+2);
        sParam.addRule(RelativeLayout.RIGHT_OF,index+1);
        tempScore.setLayoutParams(sParam);
    }
    /**
     *  make the text view that says Scores: for aesthetics
     */
    private void addScoreTopTextView(){
        TextView scoreTop = new TextView(getActivity());
        scoreTop.setId(TOP_TEXT_VIEWS_ID[1]);
        mainContainer.addView(scoreTop);
        RelativeLayout.LayoutParams scoreTopParam = new RelativeLayout.LayoutParams(mainWidth, mainHeight*2);
        scoreTopParam.addRule(RelativeLayout.RIGHT_OF,1);
        scoreTopParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        scoreTop.setLayoutParams(scoreTopParam);
    }
    /**
     *  make the Text View that says UserName: for aesthetics
     */
    private void addUserNameTopTextView(){
        TextView userNameTop = new TextView(getActivity());
        userNameTop.setId(TOP_TEXT_VIEWS_ID[0]);
        mainContainer.addView(userNameTop);
        RelativeLayout.LayoutParams userNameTopParam = new RelativeLayout.LayoutParams(mainWidth, mainHeight*2);
        userNameTopParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        userNameTopParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        userNameTop.setLayoutParams(userNameTopParam);
    }
    /**
     * make the Text View that will change between global and private LeaderBoardActivity
     */
    private void addUpdateLeaderBoardButton(){
        Button updaterButton;
        updaterButton = new Button(getActivity());
        updaterButton.setId(TOP_TEXT_VIEWS_ID[2]);
        mainContainer.addView(updaterButton);
        RelativeLayout.LayoutParams updaterButtonParam = new RelativeLayout.LayoutParams(mainWidth,mainHeight*2);
        updaterButtonParam.addRule(RelativeLayout.BELOW,2*numScores+2);
        updaterButton.setLayoutParams(updaterButtonParam);
        addCurrentScoreTextView();
    }
    /**
     * add the text view that will show the users current score if they are in the correct tab
     */
    private void addCurrentScoreTextView(){
        TextView currentScore = new TextView(getActivity());
        currentScore.setId(TOP_TEXT_VIEWS_ID[3]);
        mainContainer.addView(currentScore);
        RelativeLayout.LayoutParams currentScoreParam = new RelativeLayout.LayoutParams(mainWidth, mainHeight*2);
        currentScoreParam.addRule(RelativeLayout.RIGHT_OF,2*numScores+3);
        currentScoreParam.addRule(RelativeLayout.BELOW,2*numScores+2);
        currentScore.setLayoutParams(currentScoreParam);
    }

}