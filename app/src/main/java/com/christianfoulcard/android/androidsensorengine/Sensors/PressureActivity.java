package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class PressureActivity extends AppCompatActivity implements SensorEventListener {

    //Dialog popup info
    Dialog pressureInfoDialog;

    //TextViews
    TextView pressure_text;
    TextView currentPressure;
    TextView pressureLevel;

    //ImsgeViews
    ImageView pressureInfo;

    //Sensor initiation
    private SensorManager sensorManager;
    private Sensor pressure;
    private Context mContext;
    private Activity mActivity;

    private SharedPreferences mSharedPreferences;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeSensors);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pressure_sensor);

        //TextViews
        pressure_text = (TextView) findViewById(R.id.pressure);
        currentPressure = (TextView) findViewById(R.id.current_pressure);
        pressureLevel = (TextView) findViewById(R.id.pressure_sensor);

        //ImageViews
        pressureInfo = (ImageView) findViewById(R.id.info_button);

        //Dialog Box for Temperature Info
        pressureInfoDialog = new Dialog(this);

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        pressure_text.startAnimation(in);
        currentPressure.startAnimation(in);
        pressureLevel.startAnimation(in);
        pressureInfo.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // the relative temperature. If device does not support this sensor a toast message will
        // appear
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show();
        }

        // Ambient Temperature measures the temperature around the device
        pressure = sensorManager.getDefaultSensor((Sensor.TYPE_PRESSURE));
        currentPressure = (TextView) findViewById(R.id.current_pressure);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = PressureActivity.this;

        int pressure_level = (int) event.values[0];

        //The default Android properties for event.values[0] is the formula for Celsius
        //This is for Fahrenheit
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            currentPressure.setText(pressure_level + " hPa");

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void showPressureDialogPopup(View v) {
        pressureInfoDialog.setContentView(R.layout.pressure_popup_info);

        pressureInfoDialog.show();
    }

    public void closePressureDialogPopup(View v) {
        pressureInfoDialog.setContentView(R.layout.pressure_popup_info);

        pressureInfoDialog.dismiss();
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

