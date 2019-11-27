package com.example.android.soundtechsensors.Sensors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.soundtechsensors.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LightSensorActivity extends AppCompatActivity implements SensorEventListener {

    TextView luminosity;
    TextView currentLux;
    private SensorManager sensorManager;
    private Sensor light;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lux_sensor);

        luminosity = (TextView) findViewById(R.id.luminosity);
        currentLux = (TextView) findViewById(R.id.current_lux);

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        luminosity.startAnimation(in);
        currentLux.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // a the light sensor. If the device does not support this sensor a toast message
        // will appear
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            Toast.makeText(this, "Your device does not support this sensor", Toast.LENGTH_LONG).show();
        }

        //Light sensor to measure light
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        currentLux = (TextView) findViewById(R.id.current_lux);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    //Main magic of the Light Sensor
    @Override
    public final void onSensorChanged(SensorEvent event) {
        if( event.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            currentLux.setText(event.values[0] + " lux" );
         //   currentLux.setTextColor(Color.CYAN);
        }
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
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


