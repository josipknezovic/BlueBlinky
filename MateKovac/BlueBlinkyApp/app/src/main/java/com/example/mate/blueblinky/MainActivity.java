package com.example.mate.blueblinky;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    Button openBtn;
    Button scanBtn;
    Button cancelBtn;
    ImageView redDot;
    ImageView greenDot;

    String address = null;

    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    //private ProgressDialog progress;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private Set<BluetoothDevice> pairedDevices;
    private MyBroadcastReceiver br = null;
    private Animation animation = null;


    public static String HC_ADDRESS = "20:14:03:24:28:61";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = new AlphaAnimation(1, 0);
        animation.setDuration(250);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        bluetoothSetup();

        openBtn = (Button) findViewById(R.id.button);
        openBtn.setEnabled(false);
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("2".getBytes());
                    } catch (IOException e) {
                        msg("Error");
                    }
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                }
            }
        });

        scanBtn = (Button) findViewById(R.id.button1);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetooth.isDiscovering()) myBluetooth.cancelDiscovery();
                myBluetooth.startDiscovery();
            }
        });

        cancelBtn = (Button) findViewById(R.id.button2);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket!=null) //If the btSocket is busy
                {
                    try
                    {
                        btSocket.close(); //close connection
                    }
                    catch (IOException e)
                    { msg("Error");}
                    if(myBluetooth.isDiscovering()) myBluetooth.cancelDiscovery();
                    resetButtons();
                    isBtConnected = false;
                }
            }
        });

        redDot = (ImageView) findViewById(R.id.imageView2);
        greenDot = (ImageView) findViewById(R.id.imageView);
        greenDot.setVisibility(View.INVISIBLE);
    }

    private void bluetoothSetup() {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null) {
            msg("Bluetooth Device Not Available");
            finish();
        }
        if(!myBluetooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        Thread t = new Thread() {
            public void run() {
                try {
                    while(!myBluetooth.isEnabled()) Thread.sleep(10);
                } catch (Exception e) {
                }
                pairedDevices = myBluetooth.getBondedDevices();
                if(myBluetooth.isDiscovering()) myBluetooth.cancelDiscovery();
                myBluetooth.startDiscovery();
            }
        };
        t.start();

        br = new MyBroadcastReceiver();
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(BluetoothDevice.ACTION_FOUND);
        ifilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(br, ifilter);

    }

    private void connect(String address) {
        myBluetooth.cancelDiscovery();
        this.address = address;
        new ConnectBT().execute();
    }

    private boolean isPaired(BluetoothDevice device) {
        return pairedDevices.contains(device);
    }

    private void resetButtons() {
        openBtn.setEnabled(false);
        redDot.setVisibility(View.VISIBLE);
        greenDot.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                resetButtons();
            }

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(isPaired(device) && device.getAddress().equals(HC_ADDRESS)) {
                    connect(device.getAddress());
                }
            }
        }

    }


    private class ConnectBT extends AsyncTask<Void, Void, Void> {

        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            greenDot.setVisibility(View.VISIBLE);
            redDot.setVisibility(View.INVISIBLE);
            greenDot.startAnimation(animation);
            //progress = ProgressDialog.show(MainActivity.this,"Connecting...", "Please wait!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            if(btSocket == null || !isBtConnected) {
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                Toast.makeText(getApplicationContext(),"Connection failed. Try again.",Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getApplicationContext(),"Connected.",Toast.LENGTH_LONG).show();
                greenDot.clearAnimation();
                openBtn.setEnabled(true);
                isBtConnected = true;
            }
            //progress.dismiss();
        }
    }

}
