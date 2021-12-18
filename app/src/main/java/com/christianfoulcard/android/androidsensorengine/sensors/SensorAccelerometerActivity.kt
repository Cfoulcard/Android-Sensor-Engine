package com.christianfoulcard.android.androidsensorengine.sensors

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.christianfoulcard.android.androidsensorengine.OneTimeAlertDialog
import com.christianfoulcard.android.androidsensorengine.preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.databinding.ActivityAccelerometerBinding

////////////////////////////////////////////////////////////////////////////////////////////////////
/** Tracks speed using the LocationListener and GPS data */
class SensorAccelerometerActivity : AppCompatActivity(), LocationListener {

    // View Binding to call the layout's views
    private lateinit var binding: ActivityAccelerometerBinding

    // Dialog popup info
    private var accelerometerInfoDialog: Dialog? = null

    // Sensor Data
    var mlocListener: LocationListener? = null

    // Needed for location permission
    private val REQUESTLOCATIONPERMISSION = 1

    // Handler for dialog pin shortcut dialog box
    val handler = Handler()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        binding = ActivityAccelerometerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Ads
        // MobileAds.initialize(this) {} //ADMOB App ID
        // val adRequest = AdRequest.Builder().build()
        // binding.adView.loadAd(adRequest)

        // Dialog Box for Temperature Info
        accelerometerInfoDialog = Dialog(this)

        // Opens Pin Shortcut menu after long pressing the logo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            binding.accelerometerLogo.setOnLongClickListener() {
                sensorShortcut()
            }
        }

        // Calls the location permission dialog box upon opening this activity
        requestLocationPermissions()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public override fun onStart() {
        super.onStart()
        //Animation fade in for UI elements
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        binding.accelerometer.startAnimation(`in`)
        binding.currentSpeed.startAnimation(`in`)
        binding.accelerometerSensor.startAnimation(`in`)
        binding.infoButton.startAnimation(`in`)
        createNotificationChannel()
    }


    public override fun onResume() {
        super.onResume()
        // This section parses the speed data when the permissions are granted
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

        // Creates a dialog explaining how to pin the sensor to the home screen
        // Appears after 1 second of opening activity
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            handler.postDelayed({ alertDialog() }, 1000) // 1 second
        }
    }



    public override fun onPause() {
        super.onPause()
        // Upon leaving the activity the location data will terminate
        // Used to free memory/battery usage
        val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        lm.removeUpdates(this)
    }

    fun showAccelerometerDialogPopup(v: View?) {
        accelerometerInfoDialog!!.setContentView(R.layout.dialog_accelerometer)
        accelerometerInfoDialog!!.show()
    }

    fun closeAccelerometerDialogPopup(v: View?) {
        accelerometerInfoDialog!!.setContentView(R.layout.dialog_accelerometer)
        accelerometerInfoDialog!!.dismiss()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // This will add functionality to the menu button within the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // Speed Formula for the Accelerometer
    override fun onLocationChanged(p0: Location) {
        // Get the instance of SharedPreferences object
        val settings = PreferenceManager.getDefaultSharedPreferences(this)

        //Get the string data from the Preferences
        val unit = settings.getString("speedunit", "")

        if (p0 == null) {
            when (unit) {
                "MPH", "KM/H", "M/S", "FT/S", "knots" -> binding.currentSpeed.text = "0 $unit"
            }
        } else {
            val speedMs = p0.speed.toInt() // This is the standard which returns meters per second.
            val speedMph = (p0.speed * 2.2369).toInt() // This is speed in mph
            val speedKm = (p0.speed * 3600 / 1000).toInt() // This is speed in km/h
            val speedFts = (p0.speed * 3.2808).toInt() // This is speed in Feet per second
            val speedKnot = (p0.speed * 1.9438).toInt() // This is speed in knots
            val speedMphString = "MPH"
            when (unit) {
                "" -> binding.currentSpeed.text = "$speedMph $speedMphString"
                "MPH" -> binding.currentSpeed.text = "$speedMph $unit"
                "KM/H" -> binding.currentSpeed.text = "$speedKm $unit"
                "M/S" -> binding.currentSpeed.text = "$speedMs $unit"
                "FT/S" -> binding.currentSpeed.text = "$speedFts $unit"
                "Knots" -> binding.currentSpeed.text = "$speedKnot $unit"
            }

            // Gets the unit of measurement from the speedunit key in root_preferences.xml
            val speedNumber = settings.getString("edit_text_speed", "")

            // Create an Intent for the activity you want to start
            val resultIntent = Intent(this, SensorAccelerometerActivity::class.java)

            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            val stackBuilder = TaskStackBuilder.create(this)
            stackBuilder.addNextIntentWithParentStack(resultIntent)

            // Get the PendingIntent containing the entire back stack
            val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

            // Checks to see if the temperature alert notifications are turned on in root_preferences.xml
            if (settings.getBoolean("switch_preference_speed", true)) {
                // Conditions that must be true for the notifications to work
                // If MPH is chosen as the unit of measurement
                if (speedNumber == speedMph.toString() && unit == "MPH") {
                    val textTitle = "Android Sensor Engine"
                    val textContent = getString(R.string.notify_speed_message) + " " + speedNumber + " " + unit

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification_logo)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true)

                    val notificationManager = NotificationManagerCompat.from(this)
                    notificationManager.notify(CHANNEL_ID.toInt(), builder.build())

                    // If KM/H is chosen as the unit of measurement
                } else if (speedNumber == speedKm.toString() && unit == "KM/H") {
                    val textTitle = "Android Sensor Engine"
                    val textContent = getString(R.string.notify_speed_message) + " " + speedNumber + " " + unit

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification_logo)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true)

                    val notificationManager = NotificationManagerCompat.from(this)
                    notificationManager.notify(CHANNEL_ID.toInt(), builder.build())

                    // If M/S is chosen as the unit of measurement
                } else if (speedNumber == speedMs.toString() && unit == "M/S") {
                    val textTitle = "Android Sensor Engine"
                    val textContent = getString(R.string.notify_speed_message) + " " + speedNumber + " " + unit

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification_logo)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true)

                    val notificationManager = NotificationManagerCompat.from(this)
                    notificationManager.notify(CHANNEL_ID.toInt(), builder.build())

                    // If FT/S is chosen as the unit of measurement
                } else if (speedNumber == speedFts.toString() && unit == "FT/S") {
                    val textTitle = "Android Sensor Engine"
                    val textContent = getString(R.string.notify_speed_message) + " " + speedNumber + " " + unit

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification_logo)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true)

                    val notificationManager = NotificationManagerCompat.from(this)
                    notificationManager.notify(CHANNEL_ID.toInt(), builder.build())

                    // If Knots is chosen as the unit of measurement
                } else if (speedNumber == speedKnot.toString() && unit == "Knots") {
                    val textTitle = "Android Sensor Engine"
                    val textContent = getString(R.string.notify_speed_message) + " " + speedNumber + " " + unit

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification_logo)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setOnlyAlertOnce(true)
                    val notificationManager = NotificationManagerCompat.from(this)
                    notificationManager.notify(CHANNEL_ID.toInt(), builder.build())
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name_speed)
            val description = getString(R.string.channel_description_speed)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // This section is is dedicated for the location permission properties
    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                        permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            permission.ACCESS_FINE_LOCATION)) {
                // Toast.makeText(this, "Please grant permission to measure your speed", Toast.LENGTH_LONG).show();
                // Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_FINE_LOCATION),
                        REQUESTLOCATIONPERMISSION)
            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_FINE_LOCATION),
                        REQUESTLOCATIONPERMISSION)
            }
        } else if (ContextCompat.checkSelfPermission(this,
                        permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUESTLOCATIONPERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Adds Pin Shortcut Functionality
    @RequiresApi(Build.VERSION_CODES.O)
    fun sensorShortcut(): Boolean {

        val shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)
        val intent = Intent(this, SensorAccelerometerActivity::class.java)
                .setAction("Accelerometer")

        if (shortcutManager!!.isRequestPinShortcutSupported) {

            val pinShortcutInfo = ShortcutInfo.Builder(this, "speed-shortcut")
                    .setShortLabel(getString(R.string.accelerometer_sensor))
                    .setLongLabel(getString(R.string.accelerometer_sensor))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_speed_icon))
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

    companion object {
        // ID used for notifications
        private const val CHANNEL_ID = "2"
    }


}