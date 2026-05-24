package com.example.myapplication.util

import android.util.Log

class Logger(
    private val tag: String
) {
    fun debug(message: String) {
        Log.d(tag, message)
    }

    fun info(message: String) {
        Log.i(tag, message)
    }

    fun error(message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }
}
