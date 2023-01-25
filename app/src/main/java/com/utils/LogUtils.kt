package com.androidsensorengine.utils

/** Simple Tag Object to return the class name throughout the app */
object LogUtils {

    val Any.TAG: String
        get() {
            return when (true) {
                !javaClass.isAnonymousClass -> {
                    val name = javaClass.simpleName
                    if (name.length <= 23) name else name.substring(0, 23)// first 23 chars
                }
                javaClass.isAnonymousClass -> {
                    val name = javaClass.enclosingClass.simpleName
                    if (name.length <= 23) name else name.substring(name.length - 23, name.length)// last 23 chars
                }
                else -> {
                    val name = javaClass.name
                    if (name.length <= 23) name else name.substring(name.length - 23, name.length)// last 23 chars
                }
            }
        }

}