package com.example.template.core

import android.app.Application
import com.example.template.connection.ConnectionLiveData
import com.example.template.core.util.LoggerTasks
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    companion object {
        lateinit var connectionLiveData: ConnectionLiveData
        val loggerTasks = LoggerTasks()
    }

    override fun onCreate() {
        super.onCreate()
        connectionLiveData = ConnectionLiveData(this)
    }
}