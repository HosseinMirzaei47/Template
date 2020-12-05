package com.example.template.core

import android.app.Application
import com.example.template.connection.ConnectionLiveData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    companion object {
        lateinit var connectionLiveData: ConnectionLiveData
    }

    override fun onCreate() {
        super.onCreate()
        connectionLiveData = ConnectionLiveData(this)
    }
}