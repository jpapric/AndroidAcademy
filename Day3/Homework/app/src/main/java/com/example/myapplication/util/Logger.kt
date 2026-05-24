package com.example.myapplication.util

import android.util.Log

open class Logger(
    private val tag: String
) {
    open fun debug(message: String) {
        Log.d(tag, message)
    }

    open fun info(message: String) {
        Log.i(tag, message)
    }

    open fun error(message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }
}
