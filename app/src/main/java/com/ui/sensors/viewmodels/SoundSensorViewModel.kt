package com.ui.sensors.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.androidsensorengine.utils.Constants.SOUND_PREFS
import com.preferences.AppSharedPrefs
import com.sensors.AudioDecibelManager
import com.sensors.audio.AudioRecorder
import com.sensors.audio.MediaRecorderInput

class SoundSensorViewModel(application: Application): AndroidViewModel(application) {

    val decibelLiveData = MutableLiveData<String>("0")
    val averageDecibelLiveData = MutableLiveData<String>("0")
    val highestDecibelLiveData = MutableLiveData<String>("0")
    val lowestDecibelLiveData = MutableLiveData<String>("0")

    private val audioRecorder = AudioRecorder { MediaRecorderInput(application) }

    /** Starts the microphone and decibel polling. Safe to call repeatedly. */
    fun startMeasuring() {
        if (audioRecorder.start()) {
            AppSharedPrefs().saveCondition(SOUND_PREFS, true)
            AudioDecibelManager.listenForAudioDecibels(audioRecorder::maxAmplitude)
        }
    }

    /** Stops decibel polling and releases the microphone. Safe to call repeatedly. */
    fun stopMeasuring() {
        AudioDecibelManager.stopListening()
        audioRecorder.stop()
    }

    fun currentAudioDecibels(): String {
       return when (AudioDecibelManager.audioDecibels) {
            null -> "N/A"
            0 -> "0"
            else -> AudioDecibelManager.audioDecibels.toString()
        }
    }

    fun averageDecibelReading(): String { return AudioDecibelManager.averageDecibelReading()}

    fun highestDecibelReading(): String { return AudioDecibelManager.highestDecibelReading() }

    fun lowestDecibelReading(): String { return AudioDecibelManager.lowestDecibelReading() }

    fun resetDecibelReading() { AudioDecibelManager.resetDecibelReadings() }

    override fun onCleared() {
        stopMeasuring()
    }
}
