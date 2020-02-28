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

public class HumidityActivity extends AppCompatActivity implements SensorEventListener {

    //ID used for notifications
    private static final String CHANNEL_ID = "5";

    //Dialog popup info
    Dialog humidityInfoDialog;

    //TextViews
    TextView humidity_text;
    TextView currentHumidity;
    TextView humidityAmount;

    //ImsgeViews
    ImageView humidityInfo;

    //Sensor initiation
    private SensorManager sensorManager;
    private Sensor humidity;
    private Context mContext;
    private Activity mActivity;

    private SharedPreferences mSharedPreferences;

    // Initiate Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeSensors);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.humidity_sensor);

        //TextViews
        humidity_text = (TextView) findViewById(R.id.humidity);
        currentHumidity = (TextView) findViewById(R.id.current_humidity);
        humidityAmount = (TextView) findViewById(R.id.humidity_sensor);

        //ImageViews
        humidityInfo = (ImageView) findViewById(R.id.info_button);

        //Dialog Box for Temperature Info
        humidityInfoDialog = new Dialog(this);

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1500);
        humidity_text.startAnimation(in);
        currentHumidity.startAnimation(in);
        humidityAmount.startAnimation(in);
        humidityInfo.startAnimation(in);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get an instance of the sensor service, and use that to get an instance of
        // the relative temperature. If device does not support this sensor a toast message will
        // appear
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show();
        }

        // Ambient Temperature measures the temperature around the device
        humidity = sensorManager.getDefaultSensor((Sensor.TYPE_RELATIVE_HUMIDITY));
        currentHumidity = (TextView) findViewById(R.id.current_humidity);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = HumidityActivity.this;

        // Get the instance of SharedPreferences object
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        int water_vapor = (int) event.values[0];

        //The default Android properties for event.values[0] is the formula for Celsius
        //This is for Fahrenheit
        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            currentHumidity.setText(water_vapor + "%");

        }

        //Gets the string value from the edit_text_humidity key in root_preferences.xml
        int vaporNumber = Integer.parseInt(settings.getString("edit_text_humidity", "0"));

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, HumidityActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Checks to see if the humidity alert notifications are turned on in root_preferences.xml
        if (settings.getBoolean("switch_preference_humidity", true)) {
            //Conditions that must be true for the notifications to work
            if (vaporNumber == water_vapor) {
                String textTitle = "Android Sensor Engine";
                String textContent = "The relative humidity has reached " + vaporNumber + "%";

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
            CharSequence name = getString(R.string.channel_name_humidity);
            String description = getString(R.string.channel_description_humidity);
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
        createNotificationChannel ();
        super.onStart();
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
      //  sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
     //   sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    public void showHumidityDialogPopup(View v) {
        humidityInfoDialog.setContentView(R.layout.humidity_popup_info);

        humidityInfoDialog.show();
    }

    public void closeHumidityDialogPopup(View v) {
        humidityInfoDialog.setContentView(R.layout.humidity_popup_info);

        humidityInfoDialog.dismiss();
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