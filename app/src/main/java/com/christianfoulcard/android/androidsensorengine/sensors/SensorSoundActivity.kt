package com.christianfoulcard.android.androidsensorengine.sensors

import android.Manifest
import android.Manifest.permission.RECORD_AUDIO
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import com.christianfoulcard.android.androidsensorengine.DataViewModel
import com.christianfoulcard.android.androidsensorengine.OneTimeAlertDialog
import com.christianfoulcard.android.androidsensorengine.R
import com.christianfoulcard.android.androidsensorengine.databinding.ActivitySoundBinding
import com.christianfoulcard.android.androidsensorengine.preferences.SettingsActivity
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import kotlinx.android.synthetic.main.activity_sound.*
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.math.log10

const val EMA_FILTER = 0.6

// Used for record audio permission
const val AUDIO_RECORD_REQUEST_CODE = 122

/** Uses the MediaRecorder to retrieve decibels */
class SensorSoundActivity : AppCompatActivity() {

    // Used to help with Sound Sensor's Audio parsing
    private var EMA = 0.0

    // Use the 'by viewModels()' Kotlin property delegate
    // from the activity-ktx artifact
    private val model: DataViewModel by viewModels()

    // Use View Binding to call the layout's views
    private lateinit var binding: ActivitySoundBinding

    // Dialog popup info
    private var soundInfoDialog: Dialog? = null

    // var backgroundWorker: BackgroundWorker? = null

    // For sound recording + converting to sound data
    // Handler is also used for pin shortcut dialog box
   // private var mRecorder: MediaRecorder? = null
    private var mRecorder: MediaRecorder? = null
    private var audioFile: String? = null

    var runner: Thread? = null
    val updater = Runnable { updateTv() }
    private val mHandler = Handler()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeSensors)
        super.onCreate(savedInstanceState)
        binding = ActivitySoundBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Opens Pin Shortcut menu after long pressing the logo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            binding.soundLogo.setOnLongClickListener() {
                sensorShortcut()
            }
        }

        // Dialog Box for Sound Info
        soundInfoDialog = Dialog(this)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(Build.VERSION_CODES.O)
    public override fun onStart() {
        super.onStart()
        // Animation that plays fading animation when entering/exiting Activity
        val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
        `in`.duration = 1500
        binding.currentDecibel.startAnimation(`in`)
        binding.decibels.startAnimation(`in`)
        binding.soundSensor.startAnimation(`in`)
        binding.infoButton.startAnimation(`in`)

        enableRecordingPermissionCheck()

    }

    // Microphone recording starts
    public override fun onResume() {
        super.onResume()

       // recordTimer()


        if (mRecorder != null) {
            if (runner == null) {
                runner = object : Thread() {
                    override fun run() {
                        while (runner != null) {
                            try {
                                sleep(500)

                                  Log.i("Noise", "Tock");
                            } catch (e: InterruptedException) {
                            }
                            mHandler.post(updater)
                        }
                    }
                }
                (runner as Thread).start()
                Timber.d("start runner()")
            }
        } else {
            current_decibel!!.text = "N/A"
        }


        // Creates a dialog explaining how to pin the sensor to the home screen
//        // Appears after 1 second of opening activity
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            mHandler.postDelayed({ alertDialog() }, 1000) // 1 second
//        }
    }

    private fun recordTimer() {
            val timer = Timer()

            val nameOfTimer: TimerTask = object: TimerTask() {
                override fun run() {
                    runOnUiThread {
                        updateTv()
                        Timber.d("Test")

                    }
                }

            }
        timer.scheduleAtFixedRate(nameOfTimer, 0, 1000)
        }

    public override fun onStop() {
        super.onStop()
        stopRecorder()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

        if (grantResults.isEmpty()) {
            Toast.makeText(this, R.string.sound_permission_denied, Toast.LENGTH_LONG).show()
        }
}

    @RequiresApi(Build.VERSION_CODES.O)
    @AfterPermissionGranted(AUDIO_RECORD_REQUEST_CODE)
    private fun enableRecordingPermissionCheck() {
        if (EasyPermissions.hasPermissions(this, RECORD_AUDIO)) {

            mRecorder = MediaRecorder()

           // if (mRecorder != null) {
                startRecorder()
         //   }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                host = this@SensorSoundActivity,
                rationale = "Enabling the microphone will allow your device to pick up sounds",
                requestCode = AUDIO_RECORD_REQUEST_CODE,
                RECORD_AUDIO
            )
            Timber.d("Helloooooo")
        }
    }

    /** Properties of the microphone. This will start the recorder to gather sound data. */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecorder() {

        if (mRecorder == null) {

            mRecorder = MediaRecorder()

            if (audioFile == null) {
                audioFile = "/dev/null"
            }

                //mRecorder?.reset()
                mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                mRecorder?.setOutputFile(audioFile)
                mRecorder?.prepare()

            try {
                mRecorder?.start()
            } catch (error: IllegalStateException) {
                Timber.e(error.toString())
            }

            }
        }

    /** Stops the microphone from recording sound data. */
    private fun stopRecorder() {
        if (mRecorder != null) {
            mRecorder!!.stop()
            mRecorder!!.release()
            mRecorder = null
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
   /** Controls the decibel properties picked up by the microphone
    * The formula attempts to emulate SPL (Sound Pressure Level)
    * Read up more at https://www.wikiwand.com/en/Sound_pressure
    * For more decibel detail change Integer to Double */
    private fun updateTv() {

       current_decibel!!.text = Integer.toString(soundDb()) + " dB"

        // Alternate decibel measurement
        // configuredDecibel.setText(Integer.toString((int) getAmplitudeEMA()) + " Current dB");
    }

    fun soundDb(): Int {
        return (20 * log10(amplitudeEMA.toDouble())).toInt()
    }

    // Calculates the decibel valve
    private val amplitude: Int
        get() = if (mRecorder != null) mRecorder!!.maxAmplitude else 0

    private val amplitudeEMA: Int
        get() {
            val amp = amplitude
            EMA = EMA_FILTER * amp + (1 - EMA_FILTER) * EMA
            return EMA.toInt()
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun showSoundDialogPopup(v: View?) {
        soundInfoDialog!!.setContentView(R.layout.dialog_sound)
        soundInfoDialog!!.show()
    }

    fun closeSoundDialogPopup(v: View?) {
        soundInfoDialog!!.setContentView(R.layout.dialog_sound)
        soundInfoDialog!!.dismiss()
    }

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

    // Pin Shortcut Dialog Data
    private fun alertDialog() {

        OneTimeAlertDialog.Builder(this, "my_dialog_key")
                .setTitle(getString(R.string.pin_shortcut_title))
                .setMessage(getString(R.string.pin_shortut_message))
                .show()
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
// Helpful Resources
// Measure dB
// https://stackoverflow.com/questions/15693990/measuring-decibels-with-mobile-phone


}