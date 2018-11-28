package com.ramsy.GameCentre.FeedTheNanu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.ramsy.GameCentre.R;

/**
 * A class representing a pause / resume button.
 * This is a subclass of a view group that listens to click gestures,
 * and performs it's own state management internally, and then alerts a delegate if that state is now paused or not.
 * Internal state management is effectively just switching between showing one image or the other.
 * Starts off by showing the pause icon.
 */

class PauseButton extends RelativeLayout implements View.OnClickListener {

    private ImageView pauseImageView;
    private ImageView resumeImageView;
    private boolean playing = true;

    PauseButtonDelegate delegate;

    PauseButton(Context context) {
        super(context);

        Bitmap pauseIcon = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
        ImageView pauseImageView = new ImageView(context);
        pauseImageView.setImageBitmap(pauseIcon);
        this.pauseImageView = pauseImageView;

        Bitmap resumeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.resume);
        ImageView resumeImageView = new ImageView(context);
        resumeImageView.setImageBitmap(resumeIcon);
        this.resumeImageView = resumeImageView;

        this.addView(pauseImageView);
        this.addView(resumeImageView);

        // Start in the playing state; show the pause icon only
        this.showPauseIcon();
        this.setOnClickListener(this);

    }

    private void toggle() {
        this.playing = !this.playing;
        if (this.playing) {
            this.showPauseIcon();
        } else {
            this.showResumeIcon();
        }

    }

    private void showPauseIcon() {
        resumeImageView.setAlpha(0f);
        pauseImageView.setAlpha(1f);
    }

    private void showResumeIcon() {
        resumeImageView.setAlpha(1f);
        pauseImageView.setAlpha(0f);
    }


    @Override
    public void onClick(View v) {
        this.toggle();

        boolean paused = !this.playing;

        System.out.println("XXX " + paused);

        // Have a delegate method like 'wasTapped' that takes in a boolean of whether the button has been paused or not.
        // Then the client can use that boolean to resume / pause it's elements accordingly.
        // After this class has managed it's part internally.
        this.delegate.pauseButtonWasTapped(paused);



    }
}
