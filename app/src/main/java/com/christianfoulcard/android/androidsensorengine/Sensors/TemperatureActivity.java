package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.christianfoulcard.android.androidsensorengine.R;
import com.google.firebase.analytics.FirebaseAnalytics;

// TODO add menu

public class TemperatureActivity extends AppCompatActivity implements SensorEventListener {

    TextView temperature_text;
    TextView currentDegrees;
    TextView airTemp;
    TextView currentDegreesTest;
    TextView currentDegreesK;
    private SensorManager sensorManager;
    private Sensor temperature;
    private Context mContext;
    private Activity mActivity;
    private SharedPreferences mSharedPreferences;


    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_sensor);


        temperature_text = (TextView) findViewById(R.id.temperature);
        currentDegrees = (TextView) findViewById(R.id.current_temp);
        airTemp = (TextView) findViewById(R.id.temperature_sensor);
        //  currentDegreesTest = (TextView) findViewById(R.id.currentdegreestest);
        //    currentDegreesF = (TextView) findViewById(R.id.current_temp_f);
        //  currentDegreesK = (TextView) findViewById(R.id.current_temp);


        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        temperature_text.startAnimation(in);
        currentDegrees.startAnimation(in);
        airTemp.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // the surrounding temperature. If device does not support this sensor a toast message will
        // appear
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show();
        }

        // Ambient Temperature measures the temperature around the device
        temperature = sensorManager.getDefaultSensor((Sensor.TYPE_AMBIENT_TEMPERATURE));
        currentDegrees = (TextView) findViewById(R.id.current_temp);


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    //Main magic of the Temperature Sensor
    @Override
    public final void onSensorChanged(SensorEvent event) {

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = TemperatureActivity.this;

        // Get the instance of SharedPreferences object
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("", "");
        editor.apply();
        editor.commit();


        int degrees;

        //  currentDegrees.setText(mSharedPreferences.getInt("Celsius", getResources().getStringArray(0)));
        String temp_unit_c = mSharedPreferences.getString(getString(R.string.temperature_measurement),
                mContext.getResources().getString(R.string.Celsius));

        String temp_unit_f = mSharedPreferences.getString(getString(R.string.temperature_measurement),
                mContext.getResources().getString(R.string.Fahrenheit));

        String temp_unit_k = mSharedPreferences.getString(getString(R.string.temperature_measurement),
                mContext.getResources().getString(R.string.Kelvin));

        int b = (int) event.values[0];
        int c = b * 9 / 5 + 32;
        int k = (int) (b + 273.15);


       /* if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE &&
                temp_unit == mSharedPreferences.getString(getString(R.string.temperature_measurement),
                        mContext.getResources().getString(R.string.temperature_measurement))) {

            currentDegrees.setText(temp_unit);
        }*/

        //The default Android properties for event.values[0] is the formula for Celsius
        //This is for Fahrenheit
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            currentDegrees.setText(c + " °F");

        }

        //The following converts celsius into fahrenheit
/*        else if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {

            currentDegrees.setText(c + " F");
        } else if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE &&
                temp_unit_k == mSharedPreferences.getString(getString(R.string.Kelvin),
                        mContext.getResources().getString(R.string.Kelvin))) {

            currentDegrees.setText(k + " °K");
        }*/


    }

/*    public void setColor(String newColorKey) {

        @ColorInt
        int temp;

        @ColorInt
        int trailColor;

        int b = 5435432;
        int z = 432432;
        int u = 768;
       // int c = b * 9 / 5 + 32;
      //  int k = (int) (b + 273.15);

        if (newColorKey.equals("Celsius")) {
            temp = getString()
        } else if (newColorKey.equals("Fahrenheit")) {
            currentDegrees.setText(z + " F");
        } else {
            currentDegrees.setText(u + " K");
        }

        currentDegreesTest.setText(temp);

*//*        mBassCircle.setShapeColor(shapeColor);
        mMidSquare.setShapeColor(shapeColor);
        mTrebleTriangle.setShapeColor(shapeColor);

        mBassCircle.setTrailColor(trailColor);
        mMidSquare.setTrailColor(trailColor);
        mTrebleTriangle.setTrailColor(trailColor);*//*
    }*/

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }
}

