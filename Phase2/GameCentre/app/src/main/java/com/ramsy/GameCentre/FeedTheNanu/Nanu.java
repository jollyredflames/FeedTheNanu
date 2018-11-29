package com.ramsy.GameCentre.FeedTheNanu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.widget.ImageView;

import com.ramsy.GameCentre.R;

import java.util.HashMap;

/**
 * A class that represents the game creature, called a 'Nanu'.
 * This class encapsulates the animation state machine logic,
 * and holds all required textures as preloaded caches,
 * with all textures for a specific animation being held in a separate cache.
 * The game loop sets the foodIsNearby property, which is required to determine
 * which animation to play, and which texture within that animation needs displaying.
 * The class also encapsulates a State enum that represents the internal states that are needed
 * in conjunction with the externally set foodIsNearby property.
 *
 * Responsible for eating items. Items that are determined to be within eating range by the game loop,
 * will be sent as an Edible type, and the effects of eating that item will be determined in this class.
 * Eating bad stuff will decrement the Nanu's lives and send a damage message to the delegate (which could flash
 * the screen red). Eating special stuff will increment the Nanu's lives, and send a health increase message
 * to the delegate. Eating normal items will send a different message to the delegate, so score can be managed.
 *
 *
 * This class subclasses ImageView, making it like a sprite node.
 * Create an instance, lay it out, add it to a view, set its delegate, and call start.
 * Set it's foodIsNearby property in the game loop (based on intersection logic between the falling items and the Nanu's
 * trigger zone)
 * Call eat in the game loop (based on whether the Nanu's mouth zone contains the midpoint of a falling item),
 * passing that Edible instance in that method call to process effects, which will in turn call delegate methods.
 *
 * IDEAS:
 *
 * We can increase the speed temporarily when coffee is consumed haha.
 * That will increase the speed of all the Nanu's animations; his idle bouncing animation,
 * chewing animation, etc, effectively increasing the speed the Nanu can eat, like a boost.
 */


class Nanu extends ImageView implements Pausable {

    /**
     * This enum encapsulates all the states of the Nanu character.
     * They correspond to an animation for which there are textures for, as
     * well as semantic end points within those animations.
     * These are used internally, and in combination with the foodIsNearby property,
     * they propvide all that is needed to control animation.
     */


    enum State {

        IDLING,
        OPENING_MOUTH,
        MOUTH_FULLY_OPENED,
        CLOSING_MOUTH,
        MOUTH_FULLY_CLOSED,
        LICKING_LIPS,
        FINISHED_LICKING_LIPS,
        CHEWING,
        FINISHED_CHEWING;
    }


    /**
     * to be set in the game loop, when an edible item is in the trigger zone.
     * It is set to true or false in every frame of the game.
     */

    boolean foodIsNearby = false;


    /**
     * to be set in the game loop, when an edible item is in the mouth zone.
     * It is set to an Edible or null in every frame of the game.
     */

    Edible foodItemForEating = null;


    /**
     * time in milliseconds between each texture. Use a smaller number to speed up the Nanu.
     * Useful for when coffee is consumed.
     */

    private long speed = 50;


    /**
     * Holds the current state. Starts at idling.
     */

    private State state = State.IDLING;

    // Textures
    private HashMap<String, Bitmap> idleTextures;
    private HashMap<String, Bitmap> mouthOpeningTextures;
    private HashMap<String, Bitmap> mouthClosingTextures;
    private HashMap<String, Bitmap> lipLickingTextures;
    private HashMap<String, Bitmap> chewingTextures;



    /**
     * The zone that an Edible needs to be within for the Nanu to preemptively open his mouth.
     * The game loop will access this rect and determine if any of the falling Edibles intersect it.
     */

    Rect getProximityZone() {
        // Returns a rect representing the Nanu's trigger zone, in the screen's reference frame,

        Rect r = RectUtility.boundingBox(this);
        int padding = 100; // pad by this amount on the left, top and right (not the bottom)
        RectUtility.padRect(r, new int[] {padding, padding, padding, 0});

        return r;
    }


    /**
     * The zone that an Edible needs to be within for the Nanu to eat it.
     * Needs to be tuned to match the mouth region of the mouth fully opened texture.
     * The game loop will access this rect and determine if any of the falling Edibles intersect it.
     *
     */

    Rect getMouthZone() {
        Rect r = RectUtility.boundingBox(this);
        int padding = -50; // pad by this amount on the left, right, top, and bottom
        // we pad the top so the item needs to fall within the mouth a bit before it's eaten.
        RectUtility.padRect(r, new int[] {padding, padding, padding, padding});

        return r;
    }


    /**
     * The amount of life the Nanu currently has.
     * Eating bad stuff will decrease this amount.
     * Eating some special stuff will increase it.
     */


    private float currentLife;


    /**
     * The maximum life the Nanu can have.
     */

    static float maxLife = 100f;

    /**
     * @return The current life as a % of the max life.
     * This will be a float between 0 and 1 inclusive.
     */

    float getCurrentLifePercent() {
        return currentLife / Nanu.maxLife;
    }


    /**
     * The delegate of the Nanu (preferably GameActivity) that will be sent the delegate messages
     * at certain important times.
     */

    NanuDelegate delegate;



    Nanu(Context context) {
        super(context);

        this.currentLife = Nanu.maxLife;
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.nanu1);
        this.setImageBitmap(image);

        this.loadIdleTextures();
        this.loadMouthOpeningTextures();
        this.loadMouthClosingTextures();
        this.loadLipLickingTextures();
        this.loadChewingTextures();

    }


    /**
     * Pauses the animation.
     * Call start at any later time to resume without any loss of continuity in the state machine
     */

    public void pause() {

        handler.removeCallbacks(update);
    }


    /**
     * An index that keeps track of which texture to show.
     * Is incremented / reset to 1 by the animation timer.
     */

    private int counter = 1;


    /**
     * An index that keeps track of how many times a chewing animation cycle has played.
     * Is incremented / reset to 0 by the animation timer.
     */

    private int timesChewed = 0;


    /**
     * The number of chews required before an item is fully chewed.
     * Used by the animation timer to loop a portion of the chewing animation.
     * Use a smaller number to take up less time chewing.
     */

    private int chewsRequired = 1;



    Handler handler = new Handler();

    private Runnable update = new Runnable() {
        @Override
        public void run() {

            // Animation state machine logic goes here

                /*
                There are 4 animations:
                - Idle animation
                - Mouth opening animation
                - Mouth closing animation
                - Lick licking animation
                - Chewing animation (not yet done)

                The idle animation plays on loop.
                The mouth opening animation is proximity activated. At which point the mouth opening animation runs,
                and stays at it's end texture as long as a to-be-eaten item remains in the trigger radius.
                From here there are two paths. Entering the mouth triggers the chewing animation.
                Leaving the proximity zone triggers the mouth close animation, which is a mouth close that
                segues to a sad face, and a lip lick (together known as the lip licking animation),
                which then segues into the start of the idle animation

                 */

            if (state == State.IDLING && !foodIsNearby) {
                // Keep idling
                // Get the next idle texture
                // Wrap the counter to start index if its past end index for idle textures
                counter += 1;
                if (counter > 19) {
                    counter = 1;
                }

                Bitmap im = idleTextures.get(String.valueOf(counter));
                setImageBitmap(im);



            } else if (state == State.IDLING && foodIsNearby) {
                // No more idling
                // Reset counter
                // Get the first opening mouth texture
                // Set state to opening mouth
                counter = 1;

                Bitmap im = mouthOpeningTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                state = State.OPENING_MOUTH;


            } else if (state == State.OPENING_MOUTH) {
                // Keep opening mouth until fully opened
                // Get the next opening mouth texture
                // if the index is the end index, set state to mouth fully opened

                counter += 1;

                Bitmap im = mouthOpeningTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                if (counter == 9) {
                    state = State.MOUTH_FULLY_OPENED;
                }

            } else if (state == State.MOUTH_FULLY_OPENED && foodItemForEating != null) {
                // Start chewing
                // Reset counter
                // Get the first chewing texture
                // Set state to chewing
                // Process edible (which will also alert the delegate)

                counter = 1;

                Bitmap im = chewingTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                state = State.CHEWING;

                processEdible(foodItemForEating);

            } else if (state == State.CHEWING) {
                // Keep chewing until fully chewed
                // Get the next chewing texture
                // Wrap the counter to a specific index if its past the end index,
                // and increment the timesChewed counter which starts at 0.
                // if timesChewed is 1 less than chewsRequired and the index is the last index,
                // set state to finished chewing.

                counter += 1;

                if (counter > 13) {
                    counter = 5;
                    timesChewed += 1;
                }

                if (timesChewed == (chewsRequired - 1) && counter == 13) {
                    state = State.FINISHED_CHEWING;
                    timesChewed = 0;
                }

                Bitmap im = chewingTextures.get(String.valueOf(counter));
                setImageBitmap(im);


            } else if (state == State.FINISHED_CHEWING) {
                // Reset counter
                // Get the first mouth opening texture
                // Set state to opening mouth

                counter = 1;

                Bitmap im = mouthOpeningTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                state = State.OPENING_MOUTH;

            } else if (state == State.MOUTH_FULLY_OPENED && foodIsNearby) {
                // Do nothing

            } else if (state == State.MOUTH_FULLY_OPENED && !foodIsNearby) {
                // No more mouth opened
                // Reset counter
                // Get the first mouth closing texture
                // Set state to mouth closing

                counter = 1;

                Bitmap im = mouthClosingTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                state = State.CLOSING_MOUTH;


            } else if (state == State.CLOSING_MOUTH) {
                // Keep closing mouth until fully closed
                // Get the next closing mouth texture
                // if the index is the end index, set state to mouth fully closed

                counter += 1;

                Bitmap im = mouthClosingTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                if (counter == 5) {
                    state = State.MOUTH_FULLY_CLOSED;
                }


            } else if (state == State.MOUTH_FULLY_CLOSED) {
                // Reset counter
                // Get the first lip licking texture
                // set state to licking lips

                counter = 1;

                Bitmap im = lipLickingTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                state = State.LICKING_LIPS;


            } else if (state == State.LICKING_LIPS && !foodIsNearby) {
                // Keep licking lips
                // Get the next lip licking texture
                // If the index is the end index, set state to finished licking lips

                counter += 1;

                Bitmap im = lipLickingTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                if (counter == 25) {
                    state = State.FINISHED_LICKING_LIPS;
                }

            } else if (state == State.LICKING_LIPS && foodIsNearby) {
                // Stop licking lips
                // Reset counter
                // Get the first opening mouth texture
                // Set state to opening mouth

                counter = 1;

                Bitmap im = mouthOpeningTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                state = State.OPENING_MOUTH;


            } else if (state == State.FINISHED_LICKING_LIPS) {
                // Reset counter
                // Get the first idling texture
                // Set state to idling

                counter = 1;

                Bitmap im = idleTextures.get(String.valueOf(counter));
                setImageBitmap(im);

                state = State.IDLING;

            }



            // Recursion
            handler.postDelayed(this, speed);
        }
    };



    /**
     * Starts the animation system, essentially making the Nanu 'active'
     */

    public void resume() {
        // Start a new timer that controls which texture to display
        handler.post(update);

    }


    /**
     * Process the eating of an Edible item.
     * The Game Activity or other controller needs to call this method, passing in
     * the Edible to be eaten. It's the responsibility of that class to ensure the item
     * is in range to be eaten visually, as regardless, the following will happen:
     * The Nanu will start a chewing animation, and process the effects of the Edible.
     * It is also the responsibility of the other class to manage the Edible by removing it
     * from the view hierarchy, or recycling it.
     * @param item The Edible to be eaten
     */

    private void processEdible(Edible edible) {

        // Alert delegate if life changes (passing in the amount it changed by)
        // Alert delegate if the score should change (pass in the amount to change the score by)
        // No need to alert delegate if the speed changes, as that is used only in this class.

        // TODO:
        delegate.aboutToEat(edible);
        delegate.scoreShouldChangeBy(edible.effectOnScore());

        if (edible.effectOnSpeed() != 1) {
            this.speed /= edible.effectOnSpeed();

        }



    }




    // Texture Loading

    /**
     * Load and store all the idle animation textures
     */

    private void loadIdleTextures() {
        this.idleTextures = new HashMap<>();
        this.idleTextures.put("1", BitmapFactory.decodeResource(getResources(), R.drawable.nanu1));
        this.idleTextures.put("2", BitmapFactory.decodeResource(getResources(), R.drawable.nanu2));
        this.idleTextures.put("3", BitmapFactory.decodeResource(getResources(), R.drawable.nanu3));
        this.idleTextures.put("4", BitmapFactory.decodeResource(getResources(), R.drawable.nanu4));
        this.idleTextures.put("5", BitmapFactory.decodeResource(getResources(), R.drawable.nanu5));
        this.idleTextures.put("6", BitmapFactory.decodeResource(getResources(), R.drawable.nanu6));
        this.idleTextures.put("7", BitmapFactory.decodeResource(getResources(), R.drawable.nanu7));
        this.idleTextures.put("8", BitmapFactory.decodeResource(getResources(), R.drawable.nanu8));
        this.idleTextures.put("9", BitmapFactory.decodeResource(getResources(), R.drawable.nanu9));
        this.idleTextures.put("10", BitmapFactory.decodeResource(getResources(), R.drawable.nanu10));
        this.idleTextures.put("11", BitmapFactory.decodeResource(getResources(), R.drawable.nanu11));
        this.idleTextures.put("12", BitmapFactory.decodeResource(getResources(), R.drawable.nanu12));
        this.idleTextures.put("13", BitmapFactory.decodeResource(getResources(), R.drawable.nanu13));
        this.idleTextures.put("14", BitmapFactory.decodeResource(getResources(), R.drawable.nanu14));
        this.idleTextures.put("15", BitmapFactory.decodeResource(getResources(), R.drawable.nanu15));
        this.idleTextures.put("16", BitmapFactory.decodeResource(getResources(), R.drawable.nanu16));
        this.idleTextures.put("17", BitmapFactory.decodeResource(getResources(), R.drawable.nanu17));
        this.idleTextures.put("18", BitmapFactory.decodeResource(getResources(), R.drawable.nanu18));
        this.idleTextures.put("19", BitmapFactory.decodeResource(getResources(), R.drawable.nanu19));
    }

    /**
     * Load and store all the mouth opening animation textures
     */

    private void loadMouthOpeningTextures() {
        this.mouthOpeningTextures = new HashMap<>();
        this.mouthOpeningTextures.put("1", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_open1));
        this.mouthOpeningTextures.put("2", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_open2));
        this.mouthOpeningTextures.put("3", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_open3));
        this.mouthOpeningTextures.put("4", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_open4));
        this.mouthOpeningTextures.put("5", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_open5));
        this.mouthOpeningTextures.put("6", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_open6));
        this.mouthOpeningTextures.put("7", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_open7));
        this.mouthOpeningTextures.put("8", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_open8));
        this.mouthOpeningTextures.put("9", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_open9));
    }


    /**
     * Load and store all the mouth closing animation textures
     */

    private void loadMouthClosingTextures() {
        this.mouthClosingTextures = new HashMap<>();
        this.mouthClosingTextures.put("1", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_close1));
        this.mouthClosingTextures.put("2", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_close2));
        this.mouthClosingTextures.put("3", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_close3));
        this.mouthClosingTextures.put("4", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_close4));
        this.mouthClosingTextures.put("5", BitmapFactory.decodeResource(getResources(), R.drawable.mouth_close5));
    }


    /**
     * Load and store all the lip licking animation textures
     */

    private void loadLipLickingTextures() {
        this.lipLickingTextures = new HashMap<>();
        this.lipLickingTextures.put("1", BitmapFactory.decodeResource(getResources(), R.drawable.lick1));
        this.lipLickingTextures.put("2", BitmapFactory.decodeResource(getResources(), R.drawable.lick2));
        this.lipLickingTextures.put("3", BitmapFactory.decodeResource(getResources(), R.drawable.lick3));
        this.lipLickingTextures.put("4", BitmapFactory.decodeResource(getResources(), R.drawable.lick4));
        this.lipLickingTextures.put("5", BitmapFactory.decodeResource(getResources(), R.drawable.lick5));
        this.lipLickingTextures.put("6", BitmapFactory.decodeResource(getResources(), R.drawable.lick6));
        this.lipLickingTextures.put("7", BitmapFactory.decodeResource(getResources(), R.drawable.lick7));
        this.lipLickingTextures.put("8", BitmapFactory.decodeResource(getResources(), R.drawable.lick8));
        this.lipLickingTextures.put("9", BitmapFactory.decodeResource(getResources(), R.drawable.lick9));
        this.lipLickingTextures.put("10", BitmapFactory.decodeResource(getResources(), R.drawable.lick10));
        this.lipLickingTextures.put("11", BitmapFactory.decodeResource(getResources(), R.drawable.lick11));
        this.lipLickingTextures.put("12", BitmapFactory.decodeResource(getResources(), R.drawable.lick12));
        this.lipLickingTextures.put("13", BitmapFactory.decodeResource(getResources(), R.drawable.lick13));
        this.lipLickingTextures.put("14", BitmapFactory.decodeResource(getResources(), R.drawable.lick14));
        this.lipLickingTextures.put("15", BitmapFactory.decodeResource(getResources(), R.drawable.lick15));
        this.lipLickingTextures.put("16", BitmapFactory.decodeResource(getResources(), R.drawable.lick16));
        this.lipLickingTextures.put("17", BitmapFactory.decodeResource(getResources(), R.drawable.lick17));
        this.lipLickingTextures.put("18", BitmapFactory.decodeResource(getResources(), R.drawable.lick18));
        this.lipLickingTextures.put("19", BitmapFactory.decodeResource(getResources(), R.drawable.lick19));
        this.lipLickingTextures.put("20", BitmapFactory.decodeResource(getResources(), R.drawable.lick20));
        this.lipLickingTextures.put("21", BitmapFactory.decodeResource(getResources(), R.drawable.lick21));
        this.lipLickingTextures.put("22", BitmapFactory.decodeResource(getResources(), R.drawable.lick22));
        this.lipLickingTextures.put("23", BitmapFactory.decodeResource(getResources(), R.drawable.lick23));
        this.lipLickingTextures.put("24", BitmapFactory.decodeResource(getResources(), R.drawable.lick24));
        this.lipLickingTextures.put("25", BitmapFactory.decodeResource(getResources(), R.drawable.lick25));
    }


    /**
     * Load and store all the chewing animation textures
     */

    private void loadChewingTextures() {
        this.chewingTextures = new HashMap<>();
        this.chewingTextures.put("1", BitmapFactory.decodeResource(getResources(), R.drawable.chew1));
        this.chewingTextures.put("2", BitmapFactory.decodeResource(getResources(), R.drawable.chew2));
        this.chewingTextures.put("3", BitmapFactory.decodeResource(getResources(), R.drawable.chew3));
        this.chewingTextures.put("4", BitmapFactory.decodeResource(getResources(), R.drawable.chew4));
        this.chewingTextures.put("5", BitmapFactory.decodeResource(getResources(), R.drawable.chew5));
        this.chewingTextures.put("6", BitmapFactory.decodeResource(getResources(), R.drawable.chew6));
        this.chewingTextures.put("7", BitmapFactory.decodeResource(getResources(), R.drawable.chew7));
        this.chewingTextures.put("8", BitmapFactory.decodeResource(getResources(), R.drawable.chew8));
        this.chewingTextures.put("9", BitmapFactory.decodeResource(getResources(), R.drawable.chew9));
        this.chewingTextures.put("10", BitmapFactory.decodeResource(getResources(), R.drawable.chew10));
        this.chewingTextures.put("11", BitmapFactory.decodeResource(getResources(), R.drawable.chew11));
        this.chewingTextures.put("12", BitmapFactory.decodeResource(getResources(), R.drawable.chew12));
        this.chewingTextures.put("13", BitmapFactory.decodeResource(getResources(), R.drawable.chew13));
    }

}
