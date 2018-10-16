package com.dartmouth.treasurehunter.Bluetooth;

import java.io.IOException;

/**
 * Created by will on 5/22/18.
 */

public interface BluetoothInterface {

    void setDataGame(String data) throws IOException;

    String getDataGame();

}
