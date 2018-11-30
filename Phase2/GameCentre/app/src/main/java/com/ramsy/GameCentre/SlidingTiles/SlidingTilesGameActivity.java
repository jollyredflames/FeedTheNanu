package com.ramsy.GameCentre.SlidingTiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.SaveState;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;
import com.ramsy.GameCentre.GameCentreCommon.FinishedGameActivity;
import com.ramsy.GameCentre.GameCentreCommon.NewOrSavedGame;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SlidingTilesGameActivity extends AppCompatActivity implements View.OnClickListener, SlidingTileGameDelegate {


    // For customization of where the board starts vertically
    int topMargin = 300;

    // Store a reference to the root view group
    RelativeLayout container;
    TextView scoreLabel;

    // Need to get these from previous Activity:
    // Hardcoded for now
    int size;
    Bitmap image; // if null, use numbers; otherwise use image.
    int slot; // for new games.
    int defaultUndoAmount = 3;

    private User meUser;

    View backgroundView;

    SlidingTileGame slidingTileGame;

    boolean userInteractionDisabled = false;

    TextView pauseButton;

    RelativeLayout pausedScreen;

    Timer autosaveTimer;

    int autosaveInterval = 10000; // 10 seconds

    TextView defaultUndoAmountLabel;

    String gameName = "SlidingTiles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        meUser = FirebaseFuncts.getUser();

        // Extract this information from the intent
        // size
        // slot

        // Extract this from elsewhere (a public static property)
        // image (may be nil)
        // save state (may be nil)

        // Call the method that returns a save for a slot index.

        /*
        If the save state is not null, this is a loaded game.
        Load from this save state and ignore all other properties.
        Except of course the slot, since a slot is needed to know where to save, whether it is a new game or
        a loaded game.

        If the save state is null, this is a new game.
        If the image is not null, this will be a picture game, otherwise it'll be a numbers game.

         */

        Bundle b = getIntent().getExtras(); // this is how we get data from the intent that started this activity.
        int size = b.getInt("size");
        int slot = b.getInt("slot");
        Bitmap image = SlidingTilesSizeActivity.image;
        SaveState save = meUser.getGame(gameName, slot);

        System.out.println("XXX  size" + String.valueOf(size));
        System.out.println("XXX  slot" + String.valueOf(slot));

        this.image = image;
        this.slot = slot; // assign slot in all cases. Need to know where to save!

        if (save != null) {
            System.out.println("XXX  " + "Save exists!!");
            // This will be a Loaded Game

            // Create the game instance from the save
            // Assign size value from the save state

            SlidingTileGame game = new SlidingTileGame(save);
            this.size = save.size;

            game.delegate = this;
            this.slidingTileGame = game;

        } else {
            System.out.println("XXX  " + "Save does not exist");
            // This will be a New Game

            // Assign size
            // Create a new game
            this.size = size;
            SlidingTileGame game = new SlidingTileGame(this.size);

            game.delegate = this;
            this.slidingTileGame = game;
            game.scramble();


        }


        // Create a View Group
        RelativeLayout container = new RelativeLayout(this);
        this.container = container;

        // Set the Activity's window to the View Group
        setContentView(container);

        // Create a background view
        View v = new View(this);
        v.setBackgroundColor(Color.BLUE);
        container.addView(v);
        this.backgroundView = v;

        this.setupScoreLabel();

        this.setupUndoButton();

        this.createTiles();

        this.setupPauseButton();


        this.slidingTileGame.resume();

        // start autosaving.
        this.startAutoSaving();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.stopAutoSaving(); // important, otherwise the timer keeps running
    }

    private void setupScoreLabel() {
        // Setup Score label
        TextView label = new TextView(this);
        label.setText("0");
        label.setTextColor(Color.WHITE);
        label.setTextSize(30);
        label.setBackgroundColor(Color.RED);
        label.setGravity(Gravity.CENTER);
        this.container.addView(label);
        this.scoreLabel = label;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0,100);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.setMargins(0, 0, 0, 0);
        label.setLayoutParams(params);
    }

    private void setupPauseButton() {
        // Pause Button
        TextView pauseView = new TextView(this);
        pauseView.setText("Pause");
        pauseView.setTextColor(Color.BLACK);
        pauseView.setBackgroundColor(Color.WHITE);
        pauseView.setId(-2);
        pauseView.setOnClickListener(this);
        pauseView.setGravity(Gravity.CENTER);
        this.container.addView(pauseView);

        RelativeLayout.LayoutParams pauseParams = new RelativeLayout.LayoutParams(200, 100);
        pauseParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pauseParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        pauseView.setLayoutParams(pauseParams);

        this.pauseButton = pauseView;
    }

    private void setupUndoButton() {
        // Undo Button
        // Using a text view because a Button auto capitalizes its text for some reason.
        TextView undoView = new TextView(this);
        undoView.setText("Undo");
        undoView.setTextColor(Color.BLACK);
        undoView.setBackgroundColor(Color.WHITE);
        undoView.setId(-1);
        undoView.setOnClickListener(this);
        undoView.setGravity(Gravity.CENTER);
        this.container.addView(undoView);

        RelativeLayout.LayoutParams undoParams = new RelativeLayout.LayoutParams(200, 100);
        undoParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        undoParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        undoView.setLayoutParams(undoParams);
    }


    private int[] createHues() {
        // Remember that colours are actually ints.

        int n = this.size;
        float hue = 0.2f; // start hue
        float hueDelta = (1.0f - hue) / (n * n); // end hue is 1.0

        // There will be n^2 - 1 tiles, so there should be that many colours.
        int[] colors = new int[(n * n) - 1];

        for (int i = 0; i < colors.length; i += 1) {
            int color = Color.rgb(0, hue, 0);
            colors[i] = color;
            hue += hueDelta;

            // Create a random color
//            int color = Color.rgb((float) Math.random(), (float) Math.random(), (float) Math.random());
        }

        return colors;

    }


    private void createTiles() {

        // NEW:
        // Create tiles and position them.
        // Need to have access to game size (for hue shades) and the hashmap.
        // You can create a hue list first, and then create and position tiles in one loop,
        // indexing on the hue list so that when sorted, the hue shades are ordered.

        // Get the screen width
        // Divide it by the number of tiles that need to fit.
        // This is the width of each tile.
        // Let it also be the height of each tile.
        // Declare a Top Margin value (this will be the top of View collection; where the board starts vertically)


        // Use padding to correctly place the views based on their position.
        // So instead of laying out views relative to their above sibling or whatever,
        // position them all to hug the parent's top left, and assign different paddings with a multiplier using their
        // positions from the model.


        int w = this.screenWidth() / this.size; // truncates

        int[] colors = new int[0];
        Bitmap[] slices = new Bitmap[0];

        if (this.image == null) {
            // Numbers mode
            colors = this.createHues();

        } else {
            // Picture mode
            Bitmap scaled = ImageUtils.scaleImageToFitInsideSquareOfSideLength(this.image, this.screenWidth());
            slices = ImageUtils.sliceImageIntoGridOfSideLength(scaled, this.size);

        }


        for (Map.Entry<String, Point> entry : this.slidingTileGame.positionMap.entrySet()) {
            String key = entry.getKey();
            Point point = entry.getValue();

            // Create a new View Group
            RelativeLayout con = new RelativeLayout(this);
            con.setId(Integer.valueOf(key));
            this.container.addView(con);

            if (image == null) {
                // Numbers mode

                // Create a new View
                View v = new View(this);
                v.setBackgroundColor(colors[Integer.valueOf(key) - 1]);

                // Create a Text View
                TextView label = new TextView(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w ,w);
//        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                label.setLayoutParams(params);

                label.setText(key);
                label.setTextColor(Color.WHITE);
                label.setTextSize(30);
//            label.setBackgroundColor(Color.BLACK);
                label.setGravity(Gravity.CENTER);

                con.addView(v);
                con.addView(label);


            } else {
                // Picture mode

                // Create an Image View
                ImageView imv = new ImageView(this);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w ,w);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                imv.setLayoutParams(params);

                imv.setImageBitmap(slices[Integer.valueOf(key) - 1]);

                con.addView(imv);




            }

            // Listen to taps
            con.setOnClickListener(this);


            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, w);
            p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            p.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            // Set a Margin for these Views
            // You can adjust the Top Margin value to move the whole View collection vertically
            p.setMargins(point.x * w, this.topMargin + (point.y * w), 0, 0);
            con.setLayoutParams(p);
        }

    }


    private int screenWidth() {
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        return display.widthPixels;
    }


    @Override
    public void onClick(View v) {

        if (this.userInteractionDisabled) {
            return;
        }

        if (v.getId() == -1) {
            this.undo(this.defaultUndoAmount);
            return;
        }

        if (v.getId() == -2) {
            this.pauseTapped();
            return;
        }

        // Quit button
        if (v.getId() == 1000) {
            this.quitPressed();
            return;
        }

        if (v.getId() == -999) {
            if (this.defaultUndoAmount == 1) {
            } else {
                this.defaultUndoAmount -= 1;
                this.defaultUndoAmountLabel.setText(String.valueOf(this.defaultUndoAmount));
            }
            return;
        }


        if (v.getId() == 999) {
            if (this.defaultUndoAmount == 10) {
            } else {
                this.defaultUndoAmount += 1;
                this.defaultUndoAmountLabel.setText(String.valueOf(this.defaultUndoAmount));
            }
            return;
        }


        Move m = this.slidingTileGame.moveFor(v);
        if (m == null) {
            return;
        }

        this.performMove(m, true, 300, null);

    }

    private void pauseTapped() {
        if (this.pauseButton.getText().equals("Pause")) {
            // Pause the game
            this.pauseButton.setText("Resume");
            this.slidingTileGame.pause();
            this.showPausedScreen();

            // stop autosaving
            this.stopAutoSaving();


        } else {
            // Resume the game
            this.pauseButton.setText("Pause");
            this.slidingTileGame.resume();
            this.container.removeView(this.pausedScreen);

            // start autosaving
            this.startAutoSaving();

        }
    }

    void startAutoSaving() {

        Timer t = new Timer();
        this.autosaveTimer = t;
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                save();
            }
        }, this.autosaveInterval, this.autosaveInterval);

    }

    void stopAutoSaving() {
        this.autosaveTimer.cancel();
    }

    void save() {
        SaveState s = this.slidingTileGame.save();
        int slot = this.slot;

        // Throw this to the backend function
        meUser.saveGame("SlidingTiles", s, slot);

    }

    //TODO:check for == should be equals()
    // TODO: use refactoring for views

    void undo(int i) {
        // When the undoView is tapped, it'll call this function with i being the default number of moves to undo (3).

        // Get a Move from the stack
        // Reverse it's Direction.
        // Call a function with this move.


        if (this.slidingTileGame.movesStack.isEmpty() || i == 0) {
            this.userInteractionDisabled = false;
            return;
        }

        this.userInteractionDisabled = true;
        Move m = this.slidingTileGame.movesStack.pop();
        m.reverse();
        this.performMove(m, false, 100, () -> this.undo(i - 1)); // if this function takes in a closure, and calls it at the end of the animation,
        // that provides a chance to chain another call to undo, this time with i - 1.
        // We simply pass that closure to the animation to call when it ends.

    }

    void performMove(Move m, boolean forward, int duration, Runnable completionHandler) {
        // Take in a Move (a View and a Direction).
        // Animate the View to that Direction.
        // Update the View's coordinates to the gap, in the data model.
        // Update the gap's coordinates to the View's previous coordinates.
        // Atomize animating a move, and updating the data model.

        View v = m.v;
        Direction d = m.direction;

        Point gap = this.slidingTileGame.gap;


        ViewPropertyAnimator anim = v.animate().setDuration(duration).setInterpolator(new DecelerateInterpolator());
        if (completionHandler != null) {
            anim.withEndAction(completionHandler);
        }

        // Use the view's current Coordinates to determine where to animate it.
        // Don't animate with deltas, as taps mid-animation will spoil the structure.
        // So animate to absolute positions

        if (d.isHorizontal()) {
            // gap.x is a value in the range [0, n-1]
            // Multiple gap.x by the width of the View; that's where to animate it to.
            int newX = gap.x * v.getWidth();
            anim.x(newX);

        } else {

            int newY = gap.y * v.getWidth() + this.topMargin;
            anim.y(newY);

        }

        // Tell the Game object to make this move.
        this.slidingTileGame.makeMove(m, forward);

    }

    void quitPressed() {

        // Save the game manually once
        // Transition somewhere

        this.save();
//        this.finish();

        Intent n = new Intent(this, NewOrSavedGame.class);
        startActivity(n);


    }

    @Override
    public void scoreDidChange(int newScore) {
        this.scoreLabel.setText(String.valueOf(newScore));
    }

    @Override
    public void didComplete() {
        this.backgroundView.setBackgroundColor(Color.GREEN);
        this.userInteractionDisabled = true;

        // stop autosaving
        this.stopAutoSaving();

        // delete slot?

        // Transition to finish screen activity in 1 second.
        Intent n = new Intent(this, FinishedGameActivity.class);
        n.putExtra("gameScore", scoreLabel.getText());

        String s = String.valueOf(this.size);

        n.putExtra("gameName", s + "x" + s);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(n);
            }
        }, 1000);

    }

    private void showPausedScreen() {

        // Create a View Group
        RelativeLayout con = new RelativeLayout(this);
        this.pausedScreen = con;

        // Add a dimming view
        View d = new View(this);
        d.setBackgroundColor(Color.BLACK);
        d.setAlpha(0.8f);
        con.addView(d);

        // Add the Quit Button
        Button b = new Button(this);
        b.setId(1000);
        b.setText("Quit");
        b.setGravity(Gravity.CENTER);
        b.setOnClickListener(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400, 200);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        b.setLayoutParams(params);
        con.addView(b);


        // Default Undo Amount Text View
        // Setup Score label
        TextView label = new TextView(this);
        label.setText(String.valueOf(this.defaultUndoAmount));
        label.setTextColor(Color.WHITE);
        label.setId(1234);
        label.setTextSize(100);
//        label.setBackgroundColor(Color.RED);
        label.setGravity(Gravity.CENTER);
        con.addView(label);
        this.defaultUndoAmountLabel = label;

        RelativeLayout.LayoutParams undoLabelParams = new RelativeLayout.LayoutParams(300,300);
        undoLabelParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        undoLabelParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        undoLabelParams.setMargins(0, 300, 0, 0);
        label.setLayoutParams(undoLabelParams);

        // Decrement Button
        TextView decrementButton = new TextView(this);
        decrementButton.setText("-");
        decrementButton.setTextColor(Color.BLACK);
        decrementButton.setBackgroundColor(Color.WHITE);
        decrementButton.setId(-999);
        decrementButton.setOnClickListener(this);
        decrementButton.setGravity(Gravity.CENTER);
        con.addView(decrementButton);

        RelativeLayout.LayoutParams decrementButtonParams = new RelativeLayout.LayoutParams(100, 100);
        decrementButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, 1234);
        decrementButtonParams.addRule(RelativeLayout.LEFT_OF, 1234);
        decrementButtonParams.setMargins(0, 0, 100, 0);
        decrementButton.setLayoutParams(decrementButtonParams);

        // Increment Button
        TextView incrementButton = new TextView(this);


        incrementButton.setText("+");
        incrementButton.setTextColor(Color.BLACK);
        incrementButton.setBackgroundColor(Color.WHITE);
        incrementButton.setId(999);
        incrementButton.setOnClickListener(this);
        incrementButton.setGravity(Gravity.CENTER);
        con.addView(incrementButton);

        RelativeLayout.LayoutParams incrementButtonParams = new RelativeLayout.LayoutParams(100, 100);
        incrementButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, 1234);
        incrementButtonParams.addRule(RelativeLayout.RIGHT_OF, 1234);
        incrementButtonParams.setMargins(100, 0, 0, 0);
        incrementButton.setLayoutParams(incrementButtonParams);


        // Undo Instruction Label
        TextView instructionLabel = new TextView(this);
        instructionLabel.setText("Default Undo Amount");
        instructionLabel.setTextColor(Color.WHITE);
        instructionLabel.setTextSize(20);
//        instructionLabel.setBackgroundColor(Color.RED);
        instructionLabel.setGravity(Gravity.CENTER);
        con.addView(instructionLabel);

        RelativeLayout.LayoutParams instructionLabelParams = new RelativeLayout.LayoutParams(500,100);
        instructionLabelParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        instructionLabelParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        instructionLabelParams.setMargins(0, 150, 0, 0);
        instructionLabel.setLayoutParams(instructionLabelParams);

        // Add it below the pause button
        int i = this.container.indexOfChild(this.pauseButton);
        this.container.addView(con, i);
    }

    void setUpTextButton(TextView view, String text, Color textColor, Color background, int id, boolean listener, Gravity gravity){

    }

}
