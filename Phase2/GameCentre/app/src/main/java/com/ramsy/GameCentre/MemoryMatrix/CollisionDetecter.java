package com.ramsy.GameCentre.MemoryMatrix;

/**
 * this object will take in block objects and see if they collide or not
 * this object will adjust the x and y of that block accordingly so that no two
 * blocks will overlap
 */
public class CollisionDetecter {
    int screenWidth;
    int screenHeight;

    /**
     *
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     */
    public CollisionDetecter(int screenWidth,int screenHeight){
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    /**
     *
     * @param first move object to check for collision
     * @param second move object to check for collision
     */
    public void detectCollision(Block first, Block second){
        if (first != second) {
            int x = first.getX();
            int xWidth = x + first.getWidth();
            int y = first.getY();
            int yHeight = y + first.getHeight();
            if (xWidth >= second.getX() && xWidth <= second.getX() + second.getWidth()) {
                checkCollisionForFirstOnTheRight(first, second, y, yHeight);
            } else if (x >= second.getX() && x <= second.getX() + second.getWidth()) {
                checkForCollisionOnLeftOfFirst(first, second, y, yHeight);
            }
        } else {
            generateNewXAndY(first);
        }
    }

    /**
     *
     * @param first a block object to check for collisions with second
     * @param second    block object to check for collision with first
     * @param y tbe y value of first block
     * @param yHeight   the height of the first block
     */
    private void checkForCollisionOnLeftOfFirst(Block first, Block second, int y, int yHeight) {
        if (y >= second.getY() && y <= second.getY() + second.getHeight()) {
            first.setRight(true);
            first.setDown(true);
            second.setRight(false);
            second.setDown(false);
            first.setX(first.getX() + 1);
            first.setY(first.getY() + 1);
            generateNewXAndY(first);
        } else if (yHeight >= second.getY() && yHeight <= second.getY() + second.getHeight()) {
            first.setRight(false);
            first.setDown(false);
            second.setRight(true);
            second.setDown(true);
            first.setX(first.getX() - 1);
            first.setY(first.getY() - 1);
            generateNewXAndY(first);
        }
    }

    /**
     *
     * @param first a block object to check for collision with second
     * @param second    a block object to check for collision with first
     * @param y the y value of first
     * @param yHeight   the height of first
     */
    private void checkCollisionForFirstOnTheRight(Block first, Block second, int y, int yHeight) {
        if (y >= second.getY() && y <= second.getY() + second.getHeight()) {
            first.setRight(false);
            first.setDown(true);
            second.setRight(true);
            second.setDown(false);
            first.setX(first.getX() - 1);
            first.setY(first.getY() + 1);
            generateNewXAndY(first);
        } else if (yHeight >= second.getY() && yHeight <= second.getY() + second.getHeight()) {
            first.setRight(true);
            first.setDown(false);
            second.setRight(false);
            second.setDown(true);
            first.setX(first.getX() + 1);
            first.setY(first.getY() - 1);
            generateNewXAndY(first);
        }
    }

    /**
     *
     * @param moves a block object
     *              this method will adjust the x and y coordinates of the block based on
     *              collisions with other blocks or hitting the sides of the screens
     */
    public void generateNewXAndY(Block moves) {
        int x = moves.getX();
        int y = moves.getY();
        if (moves.getRight() && (x + moves.getWidth() > screenWidth)) {
            moves.setRight(false);
            x = x - 5;
        } else if (!moves.getRight() && (x <= 0)) {
            moves.setRight(true);
            x = x + 5;
        } else if (moves.getRight()) {
            x = x + 5;
        } else if (!moves.getRight()) {
            x = x - 5;
        }
        y = getY(moves, y);
        moves.setX(x);
        moves.setY(y);
    }

    /**
     *
     * @param moves object to set the new x and y
     * @param y the y value of the block
     * @return  the new y value based on if it should go up or down on the screen
     */
    private int getY(Block moves, int y) {
        if (moves.getDown() && (y + moves.getHeight() > screenHeight)) {
            moves.setDown(false);
            y = y - 10;
        } else if (!moves.getDown() && (y <= 175)) {
            moves.setDown(true);
            y = y + 10;
        } else if (moves.getDown()) {
            y = y + 10;
        } else if (!moves.getDown()) {
            y = y - 10;
        }
        return y;
    }
}
