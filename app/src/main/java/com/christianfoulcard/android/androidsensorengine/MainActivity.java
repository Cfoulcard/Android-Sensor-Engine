package com.christianfoulcard.android.androidsensorengine;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.christianfoulcard.android.androidsensorengine.Sensors.AccelerometerActivity;
import com.christianfoulcard.android.androidsensorengine.Menu_Items.About;
import com.christianfoulcard.android.androidsensorengine.Menu_Items.Credits;
import com.christianfoulcard.android.androidsensorengine.Menu_Items.Premium;
import com.christianfoulcard.android.androidsensorengine.Preferences.Configurations;
import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity;
import com.christianfoulcard.android.androidsensorengine.R;
import com.christianfoulcard.android.androidsensorengine.Sensors.BatteryActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.LightSensorActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.RamActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.SoundSensorActivity;
import com.christianfoulcard.android.androidsensorengine.Sensors.TemperatureActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    //TODO: Create about and credits menu activities
    //TODO: Show user a list of sensors their device can use
    //TODO: Fix XML Responsiveness on different devices


    //FUTURE IDEAS
    //Add new activity explaining what sensors are available on user's device

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    private Context mContext;
    private Activity mActivity;
    private SharedPreferences mSharedPreferences;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sensor_selection);

        // This will make the Status Bar completely transparent
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = MainActivity.this;

        // Get the instance of SharedPreferences object
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    //The following is for the menu items within the navigation_menu.xml file
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                Intent configurationsIntent = new Intent(this, Configurations.class);
                this.startActivity(configurationsIntent);
                return true;
            case R.id.credits:
                Intent creditsIntent = new Intent(this, Credits.class);
                this.startActivity(creditsIntent);
                return true;
            case R.id.about:
                Intent aboutIntent = new Intent(this, About.class);
                this.startActivity(aboutIntent);
                return true;
            case R.id.premium:
                Intent premiumIntent = new Intent(this, Premium.class);
                this.startActivity(premiumIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Go to the sensor_selection.xml to change the onClick values
     */

    public void soundIconIntent(View view) {
        Intent soundIntent = new Intent(this, SoundSensorActivity.class);

        ImageView sharedView = findViewById(R.id.sound_icon);
        String transitionName = getString(R.string.sound_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(soundIntent, transitionActivityOptions.toBundle());

        // this.startActivity(soundIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void tempIconIntent(View view) {
        Intent tempIntent = new Intent(this, TemperatureActivity.class);

        ImageView sharedView = findViewById(R.id.temp_icon);
        String transitionName = getString(R.string.temp_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(tempIntent, transitionActivityOptions.toBundle());

        // this.startActivity(tempIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void lightIconIntent(View view) {
        Intent lightIntent = new Intent(this, LightSensorActivity.class);

        ImageView sharedView = findViewById(R.id.light_icon);
        String transitionName = getString(R.string.light_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(lightIntent, transitionActivityOptions.toBundle());
        // this.startActivity(lightIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void ramIconIntent(View view) {
        Intent ramIntent = new Intent(this, RamActivity.class);

        ImageView sharedView = findViewById(R.id.ram_icon);
        String transitionName = getString(R.string.ram_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(ramIntent, transitionActivityOptions.toBundle());
        //    this.startActivity(ramIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void batteryIconIntent(View view) {
        Intent batteryIntent = new Intent(this, BatteryActivity.class);

        ImageView sharedView = findViewById(R.id.battery_icon);
        String transitionName = getString(R.string.battery_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(batteryIntent, transitionActivityOptions.toBundle());
        //   this.startActivity(soundIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void speedIconIntent(View view) {
        Intent speedIntent = new Intent(this, AccelerometerActivity.class);

        ImageView sharedView = findViewById(R.id.speed_icon);
        String transitionName = getString(R.string.speed_anim);

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
        this.startActivity(speedIntent, transitionActivityOptions.toBundle());
        //   this.startActivity(speedIntent);
        // Toast.makeText(view.getContext(),"Button Clicked",Toast.LENGTH_LONG).show();
    }

    public void prefIconIntent(View view) {
        Intent prefIntent = new Intent(this, SettingsActivity.class);

        this.startActivity(prefIntent);

    }
}