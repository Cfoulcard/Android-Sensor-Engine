package com.sensors

import android.media.MediaRecorder
import android.util.Log
import androidx.activity.ComponentActivity
import com.androidsensorengine.utils.Constants.BASE_AUDIO_FILTER
import com.androidsensorengine.utils.LogUtils.TAG
import java.util.*
import kotlin.math.log10

/** Decibels are a way to measure how loud or quiet something is. This Contains the properties we
 * need to measure sound decibels. As long as a [MediaRecorder] is provided, we can measure
 * decibels with no problem. */
object AudioDecibels {

    var audioDecibels : Int? = null
    var isMuted : Boolean = false

    private var audioTimerPeriod : Long = 100 // in milliseconds
    private var baseAudio = 0.0

    fun highestDecibelReading() : String {
        if (audioDecibels != null) {
            var number = Int.MIN_VALUE
            val current = audioDecibels
            if (current != null) {
                if (current > number) { number = current }
            }
            return number.toString()
        }
        return ""
    }

    /** Returns the traditional audio format of a decibel. This number will be limited to a maximum
     * of 90 on most devices due to hardware and software limitations. There's a chance we may pick up
     * a negative Int value of -2147483648 upon first initiation - if this pops up we do not return
     * decibel data */
    private fun parseDecibelReading(mediaRecorder: MediaRecorder?): Int {
        try {
            return if (mediaRecorder != null) {
                Log.d("Media", "parseDecibelReading: test 1")
                val decibelLevel =
                    (20 * log10(amplitudeAudioFilter(mediaRecorder).toDouble())).toInt()
                if (decibelLevel >= 1) {
                    Log.d("Media", "parseDecibelReading: test 3")

                    decibelLevel
                } else {
                    Log.d(
                        "Media",
                        "parseDecibelReading: test 4 - max is ${mediaRecorder.maxAmplitude}"
                    )

                    0
                }
            } else {
                Log.d("Media", "parseDecibelReading: test 2")

                0
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "parseDecibelReading: ${e.message}")
        }
        return 0
    }

    fun listenForAudioDecibels(activity: ComponentActivity, mediaRecorder: MediaRecorder?) {
        val timer = Timer()

        val toggleAutoMuteTimer: TimerTask = object: TimerTask() {
            override fun run() {

                if (activity.isDestroyed || activity.isFinishing) {
                    timer.cancel()
                    timer.purge()
                } else if (isMuted) {
                    timer.cancel()
                    timer.purge()
                } else if (!isMuted) {
                    Log.d("Hello", "run: $audioDecibels")
                    audioDecibels = parseDecibelReading(mediaRecorder)
                }
            }
        }
        timer.scheduleAtFixedRate(toggleAutoMuteTimer, 0, audioTimerPeriod)
    }

    /** Runs the the audio through a filter to return data we can use to convert to a proper decibel
     * level */
    private fun amplitudeAudioFilter(mediaRecorder: MediaRecorder) : Int {
        val amp = getAudioAmplitude(mediaRecorder)
        baseAudio = BASE_AUDIO_FILTER * amp + (1 - BASE_AUDIO_FILTER) * baseAudio
        return baseAudio.toInt()
    }

    /** Get our audio by measuring the media recorder sound wave data via amplitude. This data
     * returns the strength of sound waves (loudness/volume)
     */
    private fun getAudioAmplitude(mediaRecorder: MediaRecorder) : Int {
        return try {
            mediaRecorder.maxAmplitude
        } catch (e: IllegalStateException) {
            Log.e("Media", "getAudioAmplitude: ${e.message}", )
         //   Toast.makeText(T2DApplication.getAppContext(), "An error occurred while analyzing audio", Toast.LENGTH_SHORT).show()
            0
        }
    }
}