package com.christianfoulcard.android.androidsensorengine.Sensors

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
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
    var ramInfoDialog: Dialog? = null

    //TextViews
    var ramText: TextView? = null
    var currentRam: TextView? = null
    var ramSensor: TextView? = null

    //ImageViews
    var ramInfo: ImageView? = null

    // Initiate Firebase Analytics
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    //For Ads
    private lateinit var mAdView : AdView

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ram_sensor)

        // Initialize Ads
        MobileAds.initialize(this, "ca-app-pub-9554686964642039~3021936665") //ADMOB App ID
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        //TextViews
        ramText = findViewById(R.id.ram)
        currentRam = findViewById(R.id.current_ram)
        ramSensor = findViewById(R.id.ram_sensor)

        //ImageViews
        ramInfo = findViewById<View>(R.id.info_button) as ImageView

        //Dialog Box for Temperature Info
        ramInfoDialog = Dialog(this)

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
}