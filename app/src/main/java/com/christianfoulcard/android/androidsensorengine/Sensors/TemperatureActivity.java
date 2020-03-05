package com.christianfoulcard.android.androidsensorengine.Sensors;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
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
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceFragmentCompat;

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

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TemperatureActivity extends AppCompatActivity implements SensorEventListener {

    //ID used for notifications
    private static final String CHANNEL_ID = "1";

    //Dialog popup info
    Dialog tempInfoDialog;

    //TextViews
    TextView temperature_text;
    TextView currentDegrees;
    TextView airTemp;

    //ImsgeViews
    ImageView tempInfo;

    //Sensor initiation
    private SensorManager sensorManager;
    private Sensor temperature;
    private Context mContext;
    private Activity mActivity;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    MyTimerTask tasker;



    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeSensors);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_sensor);

        //TextViews
        temperature_text = (TextView) findViewById(R.id.temperature);
        currentDegrees = (TextView) findViewById(R.id.current_temp);
        airTemp = (TextView) findViewById(R.id.temperature_sensor);

        //ImageViews
        tempInfo = (ImageView) findViewById(R.id.info_button);

        //Dialog Box for Temperature Info
        tempInfoDialog = new Dialog(this);

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
    protected void onStart() {
        //Controls Fade in Animation upon opening app
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        temperature_text.startAnimation(in);
        currentDegrees.startAnimation(in);
        airTemp.startAnimation(in);
        tempInfo.startAnimation(in);

        // Register a listener for the sensor.
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);

        //Enables notifications if they are enabled

        createNotificationChannel();
        super.onStart();
    }




    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onDestroy();
        sensorManager.unregisterListener(this);
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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        //Get the string data from the Preferences
        String unit = settings.getString("airtempunit", "");

        //Calculates Celsius
        int c = (int) event.values[0];
        //Calculates Fahrenheit
        int f = c * 9 / 5 + 32;
        //Calculates Kelvin
        int k = (int) (c + 273.15);

        //Finds the preference string value and links it with the appropriate temperature calc formula
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            switch (unit) {
                case "C°":
                    currentDegrees.setText(c + " " + unit);
                    break;
                case "F°":
                    currentDegrees.setText(f + " " + unit);
                    break;
                case "K°":
                    currentDegrees.setText(k + " " + unit);
                    break;
            }

            //Gets the string value from the edit_text_air_temp key in root_preferences.xml
            int airNumber = Integer.parseInt(settings.getString("edit_text_air_temp", String.valueOf(10000)));

            // Create an Intent for the activity you want to start
            Intent resultIntent = new Intent(this, TemperatureActivity.class);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            // Get the PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);





            //Checks to see if the temperature alert notifications are turned on in root_preferences.xml
            if (settings.getBoolean("switch_preference_air", true)) {

                //Conditions that must be true for the notifications to work
                //If Celsius is chosen as the unit of measurement
                if (airNumber == c && unit.equals("C°")) {
                    String textTitle = "Android Sensor Engine";
                    String textContent = "The air around you has reached " + airNumber + " " + unit;

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

                    //If Fahrenheit is chosen as the unit of measurement
                } else if (airNumber == f && unit.equals("F°")) {
                    String textTitle = "Android Sensor Engine";
                    String textContent = "The air around you has reached " + airNumber + " " + unit;

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

                    //If Kelvin is chosen as the unit of measurement
                } else if (airNumber == k && unit.equals("K°")) {
                    String textTitle = "Android Sensor Engine";
                    String textContent = "The air around you has reached " + airNumber + " " + unit;

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
                } else {
                    currentDegrees.setText(f + " " + unit);
                }
            }
        }
    }

    //For handling notifications
    private void createNotificationChannel () { // Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name_air_temp);
            String description = getString(R.string.channel_description_air_temp);
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

////////////////////////////////////////////////////////////////////////////////////////////////////

    public void showTempDialogPopup(View v) {
        tempInfoDialog.setContentView(R.layout.temperature_popup_info);

        tempInfoDialog.show();
    }

    public void closeTempDialogPopup(View v) {
        tempInfoDialog.setContentView(R.layout.temperature_popup_info);

        tempInfoDialog.dismiss();
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
}

