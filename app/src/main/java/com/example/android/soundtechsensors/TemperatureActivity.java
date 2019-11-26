package com.example.android.soundtechsensors;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

// TODO add menu

public class TemperatureActivity extends AppCompatActivity implements SensorEventListener {

    TextView temperature_text;
    TextView currentDegrees;
    TextView currentDegreesF;
    TextView currentDegreesK;
    private SensorManager sensorManager;
    private Sensor temperature;
    private Context mContext;
    private Activity mActivity;
    private SharedPreferences mSharedPreferences;



    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_sensor);

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = TemperatureActivity.this;

        // Get the instance of SharedPreferences object
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        temperature_text = (TextView) findViewById(R.id.temperature);
        currentDegrees = (TextView) findViewById(R.id.current_temp);
        currentDegreesF = (TextView) findViewById(R.id.current_temp_f);
      //  currentDegreesK = (TextView) findViewById(R.id.current_temp);


        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        temperature_text.startAnimation(in);
        currentDegrees.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // the surrounding temperature. If device does not support this sensor a toast message will
        // appear
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == null){
            Toast.makeText(this, "Your device does not support this sensor", Toast.LENGTH_LONG).show();
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
        String temp_unit = mSharedPreferences.getString(getString(R.string.temperature_measurement), null);



        String temp_unit2 = mSharedPreferences.getString(getString(R.string.temperature_measurement), String.valueOf(1));
            int temp_id_celsius = mSharedPreferences.getInt("Celsius", 0);
            int temp_id_fahrenheit = mSharedPreferences.getInt("Fahrenheit", 1);
            int temp_id_kelvin = mSharedPreferences.getInt(" Kelvin", 2);

            //The default Android properties for event.values[0] is the formula for Celsius
            if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE
            && temp_id_celsius == 0) {
                    currentDegrees.setText(String.format("%s Cgfr", event.values[0]));
                }

            //The following converts celsius into fahrenheit
            if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE && temp_id_fahrenheit == 1) {
                    int b = (int) event.values[0];
                    int c = b * 9 / 5 + 32;
                    currentDegrees.setText(c + " F");
                }
            }


/*
            if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                if (temp_id_kelvin == 1) {
                    int b = (int) event.values[0];
                    int k = (int) (b + 273.15);
                    currentDegrees.setText(k + " " + temp_unit);
                }
            }*/




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
    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }
}

