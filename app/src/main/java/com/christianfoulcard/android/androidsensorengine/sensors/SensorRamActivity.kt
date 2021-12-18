package com.christianfoulcard.android.androidsensorengine.sensors

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.christianfoulcard.android.androidsensorengine.OneTimeAlertDialog
import com.christianfoulcard.android.androidsensorengine.preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.databinding.ActivityRamBinding


class SensorRamActivity : AppCompatActivity() {

    //TODO: Add preferences for Ram data

    //View Binding to call the layout's views
    private lateinit var binding: ActivityRamBinding

    //Dialog popup info
    private var ramInfoDialog: Dialog? = null

    //Handler for dialog pin shortcut dialog box
    val handler = Handler()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        binding = ActivityRamBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Ads
//        MobileAds.initialize(this) {} //ADMOB App ID
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)

        //Dialog Box for Temperature Info
        ramInfoDialog = Dialog(this)

        //Opens Pin Shortcut menu after long pressing the logo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            binding.ramLogo.setOnLongClickListener() {
                sensorShortcut()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        //Animation for TextView fade in
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        binding.ram.startAnimation(`in`)
        binding.currentRam.startAnimation(`in`)
        binding.ramSensor.startAnimation(`in`)
        binding.infoButton.startAnimation(`in`)
    }

    override fun onResume() {
        val i = memorySize().toLong()
        binding.currentRam.text = "$i mB"
        super.onResume()

        // Creates a dialog explaining how to pin the sensor to the home screen
        // Appears after 1 second of opening activity
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            handler.postDelayed({ alertDialog() }, 1000) // 1 second
        }
    }

    override fun onPause() {
        super.onPause()
        //Unbind ACTIVITY_SERVICE to release ram resources
        unbindService(Context.ACTIVITY_SERVICE)
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    private fun memorySize(): Int {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        val availableMegs = mi.availMem / 0x100000L.toDouble()
        val runtime = Runtime.getRuntime()
        val usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L

        //Percentage can be calculated for API 16+
        val percentAvail = mi.availMem / mi.totalMem.toDouble() * 100.0

        return availableMegs.toInt()
    }

    private fun unbindService(activityService: String) {}

    fun showRamDialogPopup(v: View?) {
        ramInfoDialog!!.setContentView(R.layout.dialog_ram)
        ramInfoDialog!!.show()
    }

    fun closeRamDialogPopup(v: View?) {
        ramInfoDialog!!.setContentView(R.layout.dialog_ram)
        ramInfoDialog!!.dismiss()
    }

    //This will add functionality to the menu button within the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

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
        val intent = Intent(this, SensorRamActivity::class.java)
                .setAction("Ram")

        if (shortcutManager!!.isRequestPinShortcutSupported) {

            val pinShortcutInfo = ShortcutInfo.Builder(this, "ram-shortcut")
                    .setShortLabel(getString(R.string.ram_sensor))
                    .setLongLabel(getString(R.string.ram_sensor))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_ram_icon))
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