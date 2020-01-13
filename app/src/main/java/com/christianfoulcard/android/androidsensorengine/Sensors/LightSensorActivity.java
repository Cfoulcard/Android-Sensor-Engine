package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity;
import com.christianfoulcard.android.androidsensorengine.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LightSensorActivity extends AppCompatActivity implements SensorEventListener {

    //Dialog popup info
    Dialog lightInfoDialog;

    //TextViews
    TextView luminosity;
    TextView currentLux;
    TextView lightSensor;

    //Sensor initiation
    private SensorManager sensorManager;
    private Sensor light;

    //ImsgeViews
    ImageView lightInfo;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeSensors);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lux_sensor);

        //TextViews
        luminosity = findViewById(R.id.luminosity);
        currentLux = findViewById(R.id.current_lux);
        lightSensor = findViewById(R.id.lux_sensor);

        //ImageViews
        lightInfo = (ImageView) findViewById(R.id.info_button);

        //Dialog Box for Temperature Info
        lightInfoDialog = new Dialog(this);

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        luminosity.startAnimation(in);
        currentLux.startAnimation(in);
        lightSensor.startAnimation(in);
        lightInfo.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // a the light sensor. If the device does not support this sensor a toast message
        // will appear
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show();
        }

        //Light sensor to measure light
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        currentLux = findViewById(R.id.current_lux);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    //Main magic of the Light Sensor
    @SuppressLint("SetTextI18n")
    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            currentLux.setText(event.values[0] + " lux");
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

    public void showLightDialogPopup(View v) {
        lightInfoDialog.setContentView(R.layout.lux_popup_info);

        lightInfoDialog.show();
    }

    public void closeLightDialogPopup(View v) {
        lightInfoDialog.setContentView(R.layout.lux_popup_info);

        lightInfoDialog.dismiss();
    }

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
                Intent configurationsIntent = new Intent(this, SettingsActivity.class);
                this.startActivity(configurationsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


