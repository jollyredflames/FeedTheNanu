package com.ramsy.GameCentre.MemoryMatrix;

import android.view.View;

public class Block {
    private int x;
    private int y;
    private View view;
    private boolean right = true;
    private boolean down = true;
    private int height;
    private int width;

    public Block(View v, int x, int y, int width, int height){
        this.view = v;
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

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public boolean equals(Object obj) {
        View compare = (View) obj;
        return compare.getId() == getView().getId();

    }
}