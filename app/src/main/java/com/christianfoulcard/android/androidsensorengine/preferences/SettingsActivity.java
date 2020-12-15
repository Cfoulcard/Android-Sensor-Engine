package com.christianfoulcard.android.androidsensorengine.preferences;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.christianfoulcard.android.androidsensorengine.R;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    Dialog inputError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.PreferencesTheme);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            ListPreference listPreference1 = findPreference("airtempunit");
            ListPreference listPreference2 = findPreference("batterytempunit");
            ListPreference listPreference3 = findPreference("speedunit");
            SwitchPreference switchPreference1 = findPreference("switch_preference_battery");
            SwitchPreference switchPreference2 = findPreference("switch_preference_air");
            SwitchPreference switchPreference3 = findPreference("switch_preference_speed");
            SwitchPreference switchPreference4 = findPreference("switch_preference_pressure");
            SwitchPreference switchPreference5 = findPreference("switch_preference_humidity");



            EditTextPreference editTextPreferenceBattery = findPreference("edit_text_battery_temp");
            Objects.requireNonNull(editTextPreferenceBattery).setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

                }
            });

            final EditTextPreference editTextPreferenceAir = findPreference("edit_text_air_temp");
            Objects.requireNonNull(editTextPreferenceAir).setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener() {

                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

/*                    if (editTextPreferenceAir.getText().toString().equals("")) {
                        editTextPreferenceAir.setText("0");
                    }*/
                }
            });

            EditTextPreference editTextPreferenceSpeed = findPreference("edit_text_speed");
            Objects.requireNonNull(editTextPreferenceSpeed).setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

                }
            });

            final EditTextPreference editTextPreferencePressure = findPreference("edit_text_pressure");
            Objects.requireNonNull(editTextPreferencePressure).setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
            });

            EditTextPreference editTextPreferenceHumidity = findPreference("edit_text_humidity");
            Objects.requireNonNull(editTextPreferenceHumidity).setOnBindEditTextListener(new androidx.preference.EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

                }
            });
        }
    }
}