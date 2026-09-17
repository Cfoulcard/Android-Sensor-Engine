package com.sensors.audio

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AudioRecorderTest {

    private class FakeSoundInput(
        private val failOnStart: Boolean = false,
        private val failOnStop: Boolean = false,
        private val amplitude: Int = 42,
    ) : SoundInput {
        var startCalls = 0
        var stopCalls = 0
        var releaseCalls = 0

        override fun start() {
            startCalls++
            if (failOnStart) throw IllegalStateException("start failed")
        }

        override fun stop() {
            stopCalls++
            if (failOnStop) throw IllegalStateException("stop called before start")
        }

        override fun release() {
            releaseCalls++
        }

        override fun maxAmplitude(): Int = amplitude
    }

    private val createdInputs = mutableListOf<FakeSoundInput>()

    private fun recorderWith(input: () -> FakeSoundInput) =
        AudioRecorder { input().also { createdInputs += it } }

    @Test
    fun `starting twice creates and starts only one input`() {
        val recorder = recorderWith { FakeSoundInput() }

        assertTrue(recorder.start())
        assertTrue(recorder.start())

        assertEquals(1, createdInputs.size)
        assertEquals(1, createdInputs.single().startCalls)
        assertTrue(recorder.isRecording)
    }

    @Test
    fun `stopping before starting does nothing`() {
        val recorder = recorderWith { FakeSoundInput() }

        recorder.stop()

        assertTrue(createdInputs.isEmpty())
        assertFalse(recorder.isRecording)
    }

    @Test
    fun `stop releases the input even when stopping it throws`() {
        val recorder = recorderWith { FakeSoundInput(failOnStop = true) }
        recorder.start()

        recorder.stop()

        assertEquals(1, createdInputs.single().releaseCalls)
        assertFalse(recorder.isRecording)
    }

    @Test
    fun `starting again after stop uses a fresh input`() {
        val recorder = recorderWith { FakeSoundInput() }
        recorder.start()
        recorder.stop()

        recorder.start()

        assertEquals(2, createdInputs.size)
        assertEquals(1, createdInputs[1].startCalls)
        assertTrue(recorder.isRecording)
    }

    @Test
    fun `a failed start releases the input and reports not recording`() {
        val recorder = recorderWith { FakeSoundInput(failOnStart = true) }

        assertFalse(recorder.start())

        assertEquals(1, createdInputs.single().releaseCalls)
        assertFalse(recorder.isRecording)
    }

    @Test
    fun `a start whose input cannot be created reports not recording`() {
        val recorder = AudioRecorder { throw RuntimeException("setAudioSource failed") }

        assertFalse(recorder.start())
        assertFalse(recorder.isRecording)
    }

    @Test
    fun `amplitude is read from the input while recording and zero otherwise`() {
        val recorder = recorderWith { FakeSoundInput(amplitude = 1234) }

        assertEquals(0, recorder.maxAmplitude())
        recorder.start()
        assertEquals(1234, recorder.maxAmplitude())
        recorder.stop()
        assertEquals(0, recorder.maxAmplitude())
    }
}
