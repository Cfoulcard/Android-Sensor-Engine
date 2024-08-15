package com.utils

object RegexUtils {

    fun stringAsAValidInteger() : Regex = "-?\\d+".toRegex()

}