package com.christianfoulcard.android.androidsensorengine.Sensors

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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics


class RamActivity : AppCompatActivity() {

    //TODO: Add preferences for Ram data

    //Dialog popup info
    private var ramInfoDialog: Dialog? = null

    //TextViews
    private var ramText: TextView? = null
    private var currentRam: TextView? = null
    private var ramSensor: TextView? = null

    //ImageViews
    private var ramInfo: ImageView? = null
    private var ramLogo: ImageView? = null

    // Initiate Firebase Analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    //For Ads
    private lateinit var mAdView : AdView

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ram_sensor)

        // Initialize Ads
        MobileAds.initialize(this) {} //ADMOB App ID
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        //TextViews
        ramText = findViewById(R.id.ram)
        currentRam = findViewById(R.id.current_ram)
        ramSensor = findViewById(R.id.ram_sensor)

        //ImageViews
        ramInfo = findViewById<View>(R.id.info_button) as ImageView
        ramLogo = findViewById<View>(R.id.ram_logo) as ImageView

        //Dialog Box for Temperature Info
        ramInfoDialog = Dialog(this)

        //Opens Pin Shortcut menu after long pressing the logo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ramLogo!!.setOnLongClickListener() {
                sensorShortcut()
            }
        }

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        //Animation for TextView fade in
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        ramText?.startAnimation(`in`)
        currentRam?.startAnimation(`in`)
        ramSensor?.startAnimation(`in`)
        ramInfo!!.startAnimation(`in`)
    }

    override fun onResume() {
        val i = memorySize().toLong()
        currentRam!!.text = "$i mB"
        super.onResume()
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
        ramInfoDialog!!.setContentView(R.layout.ram_popup_info)
        ramInfoDialog!!.show()
    }

    fun closeRamDialogPopup(v: View?) {
        ramInfoDialog!!.setContentView(R.layout.ram_popup_info)
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
        val intent = Intent(this, RamActivity::class.java)
                .setAction("Ram")

        if (shortcutManager!!.isRequestPinShortcutSupported) {

            val pinShortcutInfo = ShortcutInfo.Builder(this, "ram-shortcut")
                    .setShortLabel(getString(R.string.ram_sensor))
                    .setLongLabel(getString(R.string.ram_sensor))
                    .setIcon(Icon.createWithResource(this, R.drawable.ram_icon))
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
}