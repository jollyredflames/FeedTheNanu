package com.ramsy.GameCentre.SlidingTiles;

import android.view.View;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.SaveState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Encapsulation of all game related components.
 *     Stores all game data models and logic.
 *     MemoryMatrixActivity uses these components.
 *     This is an encapsulation with windows and entry points,
 *     As MemoryMatrixActivity needs access to a lot of it, some however, can be private, like the algorithms.
 *     So there is collaboration needed between this class and MemoryMatrixActivity,
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

class SlidingTileGame {


    Map<String, Point> positionMap = new HashMap<String, Point>();
    Point gap;
    int size;
    int numberOfMoves = 0;
    Stack<Move> movesStack = new Stack();
    int score; // the starting score.

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


    SlidingTileGame(SaveState s) {

        this.size = s.size;
        this.score = s.score;
        this.gap = s.positionMap.get((s.size * s.size) - 1);

        for (int i = 0; i < (s.size * s.size) - 1; i += 1) {
            String key = String.valueOf(i + 1);
            this.positionMap.put(key, s.positionMap.get(i));
        }
    }


    SlidingTileGame(int size) {

        // Set initial state
        this.gap = new Point(size - 1, size - 1);

        for (int i = 1; i < (size * size); i += 1) {
            // Determine coordinates using i
            int y = (i - 1) / size; // Row (y)
            int x = (i - 1) % size; // Column (x)
            this.positionMap.put(String.valueOf(i), new Point(x, y));
        }

        this.size = size;

        this.score = this.startingScore();

    }

    /**
     * Save this game
     * @return SaveState representing this game
     */
    SaveState save() {
        // Returns a SaveState object configured to represent the game in its current state.

        return new SaveState(this.positionMap, this.gap, this.score, this.size);
    }

    /**
     * pause this game
     */
    void pause() {
        this.timer.cancel();

    }

    /**
     * resume this game
     */
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

    /**
     * score decreases as time increases. This sets initial score proportional to size.
     * @return int representing starting score
     */
    private int startingScore() {
        // Algorithm to set the starting score based on game size.

        double n = Math.pow((double) this.size, 4);
        return 10000 + (int) n * 20;
    }

    /**
     * Based on complexity of puzzle, more iterations of the scrambler must occur to get a good mix
     * @return int representing how many times scramble should run
     */
    private int scrambleAmount() {
        // Algorithm that uses game size to determine how many moves to scramble by.
        return this.size * this.size * 10; // just a constant factor
//        return 4;
    }

    /**
     * method to move pieces simulating a touchEvent
     */
    void scramble() {

        int n = this.scrambleAmount();

        for (int i = 1; i <= n; i += 1) {

            List<Point> adjacentPoints = this.gap.adjacentPointsInsideSquareOfSize(this.size);

            // Get random element
            int length = adjacentPoints.size();
            int j = (int) (Math.random() * length);
            Point randomPoint = adjacentPoints.get(j);

            Integer id = 0;

            // Find the element that corresponds to randomPoint
            for (Map.Entry<String, Point> entry : this.positionMap.entrySet()) {
                String key = entry.getKey();
                Point value = entry.getValue();

                if (value.equals(randomPoint)) {

                    id = Integer.valueOf(key);
                    break;
                }
            }

            // Now modify the data model.

            Point temp = this.gap;
            this.gap = randomPoint;
            this.positionMap.put(String.valueOf(id), temp);

        }
    }


    /**
     * Return valid moves for this View Point object
     * @param v the tapped view point object
     * @return Moves available to view
     */
    Move moveFor(View v) {
        // Return a Move for id, if there is a valid move possible,
        // Otherwise return null.
        // Keep in mind this class does not store any Views.
        // It received a view in this method, and gets its id,
        // Returns the view back packaged into a Move object, or null.

        Point c = this.positionMap.get(String.valueOf(v.getId()));
        Point gap = this.gap;
        if (c.isAdjacentTo(gap)) {
            Direction d = c.directionOf(this.gap);

            // Now we have a View ID, and a Direction.
            // Create a Move from those.

            Move m = new Move(v, d);
            return m;
        } else {
            return null;
        }
    }

    /**
     * given move m, make the move.
     * bool forward checks if the move is occuring due to an 'undo' in which case these moves wont decrease points.
     * @param m a valid move
     * @param forward bool representing whether these moves are occuring due to undo or not
     */
    void makeMove(Move m, boolean forward) {
        // Update the model with the move passed in.
        // Whenever the View's are rearranged, by performing a move,
        // the model needs to be changed to reflect that new state.
        // However, the model also needs to be changed when an undo is performed (since view's are being rearranged)
        // But in those cases, we shouldn't increment moves (we should decrement it) and we should append the move to the moves stack.

        // If forward is true, increment number of moves and append to moves stack.
        // If false, decrement number of moves and don't append to moves stack.

        View v = m.v;
        Point p = this.positionMap.get(String.valueOf(v.getId()));

        int oldX = p.x;
        int oldY = p.y;

        p.x = this.gap.x;
        p.y = this.gap.y;

        this.gap.x = oldX;
        this.gap.y = oldY;

        if (forward) {
            this.numberOfMoves += 1;
            this.score -= 50;
            this.movesStack.push(m);

            //TODO: Talk about this in presentation
            // Make this class responsible for checking if the game has been won,
            // only if the gap is in the right position (for efficiency)

            Point bottomCorner = new Point(this.size - 1, this.size - 1);
            if (this.gap.equals(bottomCorner) && this.isComplete()) {
                // Inform the delegate
                this.pause();
                this.delegate.didComplete();
            }


        } else {
            this.numberOfMoves -= 1;
            this.score += 50;
        }
    }

    /**
     * Check if the user has solved the puzzle
     * @return true if puzzle solved, else return false.
     */
    private boolean isComplete() {
        // Returns true if the game is completed (has been won).

        // Loop over the positionsMap

        for (Map.Entry<String, Point> entry : this.positionMap.entrySet()) {
            String key = entry.getKey();
            Point value = entry.getValue();

            int i = Integer.valueOf(key);
            int y = (i - 1) / this.size; // Row (y)
            int x = (i - 1) % this.size; // Column (x)
            Point p = new Point(x, y);

            if (!value.equals(p)) {
                return false;
            }
        }

        return true;
    }


}
