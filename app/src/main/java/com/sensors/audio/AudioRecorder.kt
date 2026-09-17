package com.sensors.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import com.androidsensorengine.utils.LogUtils.TAG
import timber.log.Timber
import java.io.File

/** A source of microphone audio the app can start, stop and read loudness from */
interface SoundInput {
    fun start()
    fun stop()
    fun release()
    fun maxAmplitude(): Int
}

/** Owns at most one [SoundInput] at a time. Starting while recording and stopping while idle are
 * both no-ops, and a stopped input is always released, so the recorder is safe to drive from
 * activity lifecycle callbacks that can repeat.
 *
 * @param createInput Builds a fresh input for each recording session
 * */
class AudioRecorder(private val createInput: () -> SoundInput) {

    private var input: SoundInput? = null

    val isRecording: Boolean
        get() = input != null

    /** Starts measuring audio. Returns true if the recorder is recording afterwards */
    fun start(): Boolean {
        if (isRecording) return true
        val newInput = try {
            createInput()
        } catch (e: RuntimeException) {
            Timber.tag(TAG).e("start: could not create audio input :: ${e.message}")
            return false
        }
        return try {
            newInput.start()
            input = newInput
            Timber.tag(TAG).d("Audio Recorder started")
            true
        } catch (e: RuntimeException) {
            Timber.tag(TAG).e("start: %s", e.message)
            newInput.release()
            false
        }
    }

    /** Stops measuring audio and releases the microphone */
    fun stop() {
        val current = input ?: return
        input = null
        try {
            current.stop()
        } catch (e: RuntimeException) {
            Timber.tag(TAG).e("stop: %s", e.message)
        } finally {
            current.release()
            Timber.tag(TAG).d("Audio Recorder stopped")
        }
    }

    /** The loudest amplitude since the last call, or 0 when not recording */
    fun maxAmplitude(): Int = try {
        input?.maxAmplitude() ?: 0
    } catch (e: RuntimeException) {
        Timber.tag(TAG).e("maxAmplitude: %s", e.message)
        0
    }
}

/** A [SoundInput] backed by the device microphone through [MediaRecorder] */
class MediaRecorderInput(context: Context) : SoundInput {

    private val recorder: MediaRecorder = createMediaRecorder(context).apply {
        try {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(createOutputFile(context))
        } catch (e: RuntimeException) {
            release()
            throw e
        }
    }

    override fun start() {
        recorder.prepare()
        recorder.start()
    }

    override fun stop() = recorder.stop()

    override fun release() = recorder.release()

    override fun maxAmplitude(): Int = recorder.maxAmplitude

    private fun createMediaRecorder(context: Context): MediaRecorder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

    /** Returns an output file string we can use to tie into the media recorder
     *
     * @param context The context we use for access to the external files directory
     * */
    private fun createOutputFile(context: Context): String {
        val name = "audio.mp3"
        Timber.tag(TAG).d("Output path is ${filePath(context)?.absolutePath}/$name")
        return "${filePath(context)?.absolutePath}/$name"
    }

    /** Generates the file we need to tie into the output file */
    private fun filePath(context: Context): File? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getExternalFilesDir(Environment.DIRECTORY_RECORDINGS)
        } else {
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        }
    }
}
