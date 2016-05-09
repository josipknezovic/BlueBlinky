package com.example.mate.blueblinky;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    Button searchBtn;
    ListView deviceList;
    ListView pairedList;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter myArrayAdapter = null;
    private MyBroadcastReceiver br = null;

    public static String EXTRA_ADDRESS = "device_address";
    public static String HC_ADDRESS = "20:14:03:24:28:61";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        searchBtn = (Button) findViewById(R.id.button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myArrayAdapter.clear();
                myBluetooth.startDiscovery();
            }
        });

        deviceList = (ListView) findViewById(R.id.listView);
        deviceList.setAdapter(myArrayAdapter);
        deviceList.setOnItemClickListener(myListClickListener);

        pairedList = (ListView) findViewById(R.id.listView2);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        }
        if(!myBluetooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        while(!myBluetooth.isEnabled());
        pairedDevicesList();

        if(myBluetooth.isDiscovering()) myBluetooth.cancelDiscovery();
        myBluetooth.startDiscovery();

        br = new MyBroadcastReceiver();
        IntentFilter ifilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(br, ifilter);


    }

    private void pairedDevicesList() {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if(pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }

        } else {
            Toast.makeText(getApplicationContext(), "No paired devices found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        pairedList.setAdapter(adapter);
        pairedList.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {

            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            connect(address);
        }
    };

    private void connect(String address) {
        myBluetooth.cancelDiscovery();
        Intent i = new Intent(MainActivity.this, ledControl.class);
        i.putExtra(EXTRA_ADDRESS, address);
        startActivity(i);
    }

    private boolean isPaired(BluetoothDevice device) {
        return pairedDevices.contains(device);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                myArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                if(isPaired(device)) connect(device.getAddress());
            }
        }

    }

}
