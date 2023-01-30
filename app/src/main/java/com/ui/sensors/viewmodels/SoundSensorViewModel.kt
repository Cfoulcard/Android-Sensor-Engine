package com.ui.sensors.viewmodels

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sensors.AudioDecibels
import com.sensors.audio.AudioRecorder

class SoundSensorViewModel: ViewModel() {

    val decibelLiveData = MutableLiveData<String>("0")
    val highestDecibelLiveData = MutableLiveData<String>("0")

    fun createRecorder(activity: Activity) { AudioRecorder().createRecorder(activity) }

    fun startRecorder() { AudioRecorder().startRecorder() }

    fun pauseRecorder() { AudioRecorder().pauseRecorder() }

    fun resumeRecorder() { AudioRecorder().resumeRecorder() }

    fun destroyRecorder() { AudioRecorder().destroyRecorder() }

    fun measureDecibels(activity: ComponentActivity) {
        AudioDecibels.listenForAudioDecibels(activity, AudioRecorder.recorder)
    }

    fun currentAudioDecibels(): String {
       return when (AudioDecibels.audioDecibels) {
            null -> "N/A"
            0 -> "0"
            else -> AudioDecibels.audioDecibels.toString()
        }
    }

    fun highestDecibelReading(): String { return AudioDecibels.highestDecibelReading()}

}
