package com.dartmouth.treasurehunter.GameModels;

// Class holds Data for Explorer
// By Aaron

public class Explorer {
    private Position pos;

    Explorer(Position pos){
        this.pos=pos;
    }
    public void move(int dx, int dy){
        System.out.print("change"+dx+","+dy);
        pos.y=pos.y+dy;
        pos.x=pos.x+dx;
    }

    public int getX() {
        return pos.x;
    }
    public Position getPos(){
        return pos;
    }
    public void setPos(Position p){
        pos=p;
    }

    public void setX(int x) {
        pos.x = x;
    }

    public int getY() {
        return pos.y;
    }

    public void setY(int y) {
        pos.y = y;
    }
}
