package com.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics

object LayoutSizingUtil {

    private val displayMetrics = DisplayMetrics()

    /** Builds a dialog box with default layout settings. */
    fun configureDefaultDialogLayoutSize(activity: Activity?, dialog: Dialog?) {
        getDisplayMetrics(activity, dialog)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val params = dialog?.window?.attributes

        with (params) {
            this?.x = 0
            this?.y = 0
            this?.width = (displayMetrics.widthPixels * .9).toInt()
        }

        dialog?.window?.attributes = params
    }

    /** Builds a dialog box with custom layout settings.
     *@param width: Must be a value between 0.01 - 1.0. This is basically a percentage determining
     * how much width the layout will fill.
     * */
    fun configureCustomDialogLayoutSize(activity: Activity?, dialog: Dialog?, width: Double) {

        getDisplayMetrics(activity, dialog)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val params = dialog?.window?.attributes

        with (params) {
            this?.x = 0
            this?.y = 0
            this?.width = (displayMetrics.widthPixels * width).toInt()
        }

        dialog?.window?.attributes = params
    }

    private fun getDisplayMetrics(activity: Activity?, dialog: Dialog?) {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R) {
            val display = activity?.display
            display?.getRealMetrics(displayMetrics)
        } else {
            dialog?.window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        }
    }

    /** Calculates the width of density pixels by dividing the width by the density */
    fun dpWidth() = displayMetrics.widthPixels / displayMetrics.density

    /** Calculates the number of columns available by the width of the screen */
    fun calculateNoOfColumns(): Int {
        return dpWidth().toInt()
    }

    /** Calculates the number of columns available by the width of the screen and divides by a
     * certain amount of columns
     * */
    fun calculateNoOfColumnsForMedia(): Int {
        return (dpWidth().toInt()) / 120
    }

}