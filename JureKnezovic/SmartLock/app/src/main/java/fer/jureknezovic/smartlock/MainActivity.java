package fer.jureknezovic.smartlock;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BluetoothAdapter myBluetoothAdapter = null;
    public static BluetoothSocket btSocket = null;
    public static boolean isBtConnected = false;
    private SharedPreferences settings;
    private ImageButton openButton;
    private ImageButton scanButton;
    private ImageButton disconnectButton;
    ProgressBar progressBar;
    AsyncTask connectingBT;
    boolean vibrate;
    // SPP UUID service
    private static final UUID myUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String hc06MacAddressV1 = "20:14:03:24:28:61";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = this.getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);
        vibrate = settings.getBoolean(getString(R.string.preference_vibrate), false);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        registerReceiver(screenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));


        openButton = (ImageButton) findViewById(R.id.ButtonOpen);
        //blokiraj tipku na pocetku
        openButton.setEnabled(false);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String defaultPassword = "1234";
                String currentPassword = settings.getString(getString(R.string.preference_password), defaultPassword);
                char[] returnPassword = currentPassword.toCharArray();
                int i = 0;
                for (char sign : returnPassword) {
                    returnPassword[i] = (char) (sign + 16);
                    i++;
                }
                AnimationSet s = new AnimationSet(false);
                s.addAnimation(animAlpha);
                s.addAnimation(animScale);
                v.startAnimation(s);
                if (btSocket != null) {
                    String sendCommand = getString(R.string.controlCharInputPasswordStart) + String.valueOf(returnPassword) + getString(R.string.controlCharInputPasswordEnd);
                    try {
                        btSocket.getOutputStream().write(sendCommand.getBytes());
                    } catch (IOException e) {
                    }
                }
                vibrate();
            }
        });

        scanButton = (ImageButton) findViewById(R.id.ButtonScan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableBluetoothIfDisabled();
                vibrate();
                v.startAnimation(animAlpha);
                connectingBT = new ConnectBT().execute();
            }
        });

        disconnectButton = (ImageButton) findViewById(R.id.ButtonDisconnect);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(animAlpha);

                if (connectingBT.getStatus() == AsyncTask.Status.RUNNING) {
                    connectingBT.cancel(false);
                    isBtConnected = false;
                    scanButton.setEnabled(false);
                    showToast(getString(R.string.scanning_interrupted));
                    progressBar.setVisibility(View.INVISIBLE);
                }
                if (connectingBT.getStatus() == AsyncTask.Status.FINISHED) {
                    if (isBtConnected) {
                        killConnection();
                        isBtConnected = false;
                        openButton.setEnabled(false);
                        showToast(getString(R.string.connection_broken));
                    } else {
                        showToast(getString(R.string.device_not_connected));
                    }
                }
                if (connectingBT.getStatus() == AsyncTask.Status.PENDING) {
                    showToast(getString(R.string.device_not_connected));
                }
                vibrate();
            }
        });
        bluetoothSetup();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        unCheckAllMenuItems(navigationView);
        vibrate = settings.getBoolean(getString(R.string.preference_vibrate), false);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        unCheckAllMenuItems(navigationView);
        item.setChecked(true);
        if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.setType("message/rcf822");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"jure.knezovic@fer.hr"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pogreška u aplikaciji " + R.string.app_name);
            startActivity(Intent.createChooser(emailIntent, "Pošalji e-mail pomoću: "));
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                killConnection();
                resetButtons();
            }
        }
    };


    @Override
    public void finish() {
        try {
            unregisterReceiver(screenOffReceiver);
        } catch (IllegalArgumentException e) {
            // Check wether we are in debug mode
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        super.finish();
    }

    /**
     * Method which starts bluetooth service
     */
    private void bluetoothSetup() {
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBluetoothAdapter == null) {
            showToast("Ne postoji Bluetooth podrška na uređaju.");
            finish();
        }
        enableBluetoothIfDisabled();
        connectingBT = new ConnectBT().execute();
    }

    private void enableBluetoothIfDisabled() {
        if (!myBluetoothAdapter.isEnabled()) {
            Intent turnBtOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBtOn, 1);
        }
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void resetButtons() {
        openButton.setEnabled(false);
        disconnectButton.setEnabled(true);
        scanButton.setEnabled(true);
    }

    private void unCheckAllMenuItems(NavigationView navigationView) {
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }

    private void vibrate() {
        if (vibrate) {
            Vibrator x = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            x.vibrate(75);
        }
    }

    @Override
    protected void onDestroy() {
        if (btSocket != null) {
            killConnection();
        }
        try {
            unregisterReceiver(screenOffReceiver);
        } catch (IllegalArgumentException e) {
            // Check wether we are in debug mode
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.ProgressCircle);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... devices) //dok proces vrti, bluetooth se spaja
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetoothAdapter.cancelDiscovery();
                    myBluetoothAdapter.startDiscovery();
                    BluetoothDevice bluetoothModule = myBluetoothAdapter.getRemoteDevice(hc06MacAddressV1);//connects to the device's address and checks if it's available
                    btSocket = bluetoothModule.createInsecureRfcommSocketToServiceRecord(myUuid);
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            if (ConnectSuccess) {
                //odblokiraj tipku ako smo se uspjesno spojili
                isBtConnected = true;
                openButton.setEnabled(true);
                showToast(getString(R.string.connection_successful));
            }
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onCancelled() {
            killConnection();
            isBtConnected = false;
            resetButtons();
        }
    }

    /**
     * Reset input and output streams and make sure socket is closed.
     * This method will be used during shutdown() to ensure that the connection is properly closed during a shutdown.
     *
     * @return
     */
    private void killConnection() {
        if (btSocket != null) {
            try {
                if (btSocket.getInputStream() != null) {
                    btSocket.getInputStream().close();
                }
            } catch (IOException e) {
            }
            try {
                if (btSocket.getOutputStream() != null) {
                    btSocket.getOutputStream().close();
                }
            } catch (IOException e) {
            }
            try {
                if (btSocket != null) {
                    btSocket.close();
                }
                btSocket = null;
            } catch (IOException e) {
            }
        }
    }
}
