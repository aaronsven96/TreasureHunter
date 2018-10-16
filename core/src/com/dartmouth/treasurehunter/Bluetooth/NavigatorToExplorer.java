package com.dartmouth.treasurehunter.Bluetooth;

import com.dartmouth.treasurehunter.GameModels.Position;

import java.util.ArrayList;

public class NavigatorToExplorer {
    private Position explorer;
    private ArrayList<Position> monPositions;
    NavigatorToExplorer(Position explorer, ArrayList<Position> monPositions){
        this.explorer=explorer;
        this.monPositions=monPositions;
    }

    public ArrayList<Position> getMonPositions() {
        return monPositions;
    }

    public Position getExplorer() {
        return explorer;
    }
}
