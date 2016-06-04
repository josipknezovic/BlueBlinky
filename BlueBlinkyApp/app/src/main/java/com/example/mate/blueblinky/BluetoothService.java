package com.example.mate.blueblinky;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;


import java.io.IOException;
import java.util.UUID;

import layout.BlueWidget;

/**
 * Created by Mate on 27.5.2016..
 */
public class BluetoothService extends IntentService {

    Intent intent = null;

    //AppWidgetManager manager = null;
    //RemoteViews views = null;
    //ComponentName componentName = null;

    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothService() {
        super("BluetoothService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        intent = workIntent;
        String action = workIntent.getAction();

        if(BlueWidget.WIDGET_OPEN_CLICKED.equals(action)) {
            bluetoothSetup();
            connect(MainActivity.HC_ADDRESS);
        }
    }

    private void bluetoothSetup() {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null) {
            stopService(intent);
        }
        if(!myBluetooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            turnBTon.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(turnBTon);
        }
        while(!myBluetooth.isEnabled()) {
            try {
                while(!myBluetooth.isEnabled()) Thread.sleep(10);
            } catch (Exception e) {}
        }
    }

    private void connect(String address) {
        myBluetooth.cancelDiscovery();
        new ConnectBT(address).execute();
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void> {

        private boolean ConnectSuccess = true;
        private String address;

        public ConnectBT(String address) {
            this.address = address;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... devices) {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice disp = myBluetooth.getRemoteDevice(address);
            try {
                btSocket = disp.createInsecureRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            } catch(Exception e) {
                Log.e("", "Error creating socket");
            }
            try {
                btSocket.connect();

            } catch (IOException e) {
                Log.e("", e.getMessage());
                try {
                    btSocket = (BluetoothSocket) disp.getClass().getMethod(
                            "createRfcommSocket", new Class[] {int.class}
                    ).invoke(disp, 1);
                    btSocket.connect();

                } catch(Exception e2) {
                    ConnectSuccess = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                Log.e("", "Connection failed.");
            } else {

                try {
                    btSocket.getOutputStream().write("2".getBytes());
                    btSocket.close();
                    myBluetooth.cancelDiscovery();

                } catch (IOException e) {

                }
            }

        }
    }
}
