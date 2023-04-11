package com.sensors.system

import android.app.ActivityManager
import android.content.Context
import timber.log.Timber
import java.text.DecimalFormat

class SystemInfo(private val context: Context) {

    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private val memoryInfo = getMemoryInfo()
    private val decimalFormat = DecimalFormat("#.##")

    fun getMemoryInfo(): ActivityManager.MemoryInfo {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo
    }

    fun getAvailableMemory(): String {

        val availableMemoryBytes = memoryInfo.availMem
        val availableMemoryMB = availableMemoryBytes / (1024 * 1024f)
        val availableMemoryGB = availableMemoryBytes / (1024 * 1024 * 1024f)

        Timber.tag("MemoryInfo").d("Available Memory in Bytes: %s", availableMemoryBytes)
        Timber.tag("MemoryInfo").d("%s MB", "Available Memory in MB: " + decimalFormat.format(availableMemoryMB))
        Timber.tag("MemoryInfo").d("%s GB", "Available Memory in GB: " + decimalFormat.format(availableMemoryGB))

        return decimalFormat.format(availableMemoryGB)
    }

    fun getTotalMemory(): String {
        val availableTotalMemoryBytes = memoryInfo.totalMem
        val availableTotalMemoryMB = availableTotalMemoryBytes / (1024 * 1024f)
        val availableTotalMemoryGB = availableTotalMemoryBytes / (1024 * 1024 * 1024f)

        Timber.tag("MemoryInfo").d("Total Memory in Bytes: %s", availableTotalMemoryBytes)
        Timber.tag("MemoryInfo").d("%s MB", "Total Memory in MB: " + decimalFormat.format(availableTotalMemoryMB))
        Timber.tag("MemoryInfo").d("%s GB", "Total Memory in GB: " + decimalFormat.format(availableTotalMemoryGB))

        return decimalFormat.format(availableTotalMemoryGB)
    }

    fun getThresholdMemory() : String {
        val thresholdMemoryBytes = memoryInfo.threshold
        val thresholdTotalMemoryMB = thresholdMemoryBytes / (1024 * 1024f)
        val thresholdTotalMemoryGB = thresholdMemoryBytes / (1024 * 1024 * 1024f)

        Timber.tag("MemoryInfo").d("Threshold Memory in Bytes: %s", thresholdMemoryBytes)
        Timber.tag("MemoryInfo").d("%s MB", "Threshold Memory in MB: " + decimalFormat.format(thresholdTotalMemoryMB))
        Timber.tag("MemoryInfo").d("%s GB", "Threshold Memory in GB: " + decimalFormat.format(thresholdTotalMemoryGB))
        return decimalFormat.format(thresholdTotalMemoryGB)
    }

    fun isLowRamDevice(): Boolean {
        return activityManager.isLowRamDevice
    }
}