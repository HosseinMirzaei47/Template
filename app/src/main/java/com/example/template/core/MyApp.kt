package com.example.template.core

import android.app.Application
import com.example.template.connection.ConnectionManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()
        ConnectionManager.initialize(this)
    }
}