package com.ramsy.GameCentre.MemoryMatrix;

/**
 * This is a block object that relates to an android view with its id
 * but is completely decoupled
 */
public class Block {
    private int x;
    private int y;
    private int id;
    private boolean right = true;
    private boolean down = true;
    private int height;
    private int width;

    /**
     * @param id the id of the block which corresponds to a view id
     */
    public Block(int id){
        this.id = id;
    }

    /**
     *
     * @param id the id of the block that corresponds to a view id
     * @param x the x coordinate of the block
     * @param y the y coordinate of the block
     * @param width the width of the block
     * @param height the height of the block
     */

    public Block(int id, int x, int y, int width, int height){
        this.id = id;
        this.x = x;
        this.y =y;
        this.width = width;
        this.height = height;
    }

    /**
     *
     * @return the height of the block
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @param height set the height of the block to height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return the width of the block
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @param width set the width of the block to be width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return true if the block is moving right else return false
     */
    public boolean getRight() {
        return right;
    }

    /**
     *
     * @param right set the block to move right if right, else the block will move left if false
     */
    public void setRight(boolean right) {
        this.right = right;
    }

    /**
     *
     * @return a bool true if the block is moving down, false if the block is moving up
     */
    public boolean getDown() {
        return down;
    }

    /**
     *
     * @param down set the up or down direction to down(true) and up (false)
     */
    public void setDown(boolean down) {
        this.down = down;
    }


    /**
     *
     * @return the blocks x corrdinate
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param x set the blocks x coordinate to the new x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return this this blocks y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param y set the y coordinate to the new y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *
     * @return the blocks id
     */
    public int getId() {
        return this.id;
    }

    /**
     *
     * @param id set the blocks id to this id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param obj an object, in this case it is always a block object
     * @return  boolean return true if an only the blocks have the same id
     */

    @Override
    public boolean equals(Object obj) {
        Block compare = (Block) obj;
        return compare.getId() == this.getId();
    }
}