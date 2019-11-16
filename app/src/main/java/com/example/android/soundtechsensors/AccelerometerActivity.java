package com.example.android.soundtechsensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    TextView currentSpeed;
    private SensorManager sensorManager;
    private Sensor speed;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_sensor);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        speed = sensorManager.getDefaultSensor((Sensor.TYPE_LINEAR_ACCELERATION));
        currentSpeed = (TextView) findViewById(R.id.current_speed);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    //Main magic of the Speed Sensor
    @Override
    public final void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
        {
            currentSpeed.setText(event.values[0] + " mph" );
        }
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        //The maxReportLatencyUs is defined in microseconds. Convert the units to define desired update time
        // https://stackoverflow.com/questions/46921111/android-sensor-manager-delay-setting-it-to-a-custom-value
        sensorManager.registerListener(this, speed, SensorManager.SENSOR_DELAY_NORMAL, 1000000);
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
