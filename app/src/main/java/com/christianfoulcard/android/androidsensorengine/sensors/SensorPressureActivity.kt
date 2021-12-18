package com.christianfoulcard.android.androidsensorengine.sensors

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.christianfoulcard.android.androidsensorengine.BuildConfig
import com.christianfoulcard.android.androidsensorengine.OneTimeAlertDialog
import com.christianfoulcard.android.androidsensorengine.preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.databinding.ActivityPressureBinding

/** Gathers pressure information from the environment */
class SensorPressureActivity : AppCompatActivity(), SensorEventListener {

    // View Binding to call the layout's views
    private lateinit var binding: ActivityPressureBinding

    // Dialog popup info
    private var pressureInfoDialog: Dialog? = null

    // Sensor initiation
    private var sensorManager: SensorManager? = null
    private var pressure: Sensor? = null
    private var mContext: Context? = null
    private var mActivity: Activity? = null

    // Get the preference settings
    private val mSharedPreferences: SharedPreferences? = null

    // Handler for dialog pin shortcut dialog box
    val handler = Handler()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        binding = ActivityPressureBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Ads
//        MobileAds.initialize(this) {} //ADMOB App ID
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)

        // Opens Pin Shortcut menu after long pressing the logo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.pressureLogo.setOnLongClickListener() {
                sensorShortcut()
            }
        }

        // Get an instance of the sensor service, and use that to get an instance of
        // the relative pressure. If device does not support this sensor a toast message will
        // appear
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (BuildConfig.DEBUG && sensorManager == null) {
            error("Assertion failed")
        }
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_PRESSURE) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show()
        }

        // Ambient Temperature measures the temperature around the device
        pressure = sensorManager!!.getDefaultSensor(Sensor.TYPE_PRESSURE)
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onSensorChanged(event: SensorEvent) {

        // Get the application context
        mContext = applicationContext

        // Get the activity
        mActivity = this@SensorPressureActivity

        // Get the instance of SharedPreferences object
        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val pressure_level = event.values[0].toInt()

        if (event.sensor.type == Sensor.TYPE_PRESSURE) {
            binding.currentPressure.text = "$pressure_level hPa"
        }

        // Create an Intent for the activity you want to start
        val resultIntent = Intent(this, SensorPressureActivity::class.java)

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(resultIntent)
        
        // Get the PendingIntent containing the entire back stack
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        // Checks to see if the pressure alert notifications are turned on in root_preferences.xml
        if (settings.getBoolean("switch_preference_pressure", true)) {
            // Gets the string value from the edit_text_pressure key in root_preferences.xml
            val pressureNumber = settings.getString("edit_text_pressure", "")

            // Conditions that must be true for the notifications to work
            if (pressure_level.toString() == pressureNumber) {
                val textTitle = "Android Sensor Engine"
                val textContent = getString(R.string.notify_pressure_message) + " " + pressureNumber + " " + "hPa"

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

    // For handling notifications
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name_pressure)
            val description = getString(R.string.channel_description_pressure)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onStart() {
        super.onStart()
        //Dialog Box for Temperature Info
        pressureInfoDialog = Dialog(this)

        // Fade out animation effects
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        binding.pressure.startAnimation(`in`)
        binding.currentPressure.startAnimation(`in`)
        binding.pressureSensor.startAnimation(`in`)
        binding.infoButton.startAnimation(`in`)

        createNotificationChannel()

        // Register a listener for the sensor.
        sensorManager!!.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onResume() {
        super.onResume()
        // mAdView.resume()

        // Creates a dialog explaining how to pin the sensor to the home screen
        // Appears after 1 second of opening activity
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            handler.postDelayed({ alertDialog() }, 1000) // 1 second
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregisters the sensor when the activity pauses.
        //  sensorManager!!.unregisterListener(this)
    }

    fun showPressureDialogPopup(v: View?) {
        pressureInfoDialog!!.setContentView(R.layout.dialog_pressure)
        pressureInfoDialog!!.show()
    }

    fun closePressureDialogPopup(v: View?) {
        pressureInfoDialog!!.setContentView(R.layout.dialog_pressure)
        pressureInfoDialog!!.dismiss()
    }

    // This will add functionality to the menu button within the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    // The following is for the menu items within the navigation_menu.xml file
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
        val intent = Intent(this, SensorPressureActivity::class.java)
                .setAction("Pressure")

        if (shortcutManager!!.isRequestPinShortcutSupported) {

            val pinShortcutInfo = ShortcutInfo.Builder(this, "pressure-shortcut")
                    .setShortLabel(getString(R.string.pressure_sensor))
                    .setLongLabel(getString(R.string.pressure_sensor))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_barometer_icon))
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

        OneTimeAlertDialog.Builder(this, "my_dialog_key")
                .setTitle(getString(R.string.pin_shortcut_title))
                .setMessage(getString(R.string.pin_shortut_message))
                .show()
    }

    companion object {
        //ID used for notifications
        private const val CHANNEL_ID = "4"
    }
}