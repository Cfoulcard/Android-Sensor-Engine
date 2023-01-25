//package com.christianfoulcard.android.androidsensorengine.preferences
//
//import android.app.Dialog
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.christianfoulcard.android.androidsensorengine.R
//import androidx.preference.PreferenceFragmentCompat
//import androidx.preference.EditTextPreference.OnBindEditTextListener
//import android.widget.EditText
//import android.text.InputType
//import androidx.preference.EditTextPreference
//import androidx.preference.ListPreference
//import androidx.preference.SwitchPreference
//import java.util.*
//
//class SettingsActivity : AppCompatActivity() {
//    var inputError: Dialog? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setTheme(R.style.PreferencesTheme)
//        setContentView(R.layout.settings_activity)
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.settings, SettingsFragment())
//            .commit()
//        val actionBar = supportActionBar
//        actionBar?.setDisplayHomeAsUpEnabled(true)
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
//
//    class SettingsFragment : PreferenceFragmentCompat() {
//        override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
//            setPreferencesFromResource(R.xml.root_preferences, rootKey)
//            val listPreference1 = findPreference<ListPreference>("airtempunit")
//            val listPreference2 = findPreference<ListPreference>("batterytempunit")
//            val listPreference3 = findPreference<ListPreference>("speedunit")
//            val switchPreference1 = findPreference<SwitchPreference>("switch_preference_battery")
//            val switchPreference2 = findPreference<SwitchPreference>("switch_preference_air")
//            val switchPreference3 = findPreference<SwitchPreference>("switch_preference_speed")
//            val switchPreference4 = findPreference<SwitchPreference>("switch_preference_pressure")
//            val switchPreference5 = findPreference<SwitchPreference>("switch_preference_humidity")
//            val editTextPreferenceBattery =
//                findPreference<EditTextPreference>("edit_text_battery_temp")
//            Objects.requireNonNull(editTextPreferenceBattery)
//                ?.setOnBindEditTextListener { editText ->
//                    editText.inputType =
//                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
//                }
//            val editTextPreferenceAir = findPreference<EditTextPreference>("edit_text_air_temp")
//            Objects.requireNonNull(editTextPreferenceAir)
//                ?.setOnBindEditTextListener { editText ->
//                    editText.inputType =
//                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
//
///*                    if (editTextPreferenceAir.getText().toString().equals("")) {
//                        editTextPreferenceAir.setText("0");
//                    }*/
//                }
//            val editTextPreferenceSpeed = findPreference<EditTextPreference>("edit_text_speed")
//            Objects.requireNonNull(editTextPreferenceSpeed)
//                ?.setOnBindEditTextListener { editText ->
//                    editText.inputType =
//                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
//                }
//            val editTextPreferencePressure =
//                findPreference<EditTextPreference>("edit_text_pressure")
//            Objects.requireNonNull(editTextPreferencePressure)
//                ?.setOnBindEditTextListener { editText ->
//                    editText.inputType =
//                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
//                }
//            val editTextPreferenceHumidity =
//                findPreference<EditTextPreference>("edit_text_humidity")
//            Objects.requireNonNull(editTextPreferenceHumidity)
//                ?.setOnBindEditTextListener { editText ->
//                    editText.inputType =
//                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
//                }
//        }
//    }
//}