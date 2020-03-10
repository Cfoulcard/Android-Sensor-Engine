package com.christianfoulcard.android.androidsensorengine.Sensors

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
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
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.christianfoulcard.android.androidsensorengine.Preferences.SettingsActivity
import com.christianfoulcard.android.androidsensorengine.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.IOException

//TODO: Take out a permission

////////////////////////////////////////////////////////////////////////////////////////////////////
class SoundSensorActivity : AppCompatActivity() {

    //Initiates the Media Player to play raw files
    //MediaPlayer mp;

    //Dialog popup info
    var soundInfoDialog: Dialog? = null

    //TextView Data
    var configuredDecibel: TextView? = null
    var decibels: TextView? = null
    var soundSensor: TextView? = null

    //Image Views
    var soundInfo: ImageView? = null

    //For sound recording + converting to sound data
    var mRecorder: MediaRecorder? = null
    var runner: Thread? = null
    val updater = Runnable { updateTv() }
    val mHandler = Handler()

    lateinit var mAdView : AdView

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)

        //Enable for fade in transition
        // overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.sound_sensor)

        // Initialize Ads
        MobileAds.initialize(this, "ca-app-pub-9554686964642039~3021936665") //ADMOB App ID
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // Obtain the FirebaseAnalytics instance and Initiate Firebase Analytics
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)



        //TextViews
        configuredDecibel = findViewById<View>(R.id.current_decibel) as TextView
        decibels = findViewById<View>(R.id.decibels) as TextView
        soundSensor = findViewById<View>(R.id.sound_sensor) as TextView

        //ImageViews
        soundInfo = findViewById<View>(R.id.info_button) as ImageView

        //Dialog Box for Sound Info
        soundInfoDialog = Dialog(this)

        //Animation that plays when entering/exiting Activity
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        configuredDecibel!!.startAnimation(`in`)
        decibels!!.startAnimation(`in`)
        soundSensor!!.startAnimation(`in`)
        soundInfo!!.startAnimation(`in`)

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
                            // Log.i("Noise", "Tock");
                        } catch (e: InterruptedException) {
                        }
                        mHandler.post(updater)
                    }
                }
            }
            (runner as Thread).start()
            Log.d("Noise", "start runner()")
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Microphone recording starts
    public override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            return
        }
        startRecorder()
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
                if (grantResults.size > 0
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
                Log.e("[Monkey]", "IOException: " +
                        Log.getStackTraceString(ioe))
            } catch (e: SecurityException) {
                Log.e("[Monkey]", "SecurityException: " +
                        Log.getStackTraceString(e))
            }
            try {
                mRecorder!!.start()
            } catch (e: SecurityException) {
                Log.e("[Monkey]", "SecurityException: " +
                        Log.getStackTraceString(e))
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
    fun updateTv() {
        if (soundDb() > 0) {
            configuredDecibel!!.text = Integer.toString(soundDb()) + " dB"
        }
        if (soundDb() == 70) {
        }
        //Alternate decibel measurement
        //configuredDecibel.setText(Integer.toString((int) getAmplitudeEMA()) + " Current dB");
    }

    private fun soundDb(): Int {
        return (20 * Math.log10(amplitudeEMA.toDouble())).toInt()
    }

    //Calculates the decibel valve
    private val amplitude: Int
        get() = if (mRecorder != null) mRecorder!!.maxAmplitude else 0

    private val amplitudeEMA: Int
        get() {
            val amp = amplitude
            mEMA = EMA_FILTER * amp + (1 - EMA_FILTER) * mEMA
            return mEMA.toInt()
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Record Audio Permission
    //Upon opening this activity user will be promoted to allow audio recording
    private val isRecordAudioPermissionGranted: Boolean
        private get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        soundInfoDialog!!.setContentView(R.layout.sound_popup_info)
        soundInfoDialog!!.show()
    }

    fun closeSoundDialogPopup(v: View?) {
        soundInfoDialog!!.setContentView(R.layout.sound_popup_info)
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

    //Sound Test provided by MediaPlayer
    /*    public void playSound(View v) {
        mp.start();
    }*/
////////////////////////////////////////////////////////////////////////////////////////////////////
// Helpful Resources
//Measure dB
//https://stackoverflow.com/questions/15693990/measuring-decibels-with-mobile-phone

    companion object {
        //Notification ID
        private const val CHANNEL_ID = "123"
        private var mEMA = 0.0
        private const val EMA_FILTER = 0.6
        //Used for record audio permission
        const val AUDIO_RECORD_REQUEST_CODE = 122
        const val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 99
    }
}