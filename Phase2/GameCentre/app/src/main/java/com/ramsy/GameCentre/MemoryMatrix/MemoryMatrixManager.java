package com.ramsy.GameCentre.MemoryMatrix;

import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.FirebaseFuncts;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.SaveState;
import com.ramsy.GameCentre.DatabaseSavablesAndFuncts.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MemoryMatrixManager implements Iterable<Block> {
    private Set<Integer> clickableID;
    private Set<Integer> mustBeClicked;
    private int numToBeClicked;
    private int numUndo;
    private int life;
    private int slot;
    private int correctClick = 0;
    private boolean canClick = false;
    private Set<Integer> correctClicks = new HashSet<>();
    private ArrayList<Integer> wrongClicks = new ArrayList<>();
    private ArrayList<Block> blocksToHighLight;
    private String score;
    private int x;
    private int y;
    private User meUser;

    public MemoryMatrixManager(ArrayList<Block> blocksToHighLight ,Set<Integer> clickableID,int badIndex,int life,int numUndo,int slot,String score,int x,int y) {
        meUser = FirebaseFuncts.getUser();
        this.clickableID = clickableID;
        this.numToBeClicked = (int) Math.ceil(blocksToHighLight.size()*0.25);
        MemoryMatrixRandomizer randomer = new MemoryMatrixRandomizer(clickableID, numToBeClicked, badIndex);
        mustBeClicked = randomer.randomizer();
        this.blocksToHighLight = blocksToHighLight;
        this.life = life;
        this.numUndo = numUndo;
        this.slot = slot;
        this.score = score;
        this.x = x;
        this.y = y;
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
            loseHP();
        }
        return false;
    }
    public boolean gameOver(){
        return this.life == 0;
    }

    public void setCanClick(boolean yes){
        this.canClick = yes;
    }

    public void loseHP() {
        life--;
    }

    public void gainHP() {
        life++;
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
        if (numUndo <= 0) {
            return false;
        }
        return wrongClicks.size() != 0;
    }

    public int performUndo() {
        this.gainHP();
        numUndo--;

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


    public void save(){
        Save save = new Save();
        save.setNumY(y);
        save.setNumX(x);
        save.setScore(score);
        save.setLife(life);
        save.setNumUndo(numUndo);
        if(y == 0){
            save.setDifficulty(true);
        }
        else{
            save.setDifficulty(false);
        }
        meUser.saveGame("MemoryMatrix",(SaveState)save,slot);
    }

}