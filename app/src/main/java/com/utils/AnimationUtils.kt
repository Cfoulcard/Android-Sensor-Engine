package com.androidsensorengine.utils

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

object AnimationUtils {

    //TODO dialog fragment animation migration

    /** Fades out a UI element from the screen */
    fun fadeOut(
        fromAlpha: Float,
        toAlpha: Float,
        duration: Long,
        view: View
    ) {
        val `in`: Animation = AlphaAnimation(fromAlpha, toAlpha).also {
            it.duration = duration
        }
        view.startAnimation(`in`)
    }

    /** Fades out a UI element from the screen with a start offset */
    fun fadeOutWithStartOffset(
        fromAlpha: Float,
        toAlpha: Float,
        duration: Long,
        startOffset: Long,
        view: View
    ) {
        val `in`: Animation = AlphaAnimation(fromAlpha, toAlpha).also{
            it.startOffset = startOffset
            it.duration = duration
        }
        view.startAnimation(`in`)
    }

}