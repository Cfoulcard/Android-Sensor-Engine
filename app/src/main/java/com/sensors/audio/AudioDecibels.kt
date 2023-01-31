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
    private var highestDecibel = Int.MIN_VALUE
    private var lowestDecibel = Int.MAX_VALUE
    var isMuted : Boolean = false

    private var audioTimerPeriod : Long = 100 // in milliseconds
    private var baseAudio = 0.0

    private var count = 0
    private var sum = 0

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

    /** Implements a timer task which will update our decibel data */
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

    /** Obtains the average decibel reading we've obtained so far by dividing the sum of our decibels
     * by the amount of times decibel readings have occurred
     */
    fun averageDecibelReading(): String {
        addCurrentDecibel()
        return if (count == 0) {
            "0"
        } else {
            (sum / count).toString()
        }
    }

    /** Finds the highest decibel */
    fun highestDecibelReading(): String {
        return if (highestDecibel == 0) {
            highestDecibel = audioDecibels!!
            highestDecibel.toString()
        } else {
            highestDecibel = audioDecibels?.let { Integer.max(highestDecibel, it) }!!
            highestDecibel.toString()
        }
    }

    /** Finds the lowest decibel */
    fun lowestDecibelReading(): String {
        return if (lowestDecibel == 0) {
            lowestDecibel = audioDecibels!!
            lowestDecibel.toString()
        } else {
            lowestDecibel = audioDecibels?.let { Integer.min(lowestDecibel, it) }!!
            lowestDecibel.toString()
        }
    }

    /** Helper to reset the variables used to make decibel reading work */
    fun resetDecibelReadings() {
        highestDecibel = 0
        lowestDecibel = 0
        sum = 0
        count = 0
    }

    private fun addCurrentDecibel() {
        if (audioDecibels != null) {
            count++
            sum += audioDecibels!!
        }
    }
}