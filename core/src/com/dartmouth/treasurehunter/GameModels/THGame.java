package com.dartmouth.treasurehunter.GameModels;

import java.util.ArrayList;
import java.util.Random;

//By Aaron
//Runs the game and interfaces with the outside world

public class THGame {
    private Explorer explorer;
    private ArrayList<Monster> monsters;
    private Map mapi;
    private int monDen;
    private boolean gameOver;
    private boolean win;
    public THGame(){
    }

    public ArrayList<Position> getMonsterPositions(){
        ArrayList<Position> positions= new ArrayList<Position>();
        for (Monster m:monsters){
            positions.add(m.getPos());
        }
        return positions;
    }

    public Position getExplorerPosition(){
        return explorer.getPos();
    }

    public void newGame(char[][] map,int size,int monDen){
        win =false;
        gameOver=false;
        this.monDen=monDen;
        mapi =new Map(map,size);
        monsters=new ArrayList<Monster>();
        for (int boxH=0;boxH<size/monDen;boxH++) {
            for (int boxW=0;boxW<size/monDen;boxW++) {
                Random rand =new Random();
                ArrayList<Position> positions=mapi.getMovablePositions(boxH*monDen,(boxH+1)*monDen,boxW*monDen,(boxW+1)*monDen);
                if(positions.size()>0) {
                    int posNum = rand.nextInt(positions.size());
                    monsters.add(new Monster(positions.get(posNum), mapi));
                }

//                int x =rand.nextInt(monDen);
//                int y =rand.nextInt(monDen);
//                Position posMon= new Position((boxH*monDen)+x,boxW*monDen+y);
//                if (mapi.canMove(posMon)){
//                    monsters.add(new Monster(posMon,mapi));
//                }
            }
        }
        explorer=new Explorer(new Position(18,18));
        System.out.println(mapi.toString());
    }

    public void updateGame(Move move){
        if (!gameOver) {

            Position exPos = explorer.getPos();
            Position newPos=new Position(explorer.getX()+move.getDx(),explorer.getY()+move.getDy());
            if (mapi.canMove(newPos)) {
                System.out.println("change move:" + move.getDx() + move.getDy());
                explorer.move(move.getDx(), move.getDy());
            }

            if (isAttacked()) {
                placeExplorerRandom();
            }
            for (Monster mon : monsters) {
                if (!mon.getPos().equals(move.getFreeze())) {
                    if (move.isFake()) {
                        mon.moveMonster(move.getFake());
                    } else {
                        mon.moveMonster(explorer.getPos());
                    }
                }
            }
            if (isAttacked()) {
                placeExplorerRandom();
            }
            if (exPos.equals(mapi.getTreasure())) {
                win = true;
                gameOver = true;
            }
        }
    }
    private void placeExplorerRandom(){
        ArrayList<Position> poss =mapi.getMovablePositions(0,mapi.getSize(),0,mapi.getSize()/2);
        Random rand = new Random();
        explorer.setPos( poss.get(rand.nextInt(poss.size())));
    }
    public boolean isGameOver(){
        return gameOver;
    }
    public boolean isWin(){
        return win;
    }

    private boolean isAttacked(){
        Position exPos =explorer.getPos();
        for (Monster mon: monsters){
            if(mon.getPos().equals(exPos)){
                return true;
            }
        }
        return false;
    }
}
