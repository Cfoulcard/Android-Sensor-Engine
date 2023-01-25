package com.androidsensorengine.extras

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.androidsensorengine.sensors.SensorPressureActivity

/** Responsible for allowing user action to add a sensor to the home screen */
class PinShortcuts {

    /** Add to a UI element to enable shortcut functionality
     * @param context The activity associated with Pin Shortcuts
     * @param setAction The name of the sensor
     * @param shortcutId sensor name followed by -shortcut
     * @param labelName The string resources we want to all the shortcut
     * @param drawable The drawable resource file to associate with this shortcut
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    fun enableSensorShortcut(
        context: Context,
        setAction: String,
        shortcutId: String,
        labelName: Int,
        drawable: Int?
    ): Boolean {

        if (androidSoftwareIs26OrHigher()) {

            val (shortcutManager, intent) = setupShortcutService(context, setAction)
            if (shortcutsAreSupported(shortcutManager)) {

                val pinShortcutInfo = customizePinShortcutInfo(context, labelName, drawable, intent, shortcutId)

                // Create the PendingIntent object only if your app needs to be notified
                // that the user allowed the shortcut to be pinned. Note that, if the
                // pinning operation fails, your app isn't notified. We assume here that the
                // app has implemented a method called createShortcutResultIntent() that
                // returns a broadcast intent.
                val pinnedShortcutCallbackIntent = shortcutManager?.createShortcutResultIntent(pinShortcutInfo)

                // Configure the intent so that your app's broadcast receiver gets
                // the callback successfully.For details, see PendingIntent.getBroadcast().
                val successCallback = pinnedShortcutCallbackIntent?.let {
                    PendingIntent.getBroadcast(context, /* request code */ 0,
                        it, /* flags */ 0)
                }

                shortcutManager?.requestPinShortcut(pinShortcutInfo, successCallback?.intentSender)
            }
            return true
        } else {
            // Notify user they cannot use shortcuts. Possibly display a pop up showing what their version is vs what it should be
            return false
        }
    }

    private fun androidSoftwareIs26OrHigher() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    private fun shortcutsAreSupported(shortcutManager: ShortcutManager?) =
        if (androidSoftwareIs26OrHigher()) {
            shortcutManager?.isRequestPinShortcutSupported == true
        } else {
            TODO("VERSION.SDK_INT < O")
        }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun setupShortcutService(
        context: Context,
        setAction: String
    ): Pair<ShortcutManager?, Intent> {
        val shortcutManager = getSystemService(context, ShortcutManager::class.java) // May have to clean this up
        val intent = Intent(
            context,
            SensorPressureActivity::class.java
        ).setAction(setAction) // I.E "Pressure"
        return Pair(shortcutManager, intent)
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun customizePinShortcutInfo(
        context: Context,
        labelName: Int,
        drawable: Int?,
        intent: Intent,
        shortcutId: String
    ): ShortcutInfo {
        return ShortcutInfo.Builder(context, shortcutId)
            .setShortLabel(context.getString(labelName))
            .setLongLabel(context.getString(labelName))
            .setIcon(drawable?.let { Icon.createWithResource(context, it) })
            .setIntent(intent)
            .build()
    }
}