package com.christianfoulcard.android.androidsensorengine

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.preference.PreferenceManager
import androidx.annotation.Nullable

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Danial Goodwin
 * Source: https://github.com/danialgoodwin/android--one-time-alert-dialog
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


/** An AlertDialog that will only appear once for the given key.
 *
 * This can be just like a regular AlertDialog and AlertDialog.Builder, except
 * that `create()` is NOT supported and Builder.show() always returns null.
 * This was done to vastly simplify the code. (If create() is used, then a regular AlertDialog
 * without a prefsKey will be returned, thus show() wouldn't be limited.)
 *
 * Example:
 * `
 * private void showRateDialogOnce(Activity activity, String title, String message) {
 * OneTimeAlertDialog.Builder(activity, "rate")
 * .setTitle(title)
 * .setMessage(message)
 * ...
 * .show();
 * }
` *
 *
 */



class OneTimeAlertDialog : AlertDialog {




    /** The value to be stored and checked in the default SharedPreferences.  */
    private var mPrefsKey: String? = null

    // Hide superclass constructors.
    private constructor(context: Context) : super(context) {}
    private constructor(context: Context, theme: Int) : super(context, theme) {}
    private constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {}
    protected constructor(context: Context?, prefsKey: String?) : super(context) {
        mPrefsKey = prefsKey
    }

    protected constructor(context: Context?, theme: Int, prefsKey: String?) : super(context, theme) {
        mPrefsKey = prefsKey
    }

    protected constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?,
                          prefsKey: String?) : super(context, cancelable, cancelListener) {
        mPrefsKey = prefsKey
    }

    /** Creates and shows the dialog if the key is not marked as shown. If marked as shown, then
     * nothing happens.  */
    override fun show() {

        if (!isKeyInPrefs(context, mPrefsKey)) {
            super.show()
                markShown()
        }
    }

    /** Manually mark this dialog as already shown. The next time `show()` is called with this key
     * no dialog will be shown.  */
    fun markShown() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(mPrefsKey, true).apply()
    }

    /** Mark this dialog as not already shown. The next time `show()` is called with this key a
     * dialog will be shown.  */
    fun markNotShown() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(mPrefsKey, false).apply()
    }

    class Builder : AlertDialog.Builder {
        private var prefsKey: String? = null

        // Hide superclass constructors.
        private constructor(context: Context) : super(context) {}
        private constructor(context: Context, theme: Int) : super(context, theme) {}
        constructor(context: Context?, prefsKey: String?) : super(context) {
            this.prefsKey = prefsKey
        }

        constructor(context: Context?, theme: Int, prefsKey: String?) : super(context, theme) {
            this.prefsKey = prefsKey
        }

        /** Creates and shows the dialog if the key is not marked as shown. If marked as shown, then
         * nothing happens.  */
        @Nullable
        override fun show(): AlertDialog? {



            if (!isKeyInPrefs(context, prefsKey)) {
                super.show()
                markShown()
            }

            return null

        }

        /** This operation is not supported. Use `show()` instead.
         * @throws java.lang.UnsupportedOperationException
         */
        //        @NonNull
        //        @Override
        //        public AlertDialog create() {
        //            throw new UnsupportedOperationException("Limitation of this subclass. " +
        //                    "Use `show()` or `OneTimeFullAlertDialog` (doesn't exist yet) instead.");
        //        }
        private fun markShown() {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(prefsKey, true).apply()
        }
    }

    companion object {
        private fun isKeyInPrefs(context: Context, key: String?): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false)
        }
    }
}

private fun Handler.postDelayed(function: () -> Unit) {

}
