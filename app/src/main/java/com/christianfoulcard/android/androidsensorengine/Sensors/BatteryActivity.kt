package com.christianfoulcard.android.androidsensorengine.Sensors

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
    // private val level = registerMyReceiver()

    //ImageViews
    var batteryInfo: ImageView? = null

    // Initiate Firebase Analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private val ID = "333"

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
            //Get the string data from the Preferences

            //Finds the preference string value and links it with the appropriate temperature calc formula
            when (val unit = settings.getString("batterytempunit", "")) {
                "C°" -> currentBattery.setText(celsiusLevel.toString() + " " + unit)
                "F°" -> currentBattery.setText(fahrenheitLevel.toString() + " " + unit)
                "K°" -> currentBattery.setText(kelvinLevel.toString() + " " + unit)
            }

            val unit = settings.getString("batterytempunit", "")
            val battNumber = settings.getString("editTextPreferenceBattery", "")
            if (settings.getBoolean("switch_preference_battery", true)) {
                if (battNumber == "80") {
                    val textTitle = "Android Sensor Engine"
                    val textContent = "Your device's battery has reached " + settings.getString("edit_text_battery_temp", "") + " " +
                            unit

                    //  val intent = Intent(this, BatteryActivity::class.java)
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                    val builder = NotificationCompat.Builder(context, ID)
                            .setSmallIcon(R.drawable.launch_logo_256)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    val notificationManager = NotificationManagerCompat.from(context)
                    notificationManager.notify(123, builder.build())
                }
            }
        }



    }

    //For handling the notifications
    private fun createNotificationChannel() { // Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.battery)
            val description = getString(R.string.battery_sensor)
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.battery_sensor)

        battery_text = findViewById(R.id.battery) as TextView
        currentBattery = findViewById(R.id.current_battery) as TextView
        batterySensor = findViewById(R.id.battery_sensor) as TextView

        //ImageViews
        batteryInfo = findViewById<View>(R.id.info_button) as ImageView

        //Dialog Box for Temperature Info
        batteryInfoDialog = Dialog(this)

        val `in` = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        battery_text.startAnimation(`in`)
        currentBattery.startAnimation(`in`)
        batterySensor.startAnimation(`in`)
        batteryInfo!!.startAnimation(`in`)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        currentBattery = findViewById(R.id.current_battery)

        createNotificationChannel()




    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun registerMyReceiver(): BroadcastReceiver {
        ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(mBatteryReceiver, ifilter)

        return mBatteryReceiver
    }

    override fun onResume() {
        currentBattery.text = "${registerMyReceiver()} percent"
        super.onResume()
    }

    override fun onPause() {
        unregisterReceiver(registerMyReceiver())
        super.onPause()
    }

    override fun onDestroy() {
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