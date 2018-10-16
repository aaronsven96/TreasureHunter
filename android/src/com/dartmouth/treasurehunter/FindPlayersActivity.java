package com.dartmouth.treasurehunter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.dartmouth.treasurehunter.Bluetooth.BlueToothMainService;

public class FindPlayersActivity extends Activity {
    private static final int REQUEST_ENABLE_BT = 2;

/*    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { //Broadcast recerver to handle found devices
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.dartmouth.treasurehunter.recived_data")) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                Toast.makeText(getApplicationContext(),"Got Data from client: "+intent.getStringExtra("data"),
                        Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent();
                intent2.setAction("com.dartmouth.treasurehunter.sent_data");
                intent2.putExtra("data","got your data: "+intent.getStringExtra("data"));
                sendBroadcast(intent2);
            }
        }
    };*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != REQUEST_ENABLE_BT || resultCode != RESULT_OK ) {
            Intent returnIntnt = MainActivity.openMainIntent(FindPlayersActivity.this);
            startActivity(returnIntnt);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_players);


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }



        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        startService(new Intent(this, BlueToothMainService.class));


/*
        IntentFilter filter = new IntentFilter("com.dartmouth.treasurehunter.recived_data");
        registerReceiver(mReceiver, filter);
*/


        //new ServerAcceptThread(FindPlayersActivity.this).run();

    }




    public static Intent openFindPlayersIntent(Activity activity){
        return new Intent(activity,FindPlayersActivity.class);
    }
}
