package com.example.android.soundtechsensors;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

//TODO: Make going into other Activities responsive

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_selection);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    /**
     * Go to the sensor_selection.xml to change the onClick values
     */

    public void soundIconIntent(View view) {
        Intent soundIntent = new Intent(this, SoundSensorActivity.class);
        this.startActivity(soundIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void tempIconIntent(View view) {
        Intent tempIntent = new Intent(this, TemperatureActivity.class);
        this.startActivity(tempIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void lightIconIntent(View view) {
        Intent lightIntent = new Intent(this, LightSensorActivity.class);
        this.startActivity(lightIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void ramIconIntent(View view) {
        Intent ramIntent = new Intent(this, RamActivity.class);
        this.startActivity(ramIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void batteryIconIntent(View view) {
        Intent soundIntent = new Intent(this, BatteryActivity.class);
        this.startActivity(soundIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void speedIconIntent(View view) {
        Intent soundIntent = new Intent(this, SoundSensorActivity.class);
        this.startActivity(soundIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }
}