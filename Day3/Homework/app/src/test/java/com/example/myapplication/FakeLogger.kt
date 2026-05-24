package com.example.myapplication

import com.example.myapplication.util.Logger

class FakeLogger : Logger("Test") {
    val messages = mutableListOf<String>()

    override fun debug(message: String) {
        messages += message
    }

    override fun info(message: String) {
        messages += message
    }

    override fun error(message: String, throwable: Throwable?) {
        messages += message
    }
}
