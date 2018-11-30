package com.ramsy.GameCentre.MemoryMatrix;

public class CollisionDetecter {
    int screenWidth;
    int screenHeight;
    public CollisionDetecter(int screenWidth,int screenHeight){
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public void detectCollision(Block first, Block second){
        if (first != second) {
            int x = first.getX();
            int xWidth = x + first.getWidth();
            int y = first.getY();
            int yHeight = y + first.getHeight();
            if (xWidth >= second.getX() && xWidth <= second.getX() + second.getWidth()) {
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
            } else if (x >= second.getX() && x <= second.getX() + second.getWidth()) {
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
        } else {
            generateNewXAndY(first);
        }
    }

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
        moves.setX(x);
        moves.setY(y);
    }
}
