package com.ramsy.GameCentre.MemoryMatrix;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryGameCommon {

    public static void go(RelativeLayout container,Set<Integer> clickers, MemoryMatrixManager manager,int offSet){
        for (Block item : manager) {
            if (clickers.contains(item.getId())) {
                View v = container.getChildAt(item.getId() + 2-offSet);
                v.setBackgroundColor(Color.YELLOW);
            }
        }
    }

    /**
     * make the timer that will reset the text views to be gray so that the user can now click them
     * @param container
     * @param manager
     * @param resetDelay
     * @param offSet
     */
    public static void resetColor(RelativeLayout container,MemoryMatrixManager manager,int resetDelay,int offSet){
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Block item : manager) {
                    container.getChildAt(item.getId() + 2-offSet).setBackgroundColor(Color.GRAY);
                }
                manager.setCanClick(true);
                t.cancel();
            }
        }, resetDelay, 500);
    }
}
