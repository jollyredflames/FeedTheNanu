package com.ramsy.GameCentre.MemoryMatrix;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryMatrixMovingActivity extends Activity implements View.OnClickListener {
    private DisplayMetrics displayMetrics;
    int screenHeight;
    int screenWidth;

    RelativeLayout test;
    int dim = 200;
    Handler handler = new Handler();
    ArrayList<Block> blocks = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setInstanceVariables();
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        test = new RelativeLayout(this);

        setContentView(test);


        View ballView_1 = new View(this);
        ballView_1.setBackgroundColor(Color.BLACK);
        ballView_1.setOnClickListener(this);
        ballView_1.setId(1);
        test.addView(ballView_1);
        RelativeLayout.LayoutParams other1 = new RelativeLayout.LayoutParams(200, 200);
        ballView_1.setLayoutParams(other1);
        Block block1 = new Block(ballView_1, 300, 500,dim,dim);



        View temp = new View(this);
        temp.setOnClickListener(this);
        RelativeLayout.LayoutParams other2 = new RelativeLayout.LayoutParams(200, 200);
        temp.setLayoutParams(other2);
        temp.setId(2);
        temp.setBackgroundColor(Color.BLACK);
        test.addView(temp);
        Block block2 = new Block(temp, 400, 1000,dim,dim);
        temp.setOnClickListener(this);

        View no = new View(this);
        no.setId(3);
        no.setOnClickListener(this);
        RelativeLayout.LayoutParams noP = new RelativeLayout.LayoutParams(200, 200);
        no.setLayoutParams(noP);
        no.setBackgroundColor(Color.BLACK);
        test.addView(no);
        Block block3 = new Block(no, 250, 1500,dim,dim);


        View person = new View(this);
        person.setId(4);
        person.setOnClickListener(this);
        RelativeLayout.LayoutParams personP = new RelativeLayout.LayoutParams(200, 200);
        person.setLayoutParams(personP);
        person.setBackgroundColor(Color.BLACK);
        test.addView(person);
        Block block4 = new Block(person, 100, 1500,dim,dim);

        blocks.add(block1);
        blocks.add(block2);
        blocks.add(block3);
        blocks.add(block4);

        Runnable update = new Runnable() {
            @Override
            public void run() {
                for (Block moves : blocks) {
                    for(Block other:blocks) {
                        if(moves != other) {
                            int x = moves.getX();
                            int xWidth = x + moves.getWidth();
                            int y = moves.getY();
                            int yHeight = y + moves.getHeight();
                            if (xWidth >= other.getX() && xWidth <= other.getX() + other.getWidth()) {
                                if (y >= other.getY() && y <= other.getY() + other.getHeight()) {
                                    moves.setRight(false);
                                    moves.setDown(true);
                                    other.setRight(true);
                                    other.setDown(false);
                                    moves.setX(moves.getX() - 1);
                                    moves.setY(moves.getY() + 1);
                                    other.setX(other.getX() + 1);
                                    other.setY(other.getY() - 1);
                                    shift(moves.getView(), moves.getX(), moves.getY());
                                    shift(other.getView(), other.getX(), other.getY());
                                    generateNewXAndY(moves);
                                    //generateNewXAndY(other);
                                } else if (yHeight >= other.getY() && yHeight <= other.getY() + other.getHeight()) {
                                    moves.setRight(true);
                                    moves.setDown(false);
                                    other.setRight(false);
                                    other.setDown(true);
                                    moves.setX(moves.getX() + 1);
                                    moves.setY(moves.getY() - 1);
                                    other.setX(other.getX() - 1);
                                    other.setY(other.getY() + 1);
                                    shift(moves.getView(), moves.getX(), moves.getY());
                                    shift(other.getView(), other.getX(), other.getY());
                                    generateNewXAndY(moves);
                                    //generateNewXAndY(other);
                                }
                            }  else if (x >= other.getX() && x <= other.getX() + other.getY()) {
                                if (y >= other.getY() && y <= other.getY() + other.getHeight()) {
                                    moves.setRight(true);
                                    moves.setDown(true);
                                    other.setRight(false);
                                    other.setDown(false);
                                    moves.setX(moves.getX() + 1);
                                    moves.setY(moves.getY() + 1);
                                    other.setX(other.getX() - 1);
                                    other.setY(other.getY() - 1);
                                    shift(moves.getView(), moves.getX(), moves.getY());
                                    shift(other.getView(), other.getX(), other.getY());
                                    generateNewXAndY(moves);
                                    //generateNewXAndY(other);
                                }   else if (yHeight >= other.getY() && yHeight <= other.getY() + other.getHeight()) {
                                    moves.setRight(false);
                                    moves.setDown(false);
                                    other.setRight(true);
                                    other.setDown(true);
                                    moves.setX(moves.getX() - 1);
                                    moves.setY(moves.getY() - 1);
                                    other.setX(other.getX() + 1);
                                    other.setY(other.getY() + 1);
                                    shift(moves.getView(), moves.getX(), moves.getY());
                                    shift(other.getView(), other.getX(), other.getY());
                                    generateNewXAndY(moves);
                                    //generateNewXAndY(other);
                                }
                            }
                        }
                        else{
                            generateNewXAndY(moves);
                        }

                        //generateNewXAndY(other);
                    }
                }

                handler.postDelayed(this, 1);
            }
        };
        handler.post(update);


    }

    private void generateNewXAndY(Block moves) {
        int x = moves.getX();
        int y = moves.getY();
        if (moves.getRight() && (x + 200 > screenWidth)) {
            moves.setRight(false);
            x = x-10;
        } else if (!moves.getRight() && (x <= 0)) {
            moves.setRight(true);
            x = x +10;
        }
        else if (moves.getRight()) {
            x = x + 10;
        } else if (!moves.getRight()) {
            x = x - 10;
        }
        if (moves.getDown() && (y + 225 > screenHeight)) {
            moves.setDown(false);
            y = y-20;
        } else if (!moves.getDown() && (y <= 0)) {
            moves.setDown(true);
            y = y+20;
        }
        else if (moves.getDown()) {
            y = y + 20;
        } else if (!moves.getDown()) {
            y = y - 20;
        }
        moves.setX(x);
        moves.setY(y);
        shift(moves.getView(), moves.getX(), moves.getY());
    }

    public void shift(View v, int x, int y) {
        v.setX(x);
        v.setY(y);
    }

    private void setInstanceVariables() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        //1080 x 1794
    }

    public void onClick(View v) {
        if (v.getId() == 1) {
            v.setBackgroundColor(Color.BLUE);
        }
        else if(v.getId() == 2){
            v.setBackgroundColor(Color.YELLOW);
        }
        else if(v.getId() == 3){
            v.setBackgroundColor(Color.RED);
        }
    }

}

