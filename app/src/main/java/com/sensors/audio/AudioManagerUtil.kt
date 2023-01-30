package com.sensors.audio

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager

class AudioManagerUtil {

    private var audioManager: AudioManager? = null
    private var audioMode: Int? = null

    fun startAudioManager(context: Context) {
        audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
        audioMode = audioManager?.mode
        audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager?.mode= audioMode as Int
    }

}

