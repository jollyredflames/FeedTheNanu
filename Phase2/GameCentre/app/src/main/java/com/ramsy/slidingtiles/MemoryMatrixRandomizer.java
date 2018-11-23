package com.ramsy.slidingtiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MemoryMatrixRandomizer {
    Set<Integer> clickableID;
    int numClicked;
    int badIndex;
    public MemoryMatrixRandomizer(Set<Integer> clickableID, int numClicked,int badIndex){
        this.clickableID = clickableID;
        this.numClicked = numClicked;
        this.badIndex = badIndex;
    }

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
