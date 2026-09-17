package com.sensors

import com.androidsensorengine.utils.Constants.BASE_AUDIO_FILTER
import java.util.*
import kotlin.math.log10

/** Decibels are a way to measure how loud or quiet something is. This Contains the properties we
 * need to measure sound decibels. As long as an amplitude source is provided, we can measure
 * decibels with no problem. */
object AudioDecibelManager {

    @Volatile
    var audioDecibels : Int? = null
    private var highestDecibel = Int.MIN_VALUE
    private var lowestDecibel = Int.MAX_VALUE
    var isMuted : Boolean = false

    private var audioTimerPeriod : Long = 100
    private var baseAudio = 0.0
    private var timer: Timer? = null

    private var count = 0
    private var sum = 0

    /** Returns the traditional audio format of a decibel. This number will be limited to a maximum
     * of 90 on most devices due to hardware and software limitations. There's a chance we may pick up
     * a negative Int value of -2147483648 upon first initiation - if this pops up we do not return
     * decibel data */
    private fun parseDecibelReading(amplitude: Int): Int {
        val decibelLevel = (20 * log10(amplitudeAudioFilter(amplitude).toDouble())).toInt()
        return if (decibelLevel >= 1) decibelLevel else 0
    }

    /** Starts a timer task which will update our decibel data from [readAmplitude]. Any timer
     * already running is replaced, so only one keeps polling. */
    @Synchronized
    fun listenForAudioDecibels(readAmplitude: () -> Int) {
        stopListening()
        val newTimer = Timer()

        val toggleAutoMuteTimer: TimerTask = object: TimerTask() {
            override fun run() {
                if (isMuted) {
                    cancel()
                } else {
                    audioDecibels = parseDecibelReading(readAmplitude())
                }
            }
        }
        newTimer.scheduleAtFixedRate(toggleAutoMuteTimer, 0, audioTimerPeriod)
        timer = newTimer
    }

    /** Stops the timer started by [listenForAudioDecibels] */
    @Synchronized
    fun stopListening() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    /** Runs the the audio through a filter to return data we can use to convert to a proper decibel
     * level */
    private fun amplitudeAudioFilter(amplitude: Int) : Int {
        baseAudio = BASE_AUDIO_FILTER * amplitude + (1 - BASE_AUDIO_FILTER) * baseAudio
        return baseAudio.toInt()
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
        val current = audioDecibels ?: return readingOrZero(highestDecibel)
        highestDecibel = if (highestDecibel == 0) current else Integer.max(highestDecibel, current)
        return highestDecibel.toString()
    }

    /** Finds the lowest decibel */
    fun lowestDecibelReading(): String {
        val current = audioDecibels ?: return readingOrZero(lowestDecibel)
        lowestDecibel = if (lowestDecibel == 0) current else Integer.min(lowestDecibel, current)
        return lowestDecibel.toString()
    }

    /** Helper to reset the variables used to make decibel reading work */
    fun resetDecibelReadings() {
        highestDecibel = 0
        lowestDecibel = 0
        sum = 0
        count = 0
    }

    private fun readingOrZero(reading: Int): String =
        if (reading == Int.MIN_VALUE || reading == Int.MAX_VALUE) "0" else reading.toString()

    private fun addCurrentDecibel() {
        val current = audioDecibels ?: return
        count++
        sum += current
    }
}
