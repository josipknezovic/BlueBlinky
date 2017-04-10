package fer.jureknezovic.smartlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;

import static fer.jureknezovic.smartlock.MainActivity.btSocket;

public class PasswordSettingsActivity extends AppCompatActivity {
    private SharedPreferences settings;
    private EditText newPassword;
    private EditText newPasswordConfirm;
    private EditText oldPassword;
    private boolean vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_settings);
        settings = this.getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);
        vibrate = settings.getBoolean(getString(R.string.preference_vibrate), false);

        newPassword = (EditText) findViewById(R.id.newPassword);
        newPasswordConfirm = (EditText) findViewById(R.id.confirmNewPassword);
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        String defaultPassword = "1234";
        oldPassword.setText(settings.getString(getString(R.string.preference_password), defaultPassword));

        final Switch passwordToLock = (Switch) findViewById(R.id.passwordSwitch);
        if (MainActivity.isBtConnected) {
            passwordToLock.setChecked(settings.getBoolean(getString(R.string.preference_sensor), true));
            passwordToLock.setEnabled(true);
        } else {
            passwordToLock.setChecked(false);
            passwordToLock.setEnabled(false);
        }
        ImageButton applyButton = (ImageButton) findViewById(R.id.buttonApplyPassword);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordsEmpty()) {
                    showToast(getString(R.string.settings_password_error));
                } else {
                    if (passwordsMatch()) {
                        saveNewPassword();
                        if (MainActivity.isBtConnected && passwordToLock.isChecked()) {
                            char[] newPasswordField = newPasswordConfirm.getText().toString().toCharArray();
                            char[] oldPasswordField = oldPassword.getText().toString().toCharArray();
                            int i = 0;
                            for (char sign : oldPasswordField) {
                                oldPasswordField[i] = (char) (sign + 16);
                            }
                            i = 0;
                            for (char sign : newPasswordField) {
                                newPasswordField[i] = (char) (sign + 48);
                                i++;
                            }
                            if (btSocket != null) {
                                String sendCommand = getString(R.string.controlCharOldPasswordStart) + String.valueOf(oldPasswordField) + getString(R.string.controlCharNewPasswordStart) + String.valueOf(newPasswordField) + getString(R.string.controlCharNewPasswordEnd);
                                try {
                                    btSocket.getOutputStream().write(sendCommand.getBytes());
                                } catch (IOException e) {
                                }
                            }
                        }
                        showToast(getString(R.string.settings_saved));
                        onBackPressed();
                    } else {
                        showToast(getString(R.string.passwordsError));
                    }
                }
                vibrate();
            }
        });
        ImageButton cancelButton = (ImageButton) findViewById(R.id.buttonCancelPassword);
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

    private void saveNewPassword() {
        SharedPreferences.Editor settingsEditor = settings.edit();
        settingsEditor.putString(getString(R.string.preference_password), newPassword.getText().toString());
        settingsEditor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vibrate = settings.getBoolean(getString(R.string.preference_vibrate), false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private boolean passwordsMatch() {
        if (newPassword.getText().toString().equalsIgnoreCase(newPasswordConfirm.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method which checks whether one of passwords is empty.
     *
     * @return true if one of passwords is empty; false otherwise.
     */
    private boolean passwordsEmpty() {
        if (newPassword.getText().toString().isEmpty() || newPasswordConfirm.getText().toString().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
