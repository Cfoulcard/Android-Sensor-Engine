package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

public class PressureActivity extends AppCompatActivity implements SensorEventListener {

    //ID used for notifications
    private static final String CHANNEL_ID = "4";

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
        assert sensorManager != null;
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

        // Get the instance of SharedPreferences object
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        int pressure_level = (int) event.values[0];

        //The default Android properties for event.values[0] is the formula for Celsius
        //This is for Fahrenheit
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            currentPressure.setText(pressure_level + " hPa");

        }

        //Gets the string value from the edit_text_pressure key in root_preferences.xml
        int pressureNumber = Integer.parseInt(settings.getString("edit_text_pressure", ""));

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, PressureActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Checks to see if the pressure alert notifications are turned on in root_preferences.xml
        if (settings.getBoolean("switch_preference_pressure", true)) {
            //Conditions that must be true for the notifications to work
            if (pressureNumber == pressure_level) {
                String textTitle = "Android Sensor Engine";
                String textContent = "The atmospheric pressure has reached " + pressureNumber + " " + "hPa";

                // String pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)


                        .setSmallIcon(R.drawable.launch_logo_256)
                        .setContentTitle(textTitle)
                        .setContentText(textContent)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setOnlyAlertOnce(true);


                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(Integer.parseInt(CHANNEL_ID), builder.build());
            }
        }
    }

    //For handling notifications
    private void createNotificationChannel () { // Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name_pressure);
            String description = getString(R.string.channel_description_pressure);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart() {
        // Register a listener for the sensor.

        createNotificationChannel ();
        super.onStart();
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.

     //   createNotificationChannel ()
        super.onResume();
     //   sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
       // sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onDestroy();
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

