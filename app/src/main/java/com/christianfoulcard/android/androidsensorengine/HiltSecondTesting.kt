package com.christianfoulcard.android.androidsensorengine

import javax.inject.Inject

class HiltSecondTesting @Inject constructor() {
    fun logStringer(): String {
        return "Hilt is working double time"
    }
}