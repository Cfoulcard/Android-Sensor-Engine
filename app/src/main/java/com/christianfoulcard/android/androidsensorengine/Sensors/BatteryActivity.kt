package com.christianfoulcard.android.androidsensorengine.Sensors

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*


class BatteryActivity : AppCompatActivity() {

    //Dialog popup info
    var batteryInfoDialog: Dialog? = null

    //TextViews
    internal lateinit var battery_text: TextView
    internal lateinit var currentBattery: TextView
    internal lateinit var batterySensor: TextView
    private val context: Context? = null
    private val mBatteryLevel: Int = 0
    private var ifilter: IntentFilter? = null

    //ImageViews
    var batteryInfo: ImageView? = null

    // Initiate Firebase Analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    //Channel ID for notifications
    private val ID = "3"

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.battery_sensor)

        //TextViews
        battery_text = findViewById(R.id.battery) as TextView
        currentBattery = findViewById(R.id.current_battery) as TextView
        batterySensor = findViewById(R.id.battery_sensor) as TextView

        //ImageViews
        batteryInfo = findViewById<View>(R.id.info_button) as ImageView

        //Dialog Box for Temperature Info
        batteryInfoDialog = Dialog(this)



        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

       // currentBattery = findViewById(R.id.current_battery)
      //  registerMyReceiver()


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun registerMyReceiver(): BroadcastReceiver {
        ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(mBatteryReceiver, ifilter)

        return mBatteryReceiver
    }

    override fun onStart() {
        val `in` = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        battery_text.startAnimation(`in`)
        currentBattery.startAnimation(`in`)
        batterySensor.startAnimation(`in`)
        batteryInfo!!.startAnimation(`in`)

        //Registers the sensor
        registerMyReceiver()
        if (currentBattery.text  == registerMyReceiver().toString()) {
            currentBattery.text = registerMyReceiver().toString()
        }

        createNotificationChannel()
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        //Unregister the Sensor
        unregisterReceiver(registerMyReceiver())
        super.onDestroy()
    }

    fun showBatteryDialogPopup(v: View?) {
        batteryInfoDialog?.setContentView(R.layout.battery_popup_info)
        batteryInfoDialog?.show()
    }

    fun closeBatteryDialogPopup(v: View?) {
        batteryInfoDialog?.setContentView(R.layout.battery_popup_info)
        batteryInfoDialog?.dismiss()
    }

    //For handling the notifications
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name_battery)
            val description = getString(R.string.channel_description_battery)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(ID, name, importance)
            channel.description = description

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)

            // notificationId is a unique int for each notification that you must define
        }
    }

    //Initiate Broadcast Receiver to utilize BatteryManager Features
    //Is parsed by registerMyReceiver() to the textview
    private var mBatteryReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            val percent = level * 100 / scale
            val batteryPct = level / scale.toFloat()
            val celsiusLevel = level / 10
            val fahrenheitLevel = celsiusLevel * 9 / 5 + 32
            val kelvinLevel = celsiusLevel + 273

            // Get the instance of SharedPreferences object
            //Note the original context was "this". This caused a Null Pointer error
            //Used this@BatteryActivity to fix the issue
            val settings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@BatteryActivity)

            //Finds the preference string value and links it with the appropriate temperature calc formula
            when (val unit = settings.getString("batterytempunit", "")) {
                "C°" -> currentBattery.setText(celsiusLevel.toString() + " " + unit)
                "F°" -> currentBattery.setText(fahrenheitLevel.toString() + " " + unit)
                "K°" -> currentBattery.setText(kelvinLevel.toString() + " " + unit)
            }

            ////////////////////////////////////////////////////////////////////////////////////////
            //Notification alert section

            //Gets the unit of measurement from the batterytempunit key in root_preferences.xml
            val unit = settings.getString("batterytempunit", "")

            //Gets the string value from the edit_text_battery_temp key in root_preferences.xml
            val battNumber = settings.getString("edit_text_battery_temp", "")

            // Create an Intent for the activity you want to start
            val resultIntent = Intent(this, BatteryActivity::class.java)

            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addNextIntentWithParentStack(resultIntent)

            // Get the PendingIntent containing the entire back stack
            val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

            //Checks to see if the temperature alert notifications are turned on in root_preferences.xml
            if (settings.getBoolean("switch_preference_battery", true)) {
                //Conditions that must be true for the notifications to work
                //If Celsius is chosen as the unit of measurement
                if (battNumber != null) {
                    if (battNumber == celsiusLevel.toString() && unit == "C°") {
                        val textTitle = "Android Sensor Engine"
                        val textContent = getString(R.string.notify_battery_message) + " " + battNumber + " " + unit

                        val builder = NotificationCompat.Builder(context, ID)

                                .setSmallIcon(R.drawable.launch_logo_256)
                                .setContentTitle(textTitle)
                                .setContentText(textContent)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setOnlyAlertOnce(true)

                        val notificationManager = NotificationManagerCompat.from(context)
                        notificationManager.notify(123, builder.build())

                        //If Fahrenheit is chosen as the unit of measurement
                    } else if (battNumber == fahrenheitLevel.toString()  && unit == "F°") {
                        val textTitle = "Android Sensor Engine"
                        val textContent = getString(R.string.notify_battery_message) + " " + fahrenheitLevel + " " + unit

                        val builder = NotificationCompat.Builder(context, ID)
                                .setSmallIcon(R.drawable.launch_logo_256)
                                .setContentTitle(textTitle)
                                .setContentText(textContent)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setOnlyAlertOnce(true)

                        val notificationManager = NotificationManagerCompat.from(context)
                        notificationManager.notify(123, builder.build())

                        //If Kelvin is chosen as the unit of measurement
                    } else if (battNumber == kelvinLevel.toString() && unit == "K°") {
                        val textTitle = "Android Sensor Engine"
                        val textContent = getString(R.string.notify_battery_message) + " " + battNumber + " " + unit

                        val builder = NotificationCompat.Builder(context, ID)
                                .setSmallIcon(R.drawable.launch_logo_256)
                                .setContentTitle(textTitle)
                                .setContentText(textContent)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setOnlyAlertOnce(true)

                        val notificationManager = NotificationManagerCompat.from(context)
                        notificationManager.notify(123, builder.build())
                    }
                }
            }
        }
    }

    //This is needed to help open the activity from the notifications
    private fun Intent(broadcastReceiver: BroadcastReceiver, java: Class<BatteryActivity>): Intent? {
    return Intent(this, BatteryActivity::class.java)
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
}