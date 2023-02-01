package com.ui.sensors.viewmodels

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sensors.AudioDecibelManager
import com.sensors.audio.AudioRecorder

class SoundSensorViewModel: ViewModel() {

    val decibelLiveData = MutableLiveData<String>("0")
    val averageDecibelLiveData = MutableLiveData<String>("0")
    val highestDecibelLiveData = MutableLiveData<String>("0")
    val lowestDecibelLiveData = MutableLiveData<String>("0")

    fun createRecorder(activity: Activity) { AudioRecorder().createRecorder(activity) }

    fun startRecorder() { AudioRecorder().startRecorder() }

    fun pauseRecorder() { AudioRecorder().pauseRecorder() }

    fun resumeRecorder() { AudioRecorder().resumeRecorder() }

    fun destroyRecorder() { AudioRecorder().destroyRecorder() }

    fun measureDecibels(activity: ComponentActivity) {
        AudioDecibelManager.listenForAudioDecibels(activity, AudioRecorder.recorder)
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

}
