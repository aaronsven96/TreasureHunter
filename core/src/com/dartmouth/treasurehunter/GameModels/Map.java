package com.dartmouth.treasurehunter.GameModels;


import java.util.ArrayList;
// Class Holds Data for the Map
// By Aaron
class Map {
    //Useful map info
    private char[][] tileMap;
    private Boolean[][] blocked;
    private Boolean[][] NPCPos;

    private int size;//size

    private Position treasure;

    Map(char[][] map,int size){
        // create maps from tilemap
        this.tileMap=map;
        this.size=size;
        blocked=new Boolean[size][size];
        NPCPos=new Boolean[size][size];
        for (int row=0;row<size;row++){
            for (int col=0;col<size;col++){
                if (tileMap[row][col]=='t'){
                    System.out.println(row+":"+col+"|"+"Treasure");
                    treasure=new Position(row,col);
                }
                blocked[row][col]=isTileBlock(tileMap[row][col]);
                NPCPos[row][col]=false;
            }
        }
    }

    //prints the map with walls monsters and floors
    @Override
    public String toString() {
        StringBuilder mapString=new StringBuilder();
        mapString.append("\n");
        for (int row=0;row<size;row++){
            mapString.append("\n");
            for (int col=0;col<size;col++){
                if (blocked[row][col]){
                    mapString.append("|");
                }
                if (NPCPos[row][col]){
                    mapString.append("m");
                }
                else if(!isTileBlock(tileMap[row][col])){
                    mapString.append("-");
                }
            }
            }
                return mapString.toString();
    }
    //gets all movable positions within an area
    ArrayList<Position> getMovablePositions(int xR,int xL,int yB, int yT){
        ArrayList<Position> positions = new ArrayList<Position>();
        for (int row=xR;row<xL;row++){
            for (int col=yB;col<yT;col++){
                Position p =new Position(row,col);
                if (isValid(p)){
                    if (canMove(p)&& !isNPC(p)){
                        positions.add(p);
                    }
                }
            }
        }
        return positions;
    }

    int getSize(){
        return size;
    }

    //gets tileType
    char getTileType(Position pos){
        return tileMap[pos.x][pos.y];
    }

    //places a character at a position
    void placeChar(Position pos){
        NPCPos[pos.x][pos.y]=true;
    }

    // this space blocked by a wall
    boolean isBlocked(Position pos){
        return blocked[pos.x][pos.y];
    }

    //is this within the map
    boolean isValid(Position pos){
        return pos.x >= 0 && pos.x < size&& pos.y >= 0 && pos.y < size;
    }
    //is this where an NPC is
    boolean isNPC(Position pos){
        return NPCPos[pos.x][pos.y];
    }

    boolean canMove(Position pos){
        return !isBlocked(pos) &&isValid(pos);
    }

    //removes an NPC
    void removeNPC(Position NPC){
        NPCPos[NPC.x][NPC.y]=false;
    }

    //moves an NPC
    void move(Position oldPos, Position newPos){
        NPCPos[oldPos.x][oldPos.y]=false;
        NPCPos[newPos.x][newPos.y]=true;
    }

    //what characters represent what Tiles
    private boolean isTileBlock(char a) {

        if (a == 'w') {
            return true;
        }

        else if (a == 'f') {
            return false;
        }
        else if (a=='t'){
            return false;
        }
        return true;
    }

    //this is where the treasure is
    public Position getTreasure() {
        return treasure;
    }
}
