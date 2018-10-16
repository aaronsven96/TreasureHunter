package com.dartmouth.treasurehunter.GameModels;
// holds position and other useful info
// By Aaron
 public class Position {
    public int x;
    public int y;
    public int weight;
    public int frame;

    public Position(int x,int y){
        this.x=x;
        this.y=y;
        weight=0;
        frame=1;
    }
    public Position(int x, int y,int frame){
        this.x=x;
        this.y=y;
        weight=0;
        this.frame=frame;
    }

    public boolean equals(Position pos) {
        return pos.x==x&&pos.y==y;
    }

    public Position copy(){
        Position pos =new Position(x,y);
        pos.weight=weight;
        return  pos;
    }

    @Override
    public String toString() {
        return x+":" + y + ":"+ weight;
    }
}
