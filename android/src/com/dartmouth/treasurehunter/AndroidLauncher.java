package com.dartmouth.treasurehunter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dartmouth.treasurehunter.Bluetooth.SharedBTObject;
import com.dartmouth.treasurehunter.THMain;

import java.io.IOException;

public class AndroidLauncher extends AndroidApplication {



	private SharedBTObject btObject;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() { //Broadcast recerver to handle found devices
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.dartmouth.treasurehunter.recived_data")) {
				// Discovery has found a device. Get the BluetoothDevice
				// object and its info from the Intent.
				try {
					btObject.setDataThread(intent.getStringExtra("data"));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	};





	private int getDensity(){

		SharedPreferences userdata = AndroidLauncher.this.getSharedPreferences("AppPrefs", AndroidLauncher.this.MODE_PRIVATE);

		return userdata.getInt("den",1);


	}
	private int getFrequency(){
		SharedPreferences userdata = AndroidLauncher.this.getSharedPreferences("AppPrefs", AndroidLauncher.this.MODE_PRIVATE);

		return userdata.getInt("freq",1);


	}




	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		btObject = new SharedBTObject(AndroidLauncher.this);


		IntentFilter filter = new IntentFilter("com.dartmouth.treasurehunter.recived_data");
		registerReceiver(mReceiver, filter);


		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();




		if(getIntent().getBooleanExtra("playertype",false)){

			initialize(new THMain(btObject, !getIntent().getBooleanExtra("playertype", false),getDensity(),getFrequency()), config);

		}else {

			initialize(new THMain(btObject), config);
		}

		/*btObject = new SharedBTObject();
		ObjectMonitorThread startThrd = new ObjectMonitorThread(AndroidLauncher.this,btObject);
		startThrd.run();*/


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mReceiver);

		Intent intent = new Intent();
		intent.setAction("com.dartmouth.treasurehunter.stop");
		intent.putExtra("data","connectionLost");
		sendBroadcast(intent);

		Intent returnHome = MainActivity.openMainIntent(AndroidLauncher.this);
		returnHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(returnHome);

	}

/*
	@Override
	protected void onPause() {
		super.onPause();

		unregisterReceiver(mReceiver);

		Intent intent = new Intent();
		intent.setAction("com.dartmouth.treasurehunter.stop");
		intent.putExtra("data","connectionLost");
		sendBroadcast(intent);

		Intent returnHome = MainActivity.openMainIntent(AndroidLauncher.this);
		returnHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(returnHome);

	}*/






	public static Intent startGame(Context activity){

		return new Intent(activity,AndroidLauncher.class);
	}
}
