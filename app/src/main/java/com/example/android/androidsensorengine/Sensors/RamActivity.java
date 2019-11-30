package com.example.android.androidsensorengine.Sensors;

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

import com.example.android.androidsensorengine.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.w3c.dom.Text;

import static com.example.android.androidsensorengine.R.layout.ram_sensor;


public class RamActivity extends AppCompatActivity {

    TextView ramText;
    TextView currentRam;
    TextView ramSensor;
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

        //TextViews
        ramText = findViewById(R.id.ram);
        currentRam = findViewById(R.id.current_ram);
        ramSensor = findViewById(R.id.ram_sensor);

        //Animation for TextView fade in
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        ramText.startAnimation(in);
        currentRam.startAnimation(in);
        ramSensor.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        currentRam = findViewById(R.id.current_ram);
        long i = (getMemorySize());
        currentRam.setText((i) + " mB");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public int getMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;

//Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double) mi.totalMem * 100.0;

        return (int) availableMegs;
    }

    private void unbindService(String activityService) {
    }

    //Unbind ACTIVITY_SERVICE to release ram resources
    public void onDestroy() {
        super.onDestroy();

        unbindService(ACTIVITY_SERVICE);
    }

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }
}