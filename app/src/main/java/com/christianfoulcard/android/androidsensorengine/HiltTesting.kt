package com.christianfoulcard.android.androidsensorengine

import javax.inject.Inject

class HiltTesting @Inject constructor(private val theOtherClass: HiltSecondTesting) {
    fun logString(): String {
        return "Hilt is working"
    }

    fun logOtherString(): String {
        return theOtherClass.logStringer()
    }
}