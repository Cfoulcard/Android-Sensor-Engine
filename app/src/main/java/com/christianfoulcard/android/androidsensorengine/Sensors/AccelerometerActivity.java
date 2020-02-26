package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
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

//Permission for accessing GPS location -- needed to track speed
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

////////////////////////////////////////////////////////////////////////////////////////////////////
//LocationListener needed to track speed
public class AccelerometerActivity extends AppCompatActivity implements LocationListener {

    //ID used for notifications
    private static final String CHANNEL_ID = "444";

    //Dialog popup info
    Dialog accelerometerInfoDialog;

    //TextViews
    TextView accelerometer_sensor;
    TextView accelerometer;
    TextView currentSpeed;
    LocationListener mlocListener;

    //ImsgeViews
    ImageView accelerometerInfo;


    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    //Needed for location permission
    private final int REQUEST_LOCATION_PERMISSION = 1;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeSensors);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_sensor);

        //ImageViews
        accelerometerInfo = (ImageView) findViewById(R.id.info_button);

        //Dialog Box for Temperature Info
        accelerometerInfoDialog = new Dialog(this);

        //TextViews
        accelerometer = (TextView) findViewById(R.id.accelerometer);
        currentSpeed = (TextView) findViewById(R.id.current_speed);
        accelerometer_sensor = (TextView) findViewById(R.id.accelerometer_sensor);


        //Animation fade in for UI elements
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        accelerometer.startAnimation(in);
        currentSpeed.startAnimation(in);
        accelerometer_sensor.startAnimation(in);
        accelerometerInfo.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Calls the location permission dialog box upon opening this activity
        requestLocationPermissions();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();
createNotificationChannel();

        }


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

    //Upon leaving the activity the location data will terminate
    //Used to free memory/battery usage
    public  void onPause()
    {
        super.onPause();
        LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        lm.removeUpdates(this);
    }

    public void showAccelerometerDialogPopup(View v) {
        accelerometerInfoDialog.setContentView(R.layout.accelerometer_popup_info);

        accelerometerInfoDialog.show();
    }

    public void closeAccelerometerDialogPopup(View v) {
        accelerometerInfoDialog.setContentView(R.layout.accelerometer_popup_info);

        accelerometerInfoDialog.dismiss();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //Speed Formula for the Accelerometer
    @Override
    public void onLocationChanged(Location location) {

        // Get the instance of SharedPreferences object
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        //Get the string data from the Preferences
        String unit = settings.getString("speedunit", "");

        if (location == null) {
            // if you can't get speed because reasons :)
            switch (unit) {
                case "MPH":
                case "KM/H":
                case "M/S":
                case "FT/S":
                case "knots":
                    currentSpeed.setText("0 " + unit);
                    break;
            }
        } else {
            int speedMs = (int) ((location.getSpeed())); // This is the standard which returns meters per second.
            int speedMph = (int) (location.getSpeed() * 2.2369); // This is speed in mph
            int speedKm = (int) ((location.getSpeed() * 3600) / 1000); // This is speed in km/h
            int speedFts = (int) (location.getSpeed() * 3.2808); // This is speed in Feet per second
            int speedKnot = (int) (location.getSpeed() * 1.9438); // This is speed in knots

            //Refer to this link for more speed data https://www.wikiwand.com/en/Speed

            switch (unit) {
                case "MPH":
                    currentSpeed.setText(speedMph + " " + unit);
                    break;
                case "KM/H":
                    currentSpeed.setText(speedKm + " " + unit);
                    break;
                case "M/S":
                    currentSpeed.setText(speedMs + " " + unit);
                    break;
                case "FT/S":
                    currentSpeed.setText(speedFts + " " + unit);
                    break;
                case "Knots":
                    currentSpeed.setText(speedKnot + " " + unit);
                    break;
            }

            //Gets the unit of measurement from the speedunit key in root_preferences.xml
            int speedNumber = Integer.parseInt(settings.getString("edit_text_speed", ""));
            //Checks to see if the temperature alert notifications are turned on in root_preferences.xml

            if (settings.getBoolean("switch_preference_speed", true)) {
                //Conditions that must be true for the notifications to work
                //If MPH is chosen as the unit of measurement
                if (speedNumber == speedMph && unit.equals("MPH")) {
                    String textTitle = "Android Sensor Engine";
                    String textContent = "You have reached " + speedNumber + " " + unit;

                    // String pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)


                            .setSmallIcon(R.drawable.launch_logo_256)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            //  .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(Integer.parseInt(CHANNEL_ID), builder.build());
                } else if (speedNumber == speedKm && unit.equals("KM/H")) {
                    String textTitle = "Android Sensor Engine";
                    String textContent = "You have reached " + speedNumber + " " + unit;

                    // String pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)


                            .setSmallIcon(R.drawable.launch_logo_256)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            //  .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(Integer.parseInt(CHANNEL_ID), builder.build());
                } else if (speedNumber == speedMs && unit.equals("M/S")) {
                    String textTitle = "Android Sensor Engine";
                    String textContent = "You have reached " + speedNumber + " " + unit;

                    // String pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)


                            .setSmallIcon(R.drawable.launch_logo_256)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            //  .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(Integer.parseInt(CHANNEL_ID), builder.build());
                } else if (speedNumber == speedFts && unit.equals("FT/S")) {
                    String textTitle = "Android Sensor Engine";
                    String textContent = "You have reached " + speedNumber + " " + unit;

                    // String pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)


                            .setSmallIcon(R.drawable.launch_logo_256)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            //  .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(Integer.parseInt(CHANNEL_ID), builder.build());
                } else if (speedNumber == speedKnot && unit.equals("Knots")) {
                    String textTitle = "Android Sensor Engine";
                    String textContent = "You have reached " + speedNumber + " " + unit;

                    // String pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)


                            .setSmallIcon(R.drawable.launch_logo_256)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            //  .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(Integer.parseInt(CHANNEL_ID), builder.build());
                }
            }
        }
    }
    
    //For handling notifications
    private void createNotificationChannel () {
        // Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_LONG).show();
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