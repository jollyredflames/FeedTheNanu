package com.ramsy.GameCentre.FeedTheNanu;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.widget.ImageView;


/**
 * The base class for falling items in the game.
 * This class is abstract, so instances cannot be created; it must be subclassed instead,
 * allowing the subclass to set a bitmap image (this is a subclass of ImageView) and
 * also allowing them to conform to Edible to provide their custom effects.
 * This class also provides an attribute of type ObjectAnimator, and conforms to Pausable,
 * providing the implementations of the interface requirements here,
 * since they will be the same for all subclasses.
 * The animator property is not private, to allow a drop item subclass to be instantiate,
 * an animation created and configured, and then assigned to it,
 * as the Pausable implementations here require it.
 */

public abstract class DropItem extends ImageView implements Pausable {

    // Whoa, so abstract prevents it from being instantiated, but it can still define an init
    // that can be called as super(...) from a subclass.

    public ObjectAnimator animator;

    public DropItem(Context context) {
        super(context);
    }

    @Override
    public void resume() {
        animator.resume();
    }

    @Override
    public void pause() {
        animator.pause();
    }
}
