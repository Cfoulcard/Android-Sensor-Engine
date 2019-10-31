package com.example.android.soundtechsensors;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class RamActivity extends AppCompatActivity {

    TextView currentRam;
    TextView realCurrentRam;
    private SensorManager sensorManager;
    private Sensor ram;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ram_sensor);


        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 0x100000L;

//Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;


        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        currentRam = (TextView) findViewById(R.id.current_ram);
        // realCurrentRam = (TextView) findViewById(R.id.ram);

        currentRam.setText((int) availableMegs);

    }

}