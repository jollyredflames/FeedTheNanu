package com.ramsy.GameCentre.GameCentreCommon;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * this class acts as the model in the MVC design pattern, this will update the
 * text for the various text views based on if the user wants to see private or global leader
 * boards
 */

public class LeaderBoardModel {

    /**
     *
     * @param textView the text view to be changed
     * @param text  the text for the text view
     *              change the text views message to text
     */
    public static void generateTextViewDesign(TextView textView,String text){
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        RelativeLayout.LayoutParams tempParam = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        tempParam.setMargins(25,0,0,0);
        textView.setLayoutParams(tempParam);
    }

    /**
     *
     * @param changer the button which text i added to
     * @param text the message that should be displayed
     */
    public static void upDateButton(Button changer, String text){
        changer.setText(text);
    }

    /**
     *
     * @param upDater the button to be updated
     *                make the button text bold and size it based on the users current screen
     */
    public static void setButtonDetails(Button upDater){
        upDater.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        upDater.setTextColor(Color.BLACK);
    }

    /**
     *
     * @param textView the text view that must be bolded
     *                 make that text view bold
     */
    public static void makeTextViewBold(TextView textView){
        textView.setTypeface(null, Typeface.BOLD);
    }


}