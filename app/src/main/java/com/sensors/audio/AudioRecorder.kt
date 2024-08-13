package com.sensors.audio

import android.app.Activity
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import com.androidsensorengine.utils.Constants.SOUND_PREFS
import com.androidsensorengine.utils.LogUtils.TAG
import com.preferences.AppSharedPrefs
import timber.log.Timber
import java.io.File
import java.io.IOException

class AudioRecorder {

    //TODO work on file paths for different versions
    //TODO work on pause/resume flow

    private var isRecordingActive: Boolean = false

    /** Creates a [MediaRecorder] instance using the assigned [Activity]. Before we can use the
     * media recorder and measure audio we must first call this.
     * */
    fun createRecorder(activity: Activity) {
        try {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(createOutputFile(activity))
                Timber.tag(TAG).d("Audio Recorder created")
            }
        } catch (e: IllegalStateException) {
            Timber.tag(TAG).e("createRecorder: error configuring audio :: ${e.message}")
        } catch (e: RuntimeException) {
            Timber.tag(TAG).e("createRecorder: File output error :: ${e.message}")
        }
    }

    /** Measures the audio. Can only be called after te recorder has been created */
    fun startRecorder() {
        try {
            recorder?.apply {
                prepare()
                start()
                AppSharedPrefs().saveCondition(SOUND_PREFS, true)
                Timber.tag(TAG).d("Audio Recorder started")
            }
        } catch (e: IllegalStateException) {
            Timber.tag(TAG).e("startRecorder: %s", e.message)
        } catch (e: IOException) {
            Timber.tag(TAG).e("startRecorder: %s", e.message)
        }
    }

    fun pauseRecorder() {
        if (isRecordingActive) {
            try {
                recorder?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pause()
                        Timber.tag(TAG).d("Audio Recorder paused")
                    }
                }
            } catch (e: IllegalStateException) {
                Timber.tag(TAG).e("pauseRecorder: %s", e.message)
            } catch (e: IOException) {
                Timber.tag(TAG).e("pauseRecorder: %s", e.message)
            }
        }
    }

    fun resumeRecorder() {
        if (!isRecordingActive) {
            try {
                recorder?.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        resume()
                        Timber.tag(TAG).d("Audio Recorder resumed")
                    }
                }
            } catch (e: IllegalStateException) {
                Timber.tag(TAG).e("pauseRecorder: " + e.message)
            } catch (e: IOException) {
                Timber.tag(TAG).e("pauseRecorder: " + e.message)
            }
        }
    }

    /** Stops and releases the media recorder from measuring audio */
    fun destroyRecorder()  {
        recorder?.apply {
            try {
                stop()
                release()
                Timber.tag(TAG).d("Audio Recorder destroyed")
            } catch (e: IllegalStateException) {
                Timber.tag(TAG).e("destroyRecorder: " + e.message)
            }
        }
    }

    /** Returns an output file string we can use to tie into the media recorder
     *
     * @param activity The activity we assign for access to the external files directory
     * */
    private fun createOutputFile(activity: Activity): String {
        val name = "audio.mp3"
        Timber.tag(TAG).d("Output path is ${filePath(activity)?.absolutePath}/$name")
        return "${filePath(activity)?.absolutePath}/$name"
    }

    /** Generates the file we need to tie into the output file */
    private fun filePath(activity: Activity): File? {
        return if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.S) {
            activity.getExternalFilesDir(Environment.DIRECTORY_RECORDINGS)
        } else {
            activity.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        }
    }

    companion object {
        var recorder: MediaRecorder? = null
    }
}