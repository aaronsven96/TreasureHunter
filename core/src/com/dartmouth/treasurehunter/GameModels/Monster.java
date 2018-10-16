package com.dartmouth.treasurehunter.GameModels;

import java.util.Random;

//Class for monsters
//by Aaron

class Monster {

    //Monster Position
    private Position pos;
    //reference to Map
    private Map map;

    Monster(Position pos,Map map){
        this.pos=pos;
        this.map =map;
        map.placeChar(pos);
    }

    //removes monster from map
    void removeMonster(){
        map.removeNPC(pos);
    }

    //basic movement
    void move(int dx, int dy){
        pos.y=pos.y+dy;
        pos.x=pos.x+dx;
    }
    //gets the positions
    Position getPos(){
        return pos;
    }

    int getX() {
        return pos.x;
    }

    void setX(int x) {
        pos.x = x;
    }

    int getY() {
        return pos.y;
    }

    void setY(int y) {
        pos.y = y;
    }

    //moves using wiughted random movement
    void moveMonster(Position explorer) {

        //Queue of Possible moves
        com.badlogic.gdx.utils.Queue<Position> moves = new com.badlogic.gdx.utils.Queue<Position>();

        //Posible moves
        Position right = pos.copy();
        right.x++;
        Position left = pos.copy();
        left.x--;
        Position top = pos.copy();
        top.y++;
        Position down= pos.copy();
        down.y--;
        int weightTotal=0;

        //add moves to Queue
        weightTotal=queueAddHelper(moves,pos,pos,explorer,weightTotal);
        weightTotal=queueAddHelper(moves,pos,right,explorer,weightTotal);
        //System.out.println("right"+right.toString());
        right.frame=3;
        weightTotal=queueAddHelper(moves,pos,left,explorer,weightTotal);
        //System.out.println("left"+left.toString());
        left.frame=2;
        weightTotal=queueAddHelper(moves,pos,top,explorer,weightTotal);
        //System.out.println("top"+top.toString());
        top.frame=1;
        weightTotal=queueAddHelper(moves,pos,down,explorer,weightTotal);
        //System.out.println("down"+down.toString());
        down.frame=0;
        Random rand = new Random();
        int value = rand.nextInt(weightTotal);

        Position newPos;

        //pick a random move based on the wieght
        while ((newPos =moves.removeLast())!=null){
            //System.out.println(value+" weight:"+newPos.weight);
            if (value<newPos.weight){
                map.move(pos,newPos);
                pos=newPos;
                return;
            }
        }

    }
    //adds move to queue
    private int queueAddHelper(com.badlogic.gdx.utils.Queue moves,Position oldMon,Position newMon,Position explorer,int totWieght){
        newMon.weight=totWieght;
        if (oldMon.equals(newMon)){
            newMon.weight=totWieght+1;
            moves.addFirst(newMon);
        }
        else if (map.isValid(newMon)){
            if(!map.isNPC(newMon) && !map.isBlocked(newMon)) {
                int weight = monsterWeight(oldMon, newMon, explorer);
                newMon.weight = weight + totWieght;
                moves.addFirst(newMon);
            }
        }
        return newMon.weight;
    }
    //Asigns wieghts to each move
    private int monsterWeight(Position oldMon,Position newMon, Position explorer){
        int weight=1;
        //System.out.print(oldMon.toString()+"|"+newMon+"|"+explorer);
        if (Math.abs(oldMon.y-explorer.y)>Math.abs(newMon.y-explorer.y)){//are we going in the right direction
            if (Math.abs(newMon.y-explorer.y)<15){
                weight+=2;
            }
            if (Math.abs(newMon.y-explorer.y)<5){
                weight+=5;
            }
        }
        if (Math.abs(oldMon.x-explorer.x)>Math.abs(newMon.x-explorer.x)){//are we close
            if (Math.abs(newMon.x-explorer.x)<15){
                weight+=2;
            }
            if (Math.abs(newMon.x-explorer.x)<5){
                weight+=5;
            }
        }
        return weight;
    }
}
