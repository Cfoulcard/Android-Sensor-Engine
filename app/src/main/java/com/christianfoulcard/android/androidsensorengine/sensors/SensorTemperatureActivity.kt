package com.christianfoulcard.android.androidsensorengine.sensors

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.christianfoulcard.android.androidsensorengine.OneTimeAlertDialog
import com.christianfoulcard.android.androidsensorengine.preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.databinding.ActivityTemperatureBinding

class SensorTemperatureActivity : AppCompatActivity(), SensorEventListener {

    //View Binding to call the layout's views
    private lateinit var binding: ActivityTemperatureBinding

    //Dialog popup info
    private var tempInfoDialog: Dialog? = null

    //Sensor initiation
    private var sensorManager: SensorManager? = null
    private var temperature: Sensor? = null
    private var mContext: Context? = null
    private var mActivity: Activity? = null

    //Handler for dialog pin shortcut dialog box
    val handler = Handler()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        binding = ActivityTemperatureBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Ads
//        MobileAds.initialize(this) {} //ADMOB App ID
//        mAdView = findViewById(R.id.adView)
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)

        //Dialog Box for Temperature Info
        tempInfoDialog = Dialog(this)

        //Opens Pin Shortcut menu after long pressing the logo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            binding.temperatureLogo.setOnLongClickListener() {
                sensorShortcut()
            }
        }


        // Get an instance of the sensor service, and use that to get an instance of
        // the surrounding temperature. If device does not support this sensor a toast message will
        // appear
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show()
        }

        // Get the instance of SharedPreferences object
        val settings = PreferenceManager.getDefaultSharedPreferences(this)

        // Ambient Temperature measures the temperature around the device
        temperature = sensorManager!!.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onStart() {
        super.onStart()

        //Controls Fade in Animation upon opening app
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        binding.temperature.startAnimation(`in`)
        binding.currentTemp.startAnimation(`in`)
        binding.temperatureSensor.startAnimation(`in`)
        binding.infoButton.startAnimation(`in`)

        //Enables notifications if they are switched on
        createNotificationChannel()

        // Register a listener for the sensor.
        sensorManager!!.registerListener(this, temperature, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onResume() {
        super.onResume()

        // Creates a dialog explaining how to pin the sensor to the home screen
        // Appears after 1 second of opening activity
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            handler.postDelayed({ alertDialog() }, 1000) // 1 second
        }
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        // Unregisters the sensor when the activity is destroyed
        super.onDestroy()
        sensorManager!!.unregisterListener(this)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    //Main magic of the Temperature Sensor
    override fun onSensorChanged(event: SensorEvent) {

        // Get the application context
        mContext = applicationContext

        // Get the activity
        mActivity = this@SensorTemperatureActivity

        // Get the instance of SharedPreferences object
        val settings = PreferenceManager.getDefaultSharedPreferences(this)

        //Get the string data from the Preferences
        val unit = settings.getString("airtempunit", "")

        //Calculates Celsius
        val c = event.values[0].toInt()
        //Calculates Fahrenheit
        val f = c * 9 / 5 + 32
        //Calculates Kelvin
        val k = (c + 273.15).toInt()

        //String Data for when sensor does not update
        val celsiusString = "C°"

        //Finds the preference string value and links it with the appropriate temperature calc formula
        if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            when (unit) {
                "" -> binding.currentTemp.text = "$c $celsiusString"
                "C°" -> binding.currentTemp.text = "$c $unit"
                "F°" -> binding.currentTemp.text = "$f $unit"
                "K°" -> binding.currentTemp.text = "$k $unit"
            }

            //Gets the string value from the edit_text_air_temp key in root_preferences.xml
            val airNumber = settings.getString("edit_text_air_temp", "")

            // Create an Intent for the activity you want to start
            val resultIntent = Intent(this, SensorTemperatureActivity::class.java)
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            val stackBuilder = TaskStackBuilder.create(this)
            stackBuilder.addNextIntentWithParentStack(resultIntent)

            // Get the PendingIntent containing the entire back stack
            val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

            //Checks to see if the temperature alert notifications are turned on in root_preferences.xml
            if (settings.getBoolean("switch_preference_air", true)) {
                //Conditions that must be true for the notifications to work
                //If Celsius is chosen as the unit of measurement
                if (airNumber == c.toString() && unit == "C°") {
                    val textTitle = "Android Sensor Engine"
                    val textContent = getString(R.string.notify_air_message) + " " + airNumber + " " + unit

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification_logo)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true)
                    val notificationManager = NotificationManagerCompat.from(this)

                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(CHANNEL_ID.toInt(), builder.build())

                    //If Fahrenheit is chosen as the unit of measurement
                } else if (airNumber == f.toString() && unit == "F°") {
                    val textTitle = "Android Sensor Engine"
                    val textContent = getString(R.string.notify_air_message) + " " + airNumber + " " + unit

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification_logo)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true)
                    val notificationManager = NotificationManagerCompat.from(this)

                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(CHANNEL_ID.toInt(), builder.build())

                    //If Kelvin is chosen as the unit of measurement
                } else if (airNumber == k.toString() && unit == "K°") {
                    val textTitle = "Android Sensor Engine"
                    val textContent = getString(R.string.notify_air_message) + " " + airNumber + " " + unit

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification_logo)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true)
                    val notificationManager = NotificationManagerCompat.from(this)


                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(CHANNEL_ID.toInt(), builder.build())
                }
            }
        }
    }

    //For handling notifications
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name_air_temp)
            val description = getString(R.string.channel_description_air_temp)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    fun showTempDialogPopup(v: View?) {
        tempInfoDialog!!.setContentView(R.layout.dialog_temperature)
        tempInfoDialog!!.show()
    }

    fun closeTempDialogPopup(v: View?) {
        tempInfoDialog!!.setContentView(R.layout.dialog_temperature)
        tempInfoDialog!!.dismiss()
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //This will add functionality to the menu button within the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    //The following is for the menu items within the navigation_menu.xml file
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.preferences -> {
                val configurationsIntent = Intent(this, SettingsActivity::class.java)
                this.startActivity(configurationsIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Adds Pin Shortcut Functionality
    @RequiresApi(Build.VERSION_CODES.O)
    fun sensorShortcut(): Boolean {

        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)
        val intent = Intent(this, SensorTemperatureActivity::class.java)
                .setAction("Temp")

        if (shortcutManager!!.isRequestPinShortcutSupported) {

            val pinShortcutInfo = ShortcutInfo.Builder(this, "temp-shortcut")
                    .setShortLabel(getString(R.string.phone_temp_sensor))
                    .setLongLabel(getString(R.string.phone_temp_sensor))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_temp_icon))
                    .setIntent(intent)
                    .build()

            // Create the PendingIntent object only if your app needs to be notified
            // that the user allowed the shortcut to be pinned. Note that, if the
            // pinning operation fails, your app isn't notified. We assume here that the
            // app has implemented a method called createShortcutResultIntent() that
            // returns a broadcast intent.
            val pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo)

            // Configure the intent so that your app's broadcast receiver gets
            // the callback successfully.For details, see PendingIntent.getBroadcast().
            val successCallback = PendingIntent.getBroadcast(this, /* request code */ 0,
                    pinnedShortcutCallbackIntent, /* flags */ 0)

            shortcutManager.requestPinShortcut(pinShortcutInfo,
                    successCallback.intentSender)
        }
        return true
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Pin Shortcut Dialog Data
    private fun alertDialog() {

        val dialog = OneTimeAlertDialog.Builder(this, "my_dialog_key")

                dialog.setTitle(getString(R.string.pin_shortcut_title))
                dialog.setMessage(getString(R.string.pin_shortut_message))
                dialog.show()
    }

    companion object {
        //ID used for notifications
        private const val CHANNEL_ID = "1"
    }
}