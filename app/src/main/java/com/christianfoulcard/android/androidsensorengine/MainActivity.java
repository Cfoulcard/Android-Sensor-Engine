package com.christianfoulcard.android.androidsensorengine;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.christianfoulcard.android.androidsensorengine.Menu_Items.About;
import com.christianfoulcard.android.androidsensorengine.Menu_Items.Credits;
import com.christianfoulcard.android.androidsensorengine.Menu_Items.Premium;
import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.AccelerometerActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.BatteryActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.HumidityActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.LightSensorActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.PressureActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.RamActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.SoundSensorActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.TemperatureActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.WalkActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

////////////////////////////////////////////////////////////////////////////////////////////////////

public class MainActivity extends AppCompatActivity {

    //TODO: Show user a list of sensors their device can use
    //TODO: Add elevation/sea level sensor?
    //TODO: Go through each activity's lifecycle
    //TODO: Fix animations
    //TODO: Fix/String in other languages
    //TODO Custom Sounds?
    //TODO: Notification screen cleanup
    //TODO: Fix Pressure Notifications

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Make sure this theme is before calling super.onCreate
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_selection);

        // This will make the Status Bar completely transparent
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Obtain the FirebaseAnalytics instance and Initiate Firebase Analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Intents to start the sensor activities

    public void soundIconIntent(View view) {
        Intent soundIntent = new Intent(this, SoundSensorActivity.class);

        ImageView sharedView = findViewById(R.id.sound_icon);
        String transitionName = getString(R.string.sound_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(soundIntent, transitionActivityOptions.toBundle());
    }

    public void tempIconIntent(View view) {
        Intent tempIntent = new Intent(this, TemperatureActivity.class);

        ImageView sharedView = findViewById(R.id.temp_icon);
        String transitionName = getString(R.string.temp_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(tempIntent, transitionActivityOptions.toBundle());
    }

    public void lightIconIntent(View view) {
        Intent lightIntent = new Intent(this, LightSensorActivity.class);

        ImageView sharedView = findViewById(R.id.light_icon);
        String transitionName = getString(R.string.light_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(lightIntent, transitionActivityOptions.toBundle());
    }

    public void ramIconIntent(View view) {
        Intent ramIntent = new Intent(this, RamActivity.class);

        ImageView sharedView = findViewById(R.id.ram_icon);
        String transitionName = getString(R.string.ram_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(ramIntent, transitionActivityOptions.toBundle());
    }

    public void batteryIconIntent(View view) {
        Intent batteryIntent = new Intent(this, BatteryActivity.class);

        ImageView sharedView = findViewById(R.id.battery_icon);
        String transitionName = getString(R.string.battery_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(batteryIntent, transitionActivityOptions.toBundle());
    }

    public void speedIconIntent(View view) {
        Intent speedIntent = new Intent(this, AccelerometerActivity.class);

        ImageView sharedView = findViewById(R.id.speed_icon);
        String transitionName = getString(R.string.speed_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(speedIntent, transitionActivityOptions.toBundle());
    }

    public void humidityIconIntent(View view) {
        Intent humidityIntent = new Intent(this, HumidityActivity.class);

        ImageView sharedView = findViewById(R.id.humidity_icon);
        String transitionName = "";

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(humidityIntent, transitionActivityOptions.toBundle());
    }

    public void pressureIconIntent(View view) {
        Intent pressureIntent = new Intent(this, PressureActivity.class);

        ImageView sharedView = findViewById(R.id.pressure_icon);
        String transitionName = "";

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(pressureIntent, transitionActivityOptions.toBundle());
    }

    public void walkIconIntent(View view) {
        Intent walkIntent = new Intent(this, WalkActivity.class);

        ImageView sharedView = findViewById(R.id.walk_icon);
        String transitionName = "";

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(walkIntent, transitionActivityOptions.toBundle());
    }
}