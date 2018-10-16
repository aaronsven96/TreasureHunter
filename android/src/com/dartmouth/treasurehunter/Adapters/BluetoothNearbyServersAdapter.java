package com.dartmouth.treasurehunter.Adapters;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dartmouth.treasurehunter.Bluetooth.BlueToothMainService;
import com.dartmouth.treasurehunter.Bluetooth.BluetoothConnectModle;
import com.dartmouth.treasurehunter.BtClientTester;
import com.dartmouth.treasurehunter.FindPlayersActivity;
import com.dartmouth.treasurehunter.FindServerActivity;
import com.dartmouth.treasurehunter.R;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by will on 5/22/18.
 */

public class BluetoothNearbyServersAdapter extends RecyclerView.Adapter<BluetoothNearbyServersAdapter.ViewHolder>{


    private ArrayList<BluetoothConnectModle> mDataset;
    private int pos;
    public LinearLayout mBlerb;

    private class BluetoothConnectClickListener implements View.OnClickListener {



        @Override
        public void onClick(View v) {

            RecyclerView mRecyclerView=v.getRootView().findViewById(R.id.server_list);
            int itemPosition = mRecyclerView.getChildLayoutPosition(v);

            BluetoothConnectModle ex = mDataset.get(itemPosition);

            BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
            bt.cancelDiscovery();



           // new ClientConnectThread(ex.getDevice(),ex.con).run();

            Intent intent2 = new Intent(ex.con, BlueToothMainService.class);
            intent2.putExtra("btdevice",ex.getDeviceHardwareAddress());

            ex.con.startService(intent2);


            Intent intnt = new Intent(ex.con,BtClientTester.class);
            intnt.putExtra("loc","fromStart");
            ex.con.startActivity(intnt);


        }

    }





    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mBlerb;
        public ViewHolder(LinearLayout v) {
            super(v);
            mBlerb = v;
        }
    }

    public BluetoothNearbyServersAdapter(ArrayList<BluetoothConnectModle> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public BluetoothNearbyServersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        LinearLayout v =  (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_blerb, parent, false);
        v.setOnClickListener(new BluetoothConnectClickListener());
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        pos = position;
        BluetoothConnectModle ex = mDataset.get(position);

        TextView deviceName = (TextView)((LinearLayout)holder.mBlerb.getChildAt(0)).getChildAt(0);
        TextView deviceHardwareAddress = (TextView)((LinearLayout)holder.mBlerb.getChildAt(0)).getChildAt(1);

        deviceName.setText(ex.getDeviceName());
        deviceHardwareAddress.setText(ex.getDeviceHardwareAddress());




    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
