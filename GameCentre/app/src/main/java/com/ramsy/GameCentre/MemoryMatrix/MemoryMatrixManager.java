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
    private int score;
    private int x;
    private int y;
    private User meUser;

    public MemoryMatrixManager(ArrayList<Block> blocksToHighLight ,Set<Integer> clickableID,int badIndex,int life,int numUndo,int slot,int score,int x,int y) {
        meUser = FirebaseFuncts.getUser();
        this.clickableID = clickableID;
        this.numToBeClicked = (int) Math.ceil(blocksToHighLight.size()*0.35);
        MemoryMatrixRandomizer randomer = new MemoryMatrixRandomizer(clickableID, numToBeClicked, badIndex);
        mustBeClicked = randomer.randomizer();
        this.blocksToHighLight = blocksToHighLight;
        this.life = life;
        this.numUndo = numUndo;
        this.slot = slot;
        this.score = score;
        this.x = x;
        this.y = y;
        save();
    }


    public int getNumUndo(){
        return this.numUndo;
    }

    public int getLife(){
        return this.life;
    }

    public boolean checkTileCorrect(int ID) {
        boolean valid;
        if (mustBeClicked.contains(ID)) {
            if (!correctClicks.contains(ID)) {
                correctClick++;
                correctClicks.add(ID);
                valid = true;
            }
            valid = true;
            save();
            return valid;
        } else {
            wrongClicks.add(ID);
            loseHP();
        }
        save();
        return false;
    }
    public boolean gameOver(){
        return this.life <= 0;
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

    public void calculateScore() {
        int newScore = 4*x+4*y+score;
        newScore = newScore - 4*wrongClicks.size();
        this.score = newScore;
        save();
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
        save();
        return item;
    }


    @Override
    public Iterator iterator() {
        return new ClassListIterator();
    }

    /**
     * this allows you to iterate over the blocks in this class,
     * used by the activities to know which views and such needs to update
     */
    private class ClassListIterator implements Iterator<Block> {
        /**
         * The index of the next item in the class list.
         */
        int nextIndex = 0;

        /**
         *
         * @return true if there are more blocks to iterate over else return false
         */
        @Override
        public boolean hasNext() {
            return nextIndex != blocksToHighLight.size();
        }

        /**
         *
         * @return the next block in this manager class
         */
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

    public int getSlot(){
        return slot;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public String getScore(){
        return String.valueOf(score);
    }

    /**
     * makes a save state for the memory matrix game which will save whenever the uses clicks on
     * the tiles and tries to undo and or quit
     */
    public void save(){
        boolean difficulty;
        difficulty = this.y == 0;
        SaveState save = new SaveState(difficulty);
        save.setNumY(y);
        save.setNumX(x);
        save.setScore(score);
        save.setLife(life);
        save.setNumUndo(numUndo);
        save.setScore(score);
        if(y == 0){
            save.setDifficulty(true);
        }
        else{
            save.setDifficulty(false);
        }
        meUser.saveGame("MemoryMatrix", save,slot);
    }

}