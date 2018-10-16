package com.dartmouth.treasurehunter.Bluetooth;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;

/**
 * Created by will on 5/26/18.
 */

public class ObjectMonitorThread extends  Thread {

    private Context con;
    private Boolean stop;
    private SharedBTObject btObject;

    public ObjectMonitorThread(Context con, SharedBTObject btObject){
        stop = false;
        this.con = con;
        this.btObject = btObject;


    }

    public void run() {
        while(!stop){
            try {
                Thread.sleep(100); //might be too long
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String data = btObject.getDataThread();

            if(data!=null){


                Intent intent = new Intent();
                intent.setAction("com.dartmouth.treasurehunter.send_data");
                intent.putExtra("data",data);
                con.sendBroadcast(intent);


            }



        }

    }

    public void cancel() {

        stop = true;
            //either set a delete flag on the object or somehow cancel thread



    }


}
