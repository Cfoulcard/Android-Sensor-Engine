package com.christianfoulcard.android.androidsensorengine.sensors

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.christianfoulcard.android.androidsensorengine.OneTimeAlertDialog
import com.christianfoulcard.android.androidsensorengine.preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.databinding.ActivityBatteryBinding

/** Gets a heat signature from the device's battery */
class SensorBatteryActivity : AppCompatActivity() {

    // View Binding to call the layout's views
    private lateinit var binding: ActivityBatteryBinding

    // Dialog popup info
    var batteryInfoDialog: Dialog? = null

    // Sensor Data
    private val context: Context? = null
    private val mBatteryLevel: Int = 0
    private var ifilter: IntentFilter? = null

    // Channel ID for notifications
    private val ID = "3"

    // Handler for dialog pin shortcut dialog box
    val handler = Handler()

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Ads
//        MobileAds.initialize(this) {} //ADMOB App ID
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)

        // Dialog Box for Temperature Info
        batteryInfoDialog = Dialog(this)

        // Opens Pin Shortcut menu after long pressing the logo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            binding.batteryLogo.setOnLongClickListener() {
                sensorShortcut()
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Get the battery data
    fun registerMyReceiver(): BroadcastReceiver {
        ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(mBatteryReceiver, ifilter)

        return mBatteryReceiver
    }

    override fun onStart() {
        super.onStart()

        // This will add a transition effect when the activity is open
        val `in` = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        binding.battery.startAnimation(`in`)
        binding.currentBattery.startAnimation(`in`)
        binding.batterySensor.startAnimation(`in`)
        binding.infoButton.startAnimation(`in`)

        // Registers the sensor
        registerMyReceiver()
        if (binding.currentBattery.text  == registerMyReceiver().toString()) {
            binding.currentBattery.text = registerMyReceiver().toString()
        }

        createNotificationChannel()
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

    override fun onDestroy() {
        // Unregister the Sensor
        // unregisterReceiver(registerMyReceiver())
        super.onDestroy()
    }

    fun showBatteryDialogPopup(v: View?) {
        batteryInfoDialog?.setContentView(R.layout.dialog_battery)
        batteryInfoDialog?.show()
    }

    fun closeBatteryDialogPopup(v: View?) {
        batteryInfoDialog?.setContentView(R.layout.dialog_battery)
        batteryInfoDialog?.dismiss()
    }

    // For handling the notifications
     fun createNotificationChannel() {
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

    // Initiate Broadcast Receiver to utilize BatteryManager Features
    // Is parsed by registerMyReceiver() to the textview
    private var mBatteryReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            val percent = level * 100 / scale
            val batteryPct = level / scale.toFloat()
            val celsiusLevel = level / 10
            val fahrenheitLevel = celsiusLevel * 9 / 5 + 32
            val kelvinLevel = celsiusLevel + 273
            val celsiusString = "C°"

            // Get the instance of SharedPreferences object
            // Note the original context was "this". This caused a Null Pointer error
            // Used this@BatteryActivity to fix the issue
            val settings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@SensorBatteryActivity)

            //Finds the preference string value and links it with the appropriate temperature calc formula
            when (val unit = settings.getString("batterytempunit", "")) {
                "" -> binding.currentBattery.setText(celsiusLevel.toString() + " " + celsiusString)
                "C°" -> binding.currentBattery.setText(celsiusLevel.toString() + " " + unit)
                "F°" -> binding.currentBattery.setText(fahrenheitLevel.toString() + " " + unit)
                "K°" -> binding.currentBattery.setText(kelvinLevel.toString() + " " + unit)
            }

            ////////////////////////////////////////////////////////////////////////////////////////
            // Notification alert section

            // Gets the unit of measurement from the batterytempunit key in root_preferences.xml
            val unit = settings.getString("batterytempunit", "")

            // Gets the string value from the edit_text_battery_temp key in root_preferences.xml
            val battNumber = settings.getString("edit_text_battery_temp", "")

            // Create an Intent for the activity you want to start
            val resultIntent = Intent(this, SensorBatteryActivity::class.java)

            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addNextIntentWithParentStack(resultIntent)

            // Get the PendingIntent containing the entire back stack
            val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

            // Checks to see if the temperature alert notifications are turned on in root_preferences.xml
            if (settings.getBoolean("switch_preference_battery", true)) {
                // Conditions that must be true for the notifications to work
                // If Celsius is chosen as the unit of measurement
                if (battNumber != null) {
                    if (battNumber == celsiusLevel.toString() && unit == "C°") {
                        val textTitle = "Android Sensor Engine"
                        val textContent = getString(R.string.notify_battery_message) + " " + battNumber + " " + unit

                        val builder = NotificationCompat.Builder(context, ID)

                                .setSmallIcon(R.drawable.ic_notification_logo)
                                .setContentTitle(textTitle)
                                .setContentText(textContent)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setOnlyAlertOnce(true)

                        val notificationManager = NotificationManagerCompat.from(context)
                        notificationManager.notify(123, builder.build())

                        // If Fahrenheit is chosen as the unit of measurement
                    } else if (battNumber == fahrenheitLevel.toString()  && unit == "F°") {
                        val textTitle = "Android Sensor Engine"
                        val textContent = getString(R.string.notify_battery_message) + " " + fahrenheitLevel + " " + unit

                        val builder = NotificationCompat.Builder(context, ID)
                                .setSmallIcon(R.drawable.ic_notification_logo)
                                .setContentTitle(textTitle)
                                .setContentText(textContent)
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setOnlyAlertOnce(true)

                        val notificationManager = NotificationManagerCompat.from(context)
                        notificationManager.notify(123, builder.build())

                        // If Kelvin is chosen as the unit of measurement
                    } else if (battNumber == kelvinLevel.toString() && unit == "K°") {
                        val textTitle = "Android Sensor Engine"
                        val textContent = getString(R.string.notify_battery_message) + " " + battNumber + " " + unit

                        val builder = NotificationCompat.Builder(context, ID)
                                .setSmallIcon(R.drawable.ic_notification_logo)
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

    // This is needed to help open the activity from the notifications
    private fun Intent(broadcastReceiver: BroadcastReceiver, java: Class<SensorBatteryActivity>): Intent? {
    return Intent(this, SensorBatteryActivity::class.java)
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
        val intent = Intent(this, SensorBatteryActivity::class.java)
                .setAction("Battery")

        if (shortcutManager!!.isRequestPinShortcutSupported) {

            val pinShortcutInfo = ShortcutInfo.Builder(this, "battery-shortcut")
                    .setShortLabel(getString(R.string.battery_sensor))
                    .setLongLabel(getString(R.string.battery_sensor))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_battery_icon))
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

    // Pin Shortcut Dialog Data
    private fun alertDialog() {

        OneTimeAlertDialog.Builder(this, "my_dialog_key")
                .setTitle(getString(R.string.pin_shortcut_title))
                .setMessage(getString(R.string.pin_shortut_message))
                .show()
    }
}