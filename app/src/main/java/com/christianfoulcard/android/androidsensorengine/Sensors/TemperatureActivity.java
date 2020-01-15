package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity;
import com.christianfoulcard.android.androidsensorengine.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class TemperatureActivity extends AppCompatActivity implements SensorEventListener {

    //Dialog popup info
    Dialog tempInfoDialog;

    //TextViews
    TextView temperature_text;
    TextView currentDegrees;
    TextView airTemp;

    //ImsgeViews
    ImageView tempInfo;

    //Sensor initiation
    private SensorManager sensorManager;
    private Sensor temperature;
    private Context mContext;
    private Activity mActivity;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeSensors);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_sensor);

        //TextViews
        temperature_text = (TextView) findViewById(R.id.temperature);
        currentDegrees = (TextView) findViewById(R.id.current_temp);
        airTemp = (TextView) findViewById(R.id.temperature_sensor);

        //ImageViews
        tempInfo = (ImageView) findViewById(R.id.info_button);

        //Dialog Box for Temperature Info
        tempInfoDialog = new Dialog(this);



        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // the surrounding temperature. If device does not support this sensor a toast message will
        // appear
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show();
        }

        // Ambient Temperature measures the temperature around the device
        temperature = sensorManager.getDefaultSensor((Sensor.TYPE_AMBIENT_TEMPERATURE));
        currentDegrees = (TextView) findViewById(R.id.current_temp);


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    //Main magic of the Temperature Sensor
    @Override
    public final void onSensorChanged(SensorEvent event) {

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = TemperatureActivity.this;

        // Get the instance of SharedPreferences object
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        //Get the string data from the Preferences
        String unit = settings.getString("airtempunit", "");

        //Calculates Celsius
        int c = (int) event.values[0];
        //Calculates Fahrenheit
        int f = c * 9 / 5 + 32;
        //Calculates Kelvin
        int k = (int) (c + 273.15);

        //Finds the preference string value and links it with the appropriate temperature calc formula
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            switch (unit) {
                case "C°":
                    currentDegrees.setText(c + " " + unit);
                    break;
                case "F°":
                    currentDegrees.setText(f + " " + unit);
                    break;
                case "K°":
                    currentDegrees.setText(k + " " + unit);
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        temperature_text.startAnimation(in);
        currentDegrees.startAnimation(in);
        airTemp.startAnimation(in);
        tempInfo.startAnimation(in);
        super.onStart();
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void showTempDialogPopup(View v) {
        tempInfoDialog.setContentView(R.layout.temperature_popup_info);

        tempInfoDialog.show();
    }

    public void closeTempDialogPopup(View v) {
        tempInfoDialog.setContentView(R.layout.temperature_popup_info);

        tempInfoDialog.dismiss();
    }



    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    //The following is for the menu items within the navigation_menu.xml file
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                Intent configurationsIntent = new Intent(this, SettingsActivity.class);
                this.startActivity(configurationsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

