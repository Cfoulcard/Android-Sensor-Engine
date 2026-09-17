package com.sensors.audio

import com.sensors.AudioDecibelManager
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class AudioDecibelManagerTest {

    @Before
    fun setUp() {
        AudioDecibelManager.stopListening()
        AudioDecibelManager.audioDecibels = null
        AudioDecibelManager.resetDecibelReadings()
    }

    @After
    fun tearDown() {
        AudioDecibelManager.stopListening()
        AudioDecibelManager.audioDecibels = null
        AudioDecibelManager.resetDecibelReadings()
    }

    @Test
    fun `peak and lowest readings do not crash before any decibel reading exists`() {
        assertEquals("0", AudioDecibelManager.highestDecibelReading())
        assertEquals("0", AudioDecibelManager.lowestDecibelReading())
    }

    @Test
    fun `stopListening stops polling the amplitude source`() {
        val reads = AtomicInteger()
        AudioDecibelManager.listenForAudioDecibels { reads.incrementAndGet() }
        Thread.sleep(350)

        AudioDecibelManager.stopListening()
        Thread.sleep(150)
        val readsAfterStop = reads.get()
        Thread.sleep(400)

        assertEquals(readsAfterStop, reads.get())
    }

    @Test
    fun `listening twice keeps a single polling timer`() {
        val reads = AtomicInteger()
        AudioDecibelManager.listenForAudioDecibels { reads.incrementAndGet() }
        AudioDecibelManager.listenForAudioDecibels { reads.incrementAndGet() }

        Thread.sleep(1050)
        AudioDecibelManager.stopListening()

        // One 100 ms timer yields about 11 reads in 1050 ms; two timers would yield about 22.
        assert(reads.get() in 8..15) { "expected one timer's worth of reads, got ${reads.get()}" }
    }
}
