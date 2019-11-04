package com.example.android.soundtechsensors;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.example.android.soundtechsensors.R.layout.ram_sensor;

public class RamActivity extends AppCompatActivity {

    TextView currentRam;
    private SensorManager sensorManager;
    private Sensor ram;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ram_sensor);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        currentRam = (TextView) findViewById(R.id.current_ram);
        String i = String.valueOf(getMemorySize());
        currentRam.setText((i) + " mB");
    }

    public int getMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;

//Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;

        return (int) availableMegs;
    }

}