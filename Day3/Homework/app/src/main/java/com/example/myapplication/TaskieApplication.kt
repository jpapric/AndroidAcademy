package com.example.myapplication

import android.app.Application
import com.example.myapplication.di.AppContainer

class TaskieApplication : Application() {
    val appContainer by lazy {
        AppContainer()
    }
}
