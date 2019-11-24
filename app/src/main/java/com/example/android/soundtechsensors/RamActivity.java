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
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import static com.example.android.soundtechsensors.R.layout.ram_sensor;



public class RamActivity extends AppCompatActivity {

    TextView ramText;
    TextView currentRam;
    private SensorManager sensorManager;
    private Sensor ram;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ram_sensor);

        ramText = (TextView) findViewById(R.id.ram);
        currentRam = (TextView) findViewById(R.id.current_ram);

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        ramText.startAnimation(in);
        currentRam.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        currentRam = (TextView) findViewById(R.id.current_ram);
        String i = String.valueOf(getMemorySize());
        currentRam.setText((i) + " mB");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int getMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;

//Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;

        return (int) availableMegs;
    }

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

}