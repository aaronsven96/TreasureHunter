package com.dartmouth.treasurehunter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dartmouth.treasurehunter.Adapters.BluetoothNearbyServersAdapter;
import com.dartmouth.treasurehunter.Bluetooth.BluetoothConnectModle;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class FindServerActivity extends Activity {


    private RecyclerView mRecyclerView;
    //private HistoryAdapter mAdapter;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View v;
    private ArrayList<BluetoothConnectModle> buildList;


    private  BluetoothAdapter mBluetoothAdapter;
    static final int REQUEST_ENABLE_BT = 1;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { //Broadcast recerver to handle found devices
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String deviceName = device.getName();
                if(deviceName==null) deviceName="Unknown";
                String deviceHardwareAddress = device.getAddress(); // MAC address

                for(BluetoothConnectModle bt : buildList){
                    if(bt.getDeviceHardwareAddress().compareTo(deviceHardwareAddress)==0)return;
                }


               /* Toast.makeText(FindServerActivity.this,
                        "Found bluetooth device:"+deviceName,
                        Toast.LENGTH_SHORT).show();*/




                buildList.add(new BluetoothConnectModle(deviceName,deviceHardwareAddress,device,FindServerActivity.this));

                mAdapter = new BluetoothNearbyServersAdapter(buildList);
                mRecyclerView.setAdapter(mAdapter);
                //mAdapter.addAll(data);
                // force notification -- tell the adapter to display
                mAdapter.notifyDataSetChanged();

            }
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != REQUEST_ENABLE_BT || resultCode != RESULT_OK ) {
            Intent returnIntnt = MainActivity.openMainIntent(FindServerActivity.this);
            startActivity(returnIntnt);
        }


    }





    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < 23)
            return;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }else{


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {





        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //Show an explanation to the user *asynchronously*
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Location acces is required for Bluetooth")
                            .setTitle("Important permission required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                            }

                        }
                    });
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                } else {
                    Intent returnIntnt = MainActivity.openMainIntent(FindServerActivity.this);
                    startActivity(returnIntnt);
                }
            }
        }


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_server);


        buildList = new ArrayList<BluetoothConnectModle>();
        mRecyclerView = (RecyclerView) findViewById(R.id.server_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(FindServerActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        //*****************************Bluetooth setup**********************************
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(FindServerActivity.this,
                    "Could not find a Bluetooth adapter. Does you Phone support Bluetooth?",
                    Toast.LENGTH_SHORT).show();
            Intent returnIntnt = MainActivity.openMainIntent(FindServerActivity.this);
            startActivity(returnIntnt);
            finish();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        checkPermissions();



        //**************************Start Searching for Bluetooth server***************************

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();





    }






    public static Intent openFindIntent(Activity activity){
        return new Intent(activity,FindServerActivity.class);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
    }







}
