package com.ramsy.GameCentre.MemoryMatrix;

public class Block {
    private int x;
    private int y;
    private int id;
    private boolean right = true;
    private boolean down = true;
    private int height;
    private int width;
    public Block(int id){
        this.id = id;
    }

    public Block(int id, int x, int y, int width, int height){
        this.id = id;
        this.x = x;
        this.y =y;
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean getDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        Block compare = (Block) obj;
        return compare.getId() == this.getId();
    }
}