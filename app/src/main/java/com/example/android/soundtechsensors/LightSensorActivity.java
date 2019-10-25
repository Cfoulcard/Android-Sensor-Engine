package com.example.android.soundtechsensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class LightSensorActivity extends Activity implements SensorEventListener {



    private SensorManager sensorManager;
    private Sensor light;
    TextView currentLux;

    {
        setContentView(R.layout.lux_sensor);

    // Get an instance of the sensor service, and use that to get an instance of
    // a particular sensor.
    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    currentLux = (TextView) findViewById(R.id.current_lux);


}

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lumens = event.values[0];
        // Do something with this sensor data.

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

}
