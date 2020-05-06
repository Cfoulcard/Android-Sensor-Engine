package com.christianfoulcard.android.androidsensorengine.Sensors

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.Sensors.HumidityActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics

class HumidityActivity : AppCompatActivity(), SensorEventListener {

    //Dialog popup info
    var humidityInfoDialog: Dialog? = null

    //TextViews
    var humidity_text: TextView? = null
    var currentHumidity: TextView? = null
    var humidityAmount: TextView? = null

    //ImageViews
    var humidityInfo: ImageView? = null

    //Sensor initiation
    private var sensorManager: SensorManager? = null
    private var humidity: Sensor? = null
    private var mContext: Context? = null
    private var mActivity: Activity? = null

    //Gets the setting preferences
    private val mSharedPreferences: SharedPreferences? = null

    // Initiate Firebase Analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    //For Ads
    private lateinit var mAdView : AdView

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.humidity_sensor)

        // Initialize Ads
        MobileAds.initialize(this) {}  //ADMOB App ID
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        //TextViews
        humidity_text = findViewById<View>(R.id.humidity) as TextView
        currentHumidity = findViewById<View>(R.id.current_humidity) as TextView
        humidityAmount = findViewById<View>(R.id.humidity_sensor) as TextView

        //ImageViews
        humidityInfo = findViewById<View>(R.id.info_button) as ImageView

        //Dialog Box for Temperature Info
        humidityInfoDialog = Dialog(this)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Get an instance of the sensor service, and use that to get an instance of
        // the relative temperature. If device does not support this sensor a toast message will
        // appear
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) == null) {
            Toast.makeText(this, R.string.unsupported_sensor, Toast.LENGTH_LONG).show()
        }

        // Ambient Temperature measures the temperature around the device
        humidity = sensorManager!!.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        currentHumidity = findViewById<View>(R.id.current_humidity) as TextView
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onSensorChanged(event: SensorEvent) {
        // Get the application context
        mContext = applicationContext

        // Get the activity
        mActivity = this@HumidityActivity

        // Get the instance of SharedPreferences object
        val settings = PreferenceManager.getDefaultSharedPreferences(this)

        //Get Humidity from the sensor
        val waterVapor = event.values[0].toInt()

        //Gets sensor data for humidity
        if (event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            currentHumidity!!.text = "$waterVapor%"
        }

        //Gets the string value from the edit_text_humidity key in root_preferences.xml
        val vaporNumber = settings.getString("edit_text_humidity", "")

        // Create an Intent for the activity you want to start
        val resultIntent = Intent(this, HumidityActivity::class.java)

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(resultIntent)

        // Get the PendingIntent containing the entire back stack
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        //Checks to see if the humidity alert notifications are turned on in root_preferences.xml
        if (settings.getBoolean("switch_preference_humidity", true)) {
            //Conditions that must be true for the notifications to work
            if (vaporNumber == waterVapor.toString()) {
                val textTitle = "Android Sensor Engine"
                val textContent = getString(R.string.notify_humidity_message) + " " + vaporNumber + "%"
                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.launch_logo_256)
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

    //For handling notifications
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name_humidity)
            val description = getString(R.string.channel_description_humidity)
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
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        humidity_text!!.startAnimation(`in`)
        currentHumidity!!.startAnimation(`in`)
        humidityAmount!!.startAnimation(`in`)
        humidityInfo!!.startAnimation(`in`)

        createNotificationChannel()
        super.onStart()
        sensorManager!!.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        // Unregisters the sensor when the activity pauses.
        super.onDestroy()
        sensorManager!!.unregisterListener(this)
    }

    fun showHumidityDialogPopup(v: View?) {
        humidityInfoDialog!!.setContentView(R.layout.humidity_popup_info)
        humidityInfoDialog!!.show()
    }

    fun closeHumidityDialogPopup(v: View?) {
        humidityInfoDialog!!.setContentView(R.layout.humidity_popup_info)
        humidityInfoDialog!!.dismiss()
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

    companion object {
        //ID used for notifications
        private const val CHANNEL_ID = "5"
    }
}