package fer.jureknezovic.smartlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

public class LockSettingsActivity extends AppCompatActivity {
    private final String[] clockString = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00",
            "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private static final int minValueHours = 0;
    private final String[] durationString = {"3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s"};
    private static final int minValueDuration = 3;
    private static final int maxValueDuration = 10;
    private boolean vibrate;
    private SharedPreferences settings;
    char defaultSensorOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_settings);
        settings = this.getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);
        vibrate = settings.getBoolean(getString(R.string.preference_vibrate), false);
        //preferences part
        final int numberBeginValue = 7;
        int numberPickerBeginValue = settings.getInt(getString(R.string.preference_begin_value), numberBeginValue);
        final int numberEndValue = 16;
        int numberPickerEndValue = settings.getInt(getString(R.string.preference_end_value), numberEndValue);
        final int durationValue = settings.getInt(getString(R.string.preference_duration_value), 4);
        //pocetak radnog vremena
        final NumberPicker shiftStart = (NumberPicker) findViewById(R.id.shiftStart);
        final int maxValueHours = 23;
        shiftStart.setMinValue(minValueHours);
        shiftStart.setMaxValue(maxValueHours);
        shiftStart.setDisplayedValues(clockString);
        shiftStart.setValue(numberPickerBeginValue);

        //kraj radnog vremena
        final NumberPicker shiftEnd = (NumberPicker) findViewById(R.id.shiftEnd);
        shiftEnd.setMinValue(minValueHours);
        shiftEnd.setMaxValue(maxValueHours);
        shiftEnd.setDisplayedValues(clockString);
        shiftEnd.setValue(numberPickerEndValue);

        //trajanje dozvoljenog ulaska
        final NumberPicker duration = (NumberPicker) findViewById(R.id.openDuration);
        duration.setMinValue(minValueDuration);
        duration.setMaxValue(maxValueDuration);
        duration.setDisplayedValues(durationString);
        duration.setValue(durationValue);

        //led switch
        final Switch ledSwitch = (Switch) findViewById(R.id.ledSwitch);
        ledSwitch.setChecked(settings.getBoolean(getString(R.string.preference_led), true));

        //sensor switch
        final Switch sensorSwitch = (Switch) findViewById(R.id.sensorSwitch);
        sensorSwitch.setChecked(settings.getBoolean(getString(R.string.preference_sensor), true));
        if (sensorSwitch.isChecked()) {
            defaultSensorOutput = 'A';
        } else {
            defaultSensorOutput = 'O';
        }
        ImageButton applyButton = (ImageButton) findViewById(R.id.buttonApplySettings);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator x = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                x.vibrate(75);
                Calendar c = Calendar.getInstance();
                int currentMinutes = c.get(Calendar.MINUTE);
                int currentHour = c.get(Calendar.HOUR_OF_DAY);
                if (!MainActivity.isBtConnected) {
                    showToast(getString(R.string.not_connected));
                } else {
                    char returnWorkingHours;
                    int shiftStartValue = shiftStart.getValue();
                    int shiftEndValue = shiftEnd.getValue();
                    //duljina radnog vremena
                    if (shiftStartValue > shiftEndValue) {
                        returnWorkingHours = (char) ('a' + 24 - shiftStartValue + shiftEndValue);
                    } else {
                        returnWorkingHours = (char) ('a' + shiftEndValue - shiftStartValue);
                    }
                    //trenutne minute
                    char returnMinutes;
                    returnMinutes = (char) ('A' + currentMinutes);
                    char returnCurrentCounter;
                    if (currentHour < shiftStartValue) {
                        returnCurrentCounter = (char) ('A' + 24 - shiftStartValue + currentHour);
                    } else {
                        returnCurrentCounter = (char) ('A' + currentHour - shiftStartValue);
                    }
                    //duljina dozvole ulaska
                    char returnOpenDuration = (char) ('A' + duration.getValue());

                    //aktivacija ledica
                    char ledOutput;
                    if (ledSwitch.isChecked()) {
                        ledOutput = 'a';
                    } else {
                        ledOutput = 'p';
                    }

                    //aktivacija senzora
                    char sensorOutput;
                    if (sensorSwitch.isChecked()) {
                        sensorOutput = 'A';
                    } else {
                        sensorOutput = 'O';
                    }
                    String returnStr = getString(R.string.controlCharLed) + ledOutput
                            + getString(R.string.controlCharSensor) + sensorOutput
                            + getString(R.string.controlCharWorkingHours) + returnWorkingHours
                            + getString(R.string.controlCharOpenDuration) + returnOpenDuration
                            + getString(R.string.controlCharMinutes) + returnMinutes
                            + getString(R.string.controlCharCurrentCounterHours) + returnCurrentCounter
                            + getString(R.string.controlCharSensor) + sensorOutput;

                    //Saving of preferences so they show up next time we load this activity
                    SharedPreferences.Editor settingsEditor = settings.edit();
                    settingsEditor.putInt(getString(R.string.preference_begin_value), shiftStartValue);
                    settingsEditor.putInt(getString(R.string.preference_end_value), shiftEndValue);
                    settingsEditor.putInt(getString(R.string.preference_duration_value), shiftEndValue);
                    settingsEditor.putBoolean(getString(R.string.preference_led), ledSwitch.isChecked());
                    settingsEditor.putBoolean(getString(R.string.preference_sensor), sensorSwitch.isChecked());
                    settingsEditor.apply();
                    try {
                        MainActivity.btSocket.getOutputStream().write(returnStr.getBytes());
                        showToast(getString(R.string.settings_saved));
                    } catch (IOException e) {
                    }
                }
                vibrate();
                onBackPressed();
            }
        });
        ImageButton cancelButton = (ImageButton) findViewById(R.id.buttonCancelTime);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                onBackPressed();
            }
        });
    }

    private void vibrate() {
        if (vibrate) {
            Vibrator x = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            x.vibrate(75);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        vibrate = settings.getBoolean(getString(R.string.preference_vibrate), false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
