package com.christianfoulcard.android.androidsensorengine.sensors

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.PendingIntent
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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.christianfoulcard.android.androidsensorengine.OneTimeAlertDialog
import com.christianfoulcard.android.androidsensorengine.preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.databinding.ActivityLightBinding

/** Gathers Light data from the device's sensor using SensorManager */
class SensorLightActivity : AppCompatActivity(), SensorEventListener {

    // View Binding to call the layout's views
    private lateinit var binding: ActivityLightBinding

    // Dialog popup info
    private var lightInfoDialog: Dialog? = null

    // Sensor initiation
    private var sensorManager: SensorManager? = null
    private var light: Sensor? = null

    //Handler for dialog pin shortcut dialog box
    val handler = Handler()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        binding = ActivityLightBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Ads
//        MobileAds.initialize(this) {} //ADMOB App ID
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)

        //Dialog Box for Temperature Info
        lightInfoDialog = Dialog(this)

        //Opens Pin Shortcut menu after long pressing the logo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            binding.luxSensorLogo.setOnLongClickListener() {
                sensorShortcut()
            }
        }

        // Get an instance of the sensor service, and use that to get an instance of
        // the light sensor. If the device does not support this sensor a toast message
        // will appear
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show()
        }

        //Light sensor to measure light
        light = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
       // currentLux = findViewById(R.id.current_lux)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    //Main magic of the Light Sensor
    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            binding.currentLux.text = event.values[0].toString() + " lux"
        }
    }

    override fun onStart() {
        super.onStart()
        //Animation that plays fading animation when entering/exiting Activity
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        binding.luminosity.startAnimation(`in`)
        binding.currentLux.startAnimation(`in`)
        binding.luxSensor.startAnimation(`in`)
        binding.infoButton.startAnimation(`in`)
    }

    override fun onResume() {
        super.onResume()
        // Register a listener for the sensor.
        sensorManager!!.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)

        // Creates a dialog explaining how to pin the sensor to the home screen
        // Appears after 1 second of opening activity
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            handler.postDelayed({ alertDialog() }, 1000) // 1 second
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregisters the sensor when the activity pauses.
        sensorManager!!.unregisterListener(this)
    }

    fun showLightDialogPopup(v: View?) {
        lightInfoDialog!!.setContentView(R.layout.dialog_light)
        lightInfoDialog!!.show()
    }

    fun closeLightDialogPopup(v: View?) {
        lightInfoDialog!!.setContentView(R.layout.dialog_light)
        lightInfoDialog!!.dismiss()
    }

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
        val intent = Intent(this, SensorLightActivity::class.java)
                .setAction("Light")

        if (shortcutManager!!.isRequestPinShortcutSupported) {

            val pinShortcutInfo = ShortcutInfo.Builder(this, "light-shortcut")
                    .setShortLabel(getString(R.string.lux_sensor))
                    .setLongLabel(getString(R.string.lux_sensor))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_light_icon))
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
}