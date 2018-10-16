package com.dartmouth.treasurehunter.GameModels;

//Class to hold information for a move
//By Aaron

public class Move {
    private int dx;
    private int dy;
    private boolean isFake=false;
    private Position fake;
    private Position freeze;

    public Move(int dx,int dy,Position freeze){
        this.dy=dy;
        this.dx=dx;
        this.freeze=freeze;
    }
    public Move(int dx,int dy,Position fake,Boolean isFake,Position freeze){
        this.dy=dy;
        this.dx=dx;
        this.fake=fake;
        this.isFake=isFake;
        this.freeze=freeze;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public boolean isFake() {
        return isFake;
    }
    public Position getFake(){
        return fake;
    }

    public Position getFreeze() {
        return freeze;
    }
}
