package com.christianfoulcard.android.androidsensorengine.Sensors

import android.app.Activity
import android.app.Dialog
import android.app.PendingIntent
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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics

class WalkActivity : AppCompatActivity(), SensorEventListener {

    //Dialog popup info
    private var walkInfoDialog: Dialog? = null

    //TextViews
    private var steps_text: TextView? = null
    private var currentSteps: TextView? = null
    private var stepAmount: TextView? = null

    //Sensor initiation
    private var sensorManager: SensorManager? = null
    private var steps: Sensor? = null
    private var mContext: Context? = null
    private var mActivity: Activity? = null

    //ImageViews
    private var walkInfo: ImageView? = null
    private var walkLogo: ImageView? = null

    //Gets settings from preference
    private val mSharedPreferences: SharedPreferences? = null

    // Initiate Firebase Analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    //For Ads
    private lateinit var mAdView : AdView

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.walk_sensor)

        // Initialize Ads
        MobileAds.initialize(this) {} //ADMOB App ID
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        //TextViews
        steps_text = findViewById<View>(R.id.walk_sensor) as TextView
        currentSteps = findViewById<View>(R.id.current_steps) as TextView
        stepAmount = findViewById<View>(R.id.steps) as TextView

        //ImageViews
        walkInfo = findViewById<View>(R.id.info_button) as ImageView
        walkLogo = findViewById<View>(R.id.walk_logo) as ImageView

        //Dialog Box for Temperature Info
        walkInfoDialog = Dialog(this)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        //Opens Pin Shortcut menu after long pressing the logo
        walkLogo!!.setOnLongClickListener() {
            sensorShortcut()
        }

        // Get an instance of the sensor service, and use that to get an instance of
        // the relative temperature. If device does not support this sensor a toast message will
        // appear
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show()
        }

        // Ambient Temperature measures the temperature around the device
        steps = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        currentSteps = findViewById<View>(R.id.current_steps) as TextView
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onSensorChanged(event: SensorEvent) {
        // Get the application context
        mContext = applicationContext

        // Get the activity
        mActivity = this@WalkActivity

        //Turns sensor data into walking data
        val stepCounter = event.values[0].toInt()

        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            currentSteps!!.text = stepCounter.toString() + "" //Crashes without ""
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onStart() {
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        steps_text!!.startAnimation(`in`)
        currentSteps!!.startAnimation(`in`)
        stepAmount!!.startAnimation(`in`)
        walkInfo!!.startAnimation(`in`)

        // Register a listener for the sensor.
        sensorManager!!.registerListener(this, steps, SensorManager.SENSOR_DELAY_FASTEST)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        // No need to unregister. Step counter will stop if uncommented
        // sensorManager.unregisterListener(this);
    }

    fun showWalkDialogPopup(v: View?) {
        walkInfoDialog!!.setContentView(R.layout.walk_popup_info)
        walkInfoDialog!!.show()
    }

    fun closeWalkDialogPopup(v: View?) {
        walkInfoDialog!!.setContentView(R.layout.walk_popup_info)
        walkInfoDialog!!.dismiss()
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
    private fun sensorShortcut(): Boolean {
        val shortcutManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            getSystemService<ShortcutManager>(ShortcutManager::class.java)
        } else {
            TODO("VERSION.SDK_INT < N_MR1")
        }

        val walkIntent = Intent(this, WalkActivity::class.java)
                .setAction("Walk")

        if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    shortcutManager!!.isRequestPinShortcutSupported
                } else {
                    TODO("VERSION.SDK_INT < O")
                }) {

            val pinShortcutInfo = ShortcutInfo.Builder(this, "walk-shortcut")
                    .setShortLabel(getString(R.string.walk_sensor))
                    .setLongLabel(getString(R.string.walk_sensor))
                    .setIcon(Icon.createWithResource(this, R.drawable.walk_icon))
                    .setIntent(walkIntent)
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
}