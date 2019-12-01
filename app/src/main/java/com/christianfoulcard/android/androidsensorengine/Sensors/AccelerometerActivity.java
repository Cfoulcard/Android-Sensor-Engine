package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.christianfoulcard.android.androidsensorengine.R;
import com.google.firebase.analytics.FirebaseAnalytics;

//Permission for accessing GPS location -- needed to track speed
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

////////////////////////////////////////////////////////////////////////////////////////////////////
//LocationListener needed to track speed
public class AccelerometerActivity extends AppCompatActivity implements LocationListener {

    TextView accelerometer_sensor;
    TextView accelerometer;
    TextView currentSpeed;
    LocationListener mlocListener;


    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    //Needed for location permission
    private final int REQUEST_LOCATION_PERMISSION = 1;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_sensor);

        //TextViews
        accelerometer = (TextView) findViewById(R.id.accelerometer);
        currentSpeed = (TextView) findViewById(R.id.current_speed);
        accelerometer_sensor = (TextView) findViewById(R.id.accelerometer_sensor);

        //Animation fade in for TextViews
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        accelerometer.startAnimation(in);
        currentSpeed.startAnimation(in);
        accelerometer_sensor.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Calls the location permission dialog box upon opening this activity
        requestLocationPermissions();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
//This section parses the speed data when the permissions are granted
    @Override
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.onLocationChanged(null);
    }

    //Upon destroying the activity the location data will terminate
    //Used to free memory/battery usage
    public  void onDestroy()
    {
        super.onDestroy();
      LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        lm.removeUpdates(this);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    //This will add functionality to the menu button within the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //Speed Formula for the Accelerometer
    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            // if you can't get speed because reasons :)
            currentSpeed.setText("0 mph");
        } else {
            //int speed=(int) ((location.getSpeed()) is the standard which returns meters per second.
            //int speed=(int) ((location.getSpeed()*3600)/1000); //This is speed in km/h
            int speed = (int) (location.getSpeed() * 2.2369); //This is speed in mph

            //Refer to this link for more speed data https://www.wikiwand.com/en/Speed

            currentSpeed.setText(speed + " mph");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //This section is is dedicated for the location permission properties
    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    ACCESS_FINE_LOCATION)) {
                //  Toast.makeText(this, "Please grant permission to measure your speed", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}













/*public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    TextView currentSpeed;
    private SensorManager sensorManager;
    private Sensor speed;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_sensor);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        speed = sensorManager.getDefaultSensor((Sensor.TYPE_LINEAR_ACCELERATION));
        currentSpeed = (TextView) findViewById(R.id.current_speed);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    //Main magic of the Speed Sensor
    @Override
    public final void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
        {
            currentSpeed.setText(event.values[0] + " mph" );
        }
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        //The maxReportLatencyUs is defined in microseconds. Convert the units to define desired update time
        // https://stackoverflow.com/questions/46921111/android-sensor-manager-delay-setting-it-to-a-custom-value
        sensorManager.registerListener(this, speed, SensorManager.SENSOR_DELAY_NORMAL, 1000000);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    }
}*/
