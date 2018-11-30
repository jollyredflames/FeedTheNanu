package com.ramsy.GameCentre.MemoryMatrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MemoryMatrixManager implements Iterable<Block> {
    private Set<Integer> clickableID;
    private Set<Integer> mustBeClicked;
    private int numToBeClicked;
    private int undo = 5;
    private int hp = 5;
    private int correctClick = 0;
    private boolean canClick = false;
    private Set<Integer> correctClicks = new HashSet<>();
    private ArrayList<Integer> wrongClicks = new ArrayList<>();
    private ArrayList<Block> blocksToHighLight;

    public MemoryMatrixManager(ArrayList<Block> blocksToHighLight ,Set<Integer> clickableID,int badIndex) {
        this.clickableID = clickableID;
        this.numToBeClicked = (int) Math.ceil(blocksToHighLight.size()*0.25);
        MemoryMatrixRandomizer randomer = new MemoryMatrixRandomizer(clickableID, numToBeClicked, badIndex);
        mustBeClicked = randomer.randomizer();
        this.blocksToHighLight = blocksToHighLight;
    }

    public boolean checkTileCorrect(int ID) {
        if (mustBeClicked.contains(ID)) {
            if (!correctClicks.contains(ID)) {
                correctClick++;
                correctClicks.add(ID);
                return true;
            }
            return true;
        } else {
            wrongClicks.add(ID);
        }
        return false;
    }

    public void setCanClick(boolean yes){
        this.canClick = yes;
    }

    public void loseHP() {
        hp--;
    }

    public void gainHP() {
        hp++;
    }

    public int calculateScore(int prevScore) {
        return 0;
    }
    //10*(m*n)^2

    public int calculateScore() {
        return 0;
    }

    public boolean isGameComplete() {
        return correctClick == mustBeClicked.size();
    }

    public boolean getClick() {
        return canClick;
    }


    public boolean setUpUndo() {
        if (undo <= 0) {
            return false;
        }
        return wrongClicks.size() != 0;
    }

    public int performUndo() {
        this.gainHP();
        undo--;

        int index = wrongClicks.size() - 1;
        int item = wrongClicks.get(index);
        wrongClicks.remove(wrongClicks.size() - 1);
        return item;
    }


    @Override
    public Iterator iterator() {
        return new ClassListIterator();
    }


    private class ClassListIterator implements Iterator<Block> {
        /**
         * The index of the next item in the class list.
         */
        int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex != blocksToHighLight.size();
        }

        @Override
        public Block next() {
            Block result = blocksToHighLight.get(nextIndex);
            nextIndex++;
            return result;
        }
    }

    public Set<Integer> getMustBeClicked(){
        return mustBeClicked;
    }

}