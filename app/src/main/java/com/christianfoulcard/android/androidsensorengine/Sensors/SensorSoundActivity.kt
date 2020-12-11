package com.christianfoulcard.android.androidsensorengine.Sensors

import AUDIO_RECORD_REQUEST_CODE
import EMA
import EMA_FILTER
import MY_PERMISSIONS_REQUEST_RECORD_AUDIO
import android.Manifest
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import com.christianfoulcard.android.androidsensorengine.DataViewModel
import com.christianfoulcard.android.androidsensorengine.OneTimeAlertDialog
import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.databinding.ActivitySoundBinding
import com.christianfoulcard.android.androidsensorengine.makeStatusNotification
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.IOException
import kotlin.math.log10

////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
class SensorSoundActivity : AppCompatActivity() {

    // Use the 'by viewModels()' Kotlin property delegate
    // from the activity-ktx artifact
    private val model: DataViewModel by viewModels()

    //View Binding to call the layout's views
    private lateinit var binding: ActivitySoundBinding

    //Dialog popup info
    private var soundInfoDialog: Dialog? = null

   // var backgroundWorker: BackgroundWorker? = null

    //For sound recording + converting to sound data
    //Handler is also used for pin shortcut dialog box
    private var mRecorder: MediaRecorder? = null
    var runner: Thread? = null
    val updater = Runnable { updateTv() }
    val mHandler = Handler()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        binding = ActivitySoundBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Ads
//        MobileAds.initialize(this) {} //ADMOB App ID
//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)

        // Obtain the FirebaseAnalytics instance and Initiate Firebase Analytics
      //  val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        //Opens Pin Shortcut menu after long pressing the logo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            binding.soundLogo.setOnLongClickListener() {
                sensorShortcut()
            }
        }

        //Dialog Box for Sound Info
        soundInfoDialog = Dialog(this)

        //To request audio permissions upon opening activity
        requestAudioPermissions()

        //This section is used to pick up sound from the user's microphone
        //Adjust milliseconds to change refresh rate of decibel tracker
        if (runner == null) {
            runner = object : Thread() {
                override fun run() {
                    while (runner != null) {
                        try {
                            sleep(500)
                          //   Log.i("Noise", "Tock");
                        } catch (e: InterruptedException) {
                        }
                        mHandler.post(updater)
                    }
                }
            }
            (runner as Thread).start()
          //     Log.d("Noise", "start runner()")
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    public override fun onStart() {
        super.onStart()
        //Animation that plays fading animation when entering/exiting Activity
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        binding.currentDecibel.startAnimation(`in`)
        binding.decibels.startAnimation(`in`)
        binding.soundSensor.startAnimation(`in`)
        binding.infoButton.startAnimation(`in`)
    }

    //Microphone recording starts
    public override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            startRecorder()
        }
        val blurBuilder = OneTimeWorkRequestBuilder<BackgroundWorker>()
        blurBuilder.build()

        // Creates a dialog explaining how to pin the sensor to the home screen
        // Appears after 1 second of opening activity
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mHandler.postDelayed({ alertDialog() }, 1000) // 1 second
        }
    }

    //Stops microphone from recording when user exits activity
    public override fun onPause() {
        super.onPause()
        stopRecorder()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.RECORD_AUDIO)) {

                // Toast.makeText(this, "Please grant permission to measure sound", Toast.LENGTH_LONG).show();
                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO),
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO)
            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO),
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO)
            }
        } else if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            //Go ahead with recording audio now
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    Toast.makeText(this, R.string.sound_permission_denied, Toast.LENGTH_LONG).show()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    //Properties of the microphone
    private fun startRecorder() {
        if (mRecorder == null) {
            mRecorder = MediaRecorder()
            mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mRecorder!!.setOutputFile("/dev/null")
            try {
                mRecorder!!.prepare()
            } catch (ioe: IOException) {

            } catch (e: SecurityException) {

            }
            try {
                mRecorder!!.start()
            } catch (e: SecurityException) {

            }
        }
    }

    //Releases microphone
    private fun stopRecorder() {
        if (mRecorder != null) {
            mRecorder!!.stop()
            mRecorder!!.release()
            mRecorder = null
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
//This section controls the decibel properties picked up by the microphone
//The formula attempts to emulate SPL (Sound Pressure Level)
//Read up more at https://www.wikiwand.com/en/Sound_pressure
//For more decibel detail change Integer to Double
    private fun updateTv() {
        if (soundDb() > 0) {
            binding.currentDecibel.text = Integer.toString(soundDb()) + " dB"
        }

        //Alternate decibel measurement
        //configuredDecibel.setText(Integer.toString((int) getAmplitudeEMA()) + " Current dB");
    }

    fun soundDb(): Int {
        return (20 * log10(amplitudeEMA.toDouble())).toInt()
    }

    //Calculates the decibel valve
    private val amplitude: Int
        get() = if (mRecorder != null) mRecorder!!.maxAmplitude else 0

    private val amplitudeEMA: Int
        get() {
            val amp = amplitude
            EMA = EMA_FILTER * amp + (1 - EMA_FILTER) * EMA
            return EMA.toInt()
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Record Audio Permission
    //Upon opening this activity user will be promoted to allow audio recording
    private val isRecordAudioPermissionGranted: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) { // put your code for Version>=Marshmallow
                true
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this,
                            R.string.sound_permission_denied, Toast.LENGTH_SHORT).show()
                }
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO
                ), AUDIO_RECORD_REQUEST_CODE)
                false
            }
        } else { // put your code for Version < Marshmallow
            true
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun showSoundDialogPopup(v: View?) {
        soundInfoDialog!!.setContentView(R.layout.dialog_sound)
        soundInfoDialog!!.show()
    }

    fun closeSoundDialogPopup(v: View?) {
        soundInfoDialog!!.setContentView(R.layout.dialog_sound)
        soundInfoDialog!!.dismiss()
    }

    //This will add functionality to the menu button within the action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
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
        val soundIntent = Intent(this, SensorSoundActivity::class.java)
                .setAction("Sound")

        if (shortcutManager!!.isRequestPinShortcutSupported) {

            val pinShortcutInfo = ShortcutInfo.Builder(this, "sound-shortcut")
                    .setShortLabel(getString(R.string.sound_sensor))
                    .setLongLabel(getString(R.string.sound_sensor))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_sound_icon))
                    .setIntent(soundIntent)
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

////////////////////////////////////////////////////////////////////////////////////////////////////
// Helpful Resources
//Measure dB
//https://stackoverflow.com/questions/15693990/measuring-decibels-with-mobile-phone


}