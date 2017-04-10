package fer.jureknezovic.smartlock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences settings;
    private boolean vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = this.getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);
        final SharedPreferences.Editor settingsEditor = settings.edit();
        vibrate = settings.getBoolean(getString(R.string.preference_vibrate), false);


        Switch vibrateSwitch = (Switch) findViewById(R.id.vibrate);
        vibrateSwitch.setChecked(vibrate);
        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vibrate();
                    settingsEditor.putBoolean(getString(R.string.preference_vibrate), true);
                } else {
                    settingsEditor.putBoolean(getString(R.string.preference_vibrate), false);
                }
                settingsEditor.apply();
            }
        });
        TextView lockSettings = (TextView) findViewById(R.id.settings_basic);
        lockSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LockSettingsActivity.class);
                startActivity(intent);
            }
        });

        TextView passwordSettings = (TextView) findViewById(R.id.settings_password);
        passwordSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswordSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void vibrate() {
        Vibrator x = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        x.vibrate(150);
    }
}
