package com.dartmouth.treasurehunter.Bluetooth;

import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dartmouth.treasurehunter.AndroidLauncher;
import com.dartmouth.treasurehunter.THMain;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.io.IOException;

/**
 * Created by will on 5/24/18.
 */

public class BlueToothMainService extends Service {



    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
// Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";


    private static final int REQUEST_CONNECT_DEVICE = 1;

    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService outSideService;
    private String mConnectedDeviceName = null;


    private SharedBTObject btObject;

    private Boolean isNav;


    private  final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();

            switch (msg.what) {
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);




                    //intent.setAction("com.dartmouth.treasurehunter.recived_data");
                    //intent.putExtra("data",writeMessage);

                    //sendBroadcast(intent);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    /*
                    try {
                        btObject.setDataThread(readMessage);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }*/

                    intent.setAction("com.dartmouth.treasurehunter.recived_data");
                    intent.putExtra("data",readMessage);
                    //windwo bput
                    sendBroadcast(intent);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    /*Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                     */
                    /*
                    btObject = new SharedBTObject();
                     startThrd = new ObjectMonitorThread(BlueToothMainService.this,btObject);

                    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
                    initialize(new THMain(), config);


                    startThrd.run();*/




                    Intent startGame = AndroidLauncher.startGame(BlueToothMainService.this);
                    startGame.putExtra("playertype",isNav);
                    startActivity(startGame);



                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();



                    intent.setAction("com.dartmouth.treasurehunter.recived_data");
                    intent.putExtra("data","connectionLost");

                    //startThrd.cancel();

                    break;
            }
        }
    };


    String deviceAddress = null;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  Used to name the worker thread, important only for debugging.
     */

    public BlueToothMainService() {
        super();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);

        //btObject = new SharedBTObject();

        if(intent.getStringExtra("btdevice")!=null){
            deviceAddress=intent.getStringExtra("btdevice");
        }

        Log.d("inService","*****************************************************************888  started service   "+deviceAddress);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            stopSelf();
        }



        if (outSideService == null)  outSideService = new BluetoothService(this, mHandler);
        outSideService.start();


        if(deviceAddress!=null){
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
            outSideService.connect(device);
            isNav = false;

        }
        else {
            isNav = true;
           // outSideService.start();
        }

        IntentFilter filter = new IntentFilter("com.dartmouth.treasurehunter.send_data");
        registerReceiver(mReceiverSend, filter);

        IntentFilter filter2 = new IntentFilter("com.dartmouth.treasurehunter.stop");
        registerReceiver(mReceiverStop, filter2);


/*        IntentFilter filter = new IntentFilter("com.dartmouth.treasurehunter.recived_data");
        registerReceiver(mReceiverRecive, filter);*/

        return Service.START_REDELIVER_INTENT;

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiverSend);
        unregisterReceiver(mReceiverStop);
        //startThrd.cancel();
/*
        unregisterReceiver(mReceiverRecive);
*/

    }

    private final BroadcastReceiver mReceiverSend = new BroadcastReceiver() { //Broadcast recerver to handle found devices
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.dartmouth.treasurehunter.send_data")) {

                //Toast.makeText(BlueToothMainService.this, "service got a send broadcast", Toast.LENGTH_LONG).show();


                // Check that we're actually connected before trying anything
                if (outSideService.getState() != BluetoothService.STATE_CONNECTED) {
                    Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
                    return;
                }
                outSideService.write(intent.getStringExtra("data").getBytes());

            }
        }
    };


    private final BroadcastReceiver mReceiverStop = new BroadcastReceiver() { //Broadcast recerver to handle found devices
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.dartmouth.treasurehunter.stop")) {

                //Toast.makeText(BlueToothMainService.this, "service got a send broadcast", Toast.LENGTH_LONG).show();


                // Check that we're actually connected before trying anything
/*                if (outSideService.getState() != BluetoothService.STATE_CONNECTED) {
                    Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                outSideService.stop();



                stopSelf();

            }
        }
    };

/*
    private final BroadcastReceiver mReceiverRecive = new BroadcastReceiver() { //Broadcast recerver to handle found devices
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.dartmouth.treasurehunter.recived_data")) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                txt.setText(intent.getStringExtra("data"));

            }
        }
    };
*/










    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
/*
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        super.onHandle (intent);
        Toast.makeText(getApplicationContext(), "Connected to "     + intent.getStringExtra("btdevice"), Toast.LENGTH_SHORT).show();
        if(intent.getStringExtra("btdevice")!=null){
            deviceAddress=intent.getStringExtra("btdevice");
        }
    }*/
}

