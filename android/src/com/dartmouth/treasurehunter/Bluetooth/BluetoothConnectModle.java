package com.dartmouth.treasurehunter.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * Created by will on 5/22/18.
 */

public class BluetoothConnectModle {


    private String deviceName;
    private String deviceHardwareAddress;
    private BluetoothDevice device;
    public Activity con;


    public BluetoothConnectModle(String deviceName, String deviceHardwareAddress, BluetoothDevice device,Activity con) {
        this.con = con;
        this.device=device;
        this.deviceName = deviceName;
        this.deviceHardwareAddress = deviceHardwareAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceHardwareAddress() {
        return deviceHardwareAddress;
    }

    public void setDeviceHardwareAddress(String deviceHardwareAddress) {
        this.deviceHardwareAddress = deviceHardwareAddress;
    }


    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
