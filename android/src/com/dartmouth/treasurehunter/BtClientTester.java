package com.dartmouth.treasurehunter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dartmouth.treasurehunter.Adapters.BluetoothNearbyServersAdapter;
import com.dartmouth.treasurehunter.Bluetooth.BluetoothConnectModle;

public class BtClientTester extends Activity {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_client_tester);





        if(getIntent().getStringExtra("loc").compareTo("fromStart")!=0){

            Intent intnt = MainActivity.openMainIntent(BtClientTester.this);
            startActivity(intnt);
        }



    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        finish();
    }





}
