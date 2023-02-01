package com.utils

import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.view.WindowCompat

class SystemUi {

    fun hideSystemUIFull(activity: Activity) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun hideSystemUI(activity: Activity) {

        // Hides action bar
        activity.actionBar?.hide()

        // Hide the status bars
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            activity.window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }



}