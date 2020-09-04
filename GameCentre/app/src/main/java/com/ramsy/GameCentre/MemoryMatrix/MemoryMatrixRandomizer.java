package com.ramsy.GameCentre.MemoryMatrix;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * this class generates the random numbers for the view ids to be clicked in both memory matrix games
 */
public class MemoryMatrixRandomizer {
    Set<Integer> clickableID;
    int numClicked;
    int badIndex;

    /**
     *
     * @param clickableID the block ids that are able to be clicked
     * @param numClicked    the number of blocks the user must click
     * @param badIndex  the first index before even numbers are valid ids to be clicked
     */
    public MemoryMatrixRandomizer(Set<Integer> clickableID, int numClicked,int badIndex){
        this.clickableID = clickableID;
        this.numClicked = numClicked;
        this.badIndex = badIndex;
    }

    /**
     *
     * @return a set of all the block ids that must be clicked
     */
    public Set<Integer> randomizer(){
        Random rand = new Random();
        int max = Collections.max(clickableID)+2;
        int min = Collections.min(clickableID);
        Set<Integer> mustBeClicked = new HashSet<>();
        for(int i =0;i<numClicked;i++){
            int temp = rand.nextInt((max - min) + 1) + min;
            if(clickableID.contains(temp) && !mustBeClicked.contains(temp)){
                mustBeClicked.add(temp);
            }
            else{
                i--;
            }
        }
        return mustBeClicked;
    }
}
