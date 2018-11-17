package com.ramsy.slidingtiles;

import android.graphics.Color;
import android.view.View;

import java.util.*;

/**
 * Encapsulation of all game related components.
 *     Stores all game data models and logic.
 *     GameActivity uses these components.
 *     This is an encapsulation with windows and entry points,
 *     As GameActivity needs access to a lot of it, some however, can be private, like the algorithms.
 *     So there is collaboration needed between this class and GameActivity,
 *     Since that is the class that the user interacts with, and that displays visuals that correspond to the game state.
 *
 *     - ID, Point Hashmap (a hashmap that stores tile id's with their location)
 *     - Gap location (a point that stores the location of the gap)
 *     - Number of moves
 *     - Scramble algorithm
 *     - Game end checking algorithm
 *     - A stack of Moves (a View ID and a Direction) for undo functionality
 *     - Size
 *
 */

//TODO: MAKE SaveState constuctor with: int Score, int size. int mistakes/lives, int undo
//TODO: Make undo so that it increments lives and changes color of the last tile clicked to neutral color
//TODO:
class MemoryGame {


    Map<String, Point> positionMap = new HashMap<String, Point>();
    int size;
    Stack<Move> wrongStack = new Stack();
    int score; // the starting score.
    ArrayList<Boolean> pointsClicked;
    Set<Integer> highlightedPoints;

    Timer timer;

    SlidingTileGameDelegate delegate;

    // Have a timer routinely decrement the score by an amount
    // And call a delegate method, passing the value of the new score.
    // The delegate will implement that method to receive the score, and
    // use it to update a label's text.
    // When a move is made, the score will decrement / increment if it was an undo,
    // and the delegate method will get called again.

    // Game will have pause and resume methods which will control the timer.

    // Should undoing a move decrement the number of moves? Yea


    // Need a function that can create and return a SaveState based on this instance.
    // Also need an init that can create a new instance from a SaveState.


    MemoryGame(SaveState s) {

        this.size = s.size;
        this.score = s.score;
        for (int i = 0; i < (s.size * s.size) - 1; i += 1) {
            String key = String.valueOf(i + 1);
            this.positionMap.put(key, s.positionMap.get(i));
            pointsClicked.add(false);
        }

    }


    MemoryGame(int size) {

        int highlightMax = size;
        for (int i = 1; i < (size * size); i += 1) {
            // Determine coordinates using i
            int y = (i - 1) / size; // Row (y)
            int x = (i - 1) % size; // Column (x)
            this.positionMap.put(String.valueOf(i), new Point(x, y));
            pointsClicked.add(false);
        }

        this.size = size;

        this.score = this.startingScore();

    }


    SaveState save() {
        // Returns a SaveState object configured to represent the game in its current state.

        return new SaveState(this.positionMap, this.positionMap.get("1"),this.score, this.size);
    }

    //TODO: Take Out pause and Resume, make sure to take out the view's related too.
    void pause() {
        this.timer.cancel();

    }

    void resume() {
        // Start a timer that decrements the score and calls a delegate method.

        Timer t = new Timer();
        this.timer = t;
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                score -= 1;
                delegate.scoreDidChange(score);
            }
        }, 0, 30); // with a starting score of 20,000, decrementing by 1 every call, that gives 400 seconds to finish the game.
        // So make larger game sizes have a higher starting score.


    }

    private int startingScore() {
        // Algorithm to set the starting score based on game size.

        double n = Math.pow((double) this.size, 4);
        return 10000 + (int) n * 20;
    }


    void setViewstoClick() {
        MemoryMatrixRandomizer randomPoints = new MemoryMatrixRandomizer(this.size);
        this.highlightedPoints = randomPoints.randomizer();
    }



    void viewClicked(View v) {
        int clickedOn = v.getId();
        if(highlightedPoints.contains(clickedOn)){
            v.setBackgroundColor(Color.GREEN);
        }
        else{
            v.setBackgroundColor(Color.RED);
        }

    }

    private boolean isComplete() {
        for(Integer mustClick :this.highlightedPoints){
            if(!pointsClicked.get(mustClick)){
                return false;
            }
        }
        return true;
    }
}
