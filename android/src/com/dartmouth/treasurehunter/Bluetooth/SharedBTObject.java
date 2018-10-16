package com.dartmouth.treasurehunter.Bluetooth;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by will on 5/26/18.
 */

public class SharedBTObject implements BluetoothInterface {




    private String dataToRead;


    private String dataToSend;




    private ReentrantLock lockToGame;
    private ReentrantLock lockFromGame;

    private Context con;



    public SharedBTObject(Context con){

        this.con = con;


        dataToSend = null;

        dataToRead = null;

        lockToGame = new ReentrantLock();

        lockFromGame = new ReentrantLock();


    }



    @Override
    public void setDataGame(String data) throws IOException {




        Intent intent = new Intent();
        intent.setAction("com.dartmouth.treasurehunter.send_data");
        intent.putExtra("data",data);
        con.sendBroadcast(intent);








       /* lockFromGame.lock();

        if(dataToSend != null){
            lockFromGame.unlock();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lockFromGame.lock();

            if(dataToSend != null){
                lockFromGame.unlock();
                throw new IOException("Data has not been red from shared object. Something is wrong with the Bt process");

            }



        }



        dataToSend = data;

        lockFromGame.unlock();*/


    }

    public String getDataThread(){
        lockFromGame.lock();
        String tmp = dataToSend;
        dataToSend=null;
        lockFromGame.unlock();
        return tmp;
    }





    public void setDataThread(String data) throws IOException {
        lockToGame.lock();

        if(dataToRead != null){
            lockToGame.unlock();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lockToGame.lock();

            if(dataToRead != null){
                lockToGame.unlock();
                throw new IOException("Data has not been red from shared object. Something is wrong with the Bt process");

            }



        }



        dataToRead = data;
        lockToGame.unlock();

    }

    @Override
    public String getDataGame() {

        lockToGame.lock();
        String tmData = dataToRead;
        dataToRead = null;
        lockToGame.unlock();
        return tmData;

    }







}
