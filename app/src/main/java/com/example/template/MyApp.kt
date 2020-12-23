package com.example.template

import android.app.Application
import com.part.livetaskcore.connection.ConnectionLiveData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {



    override fun onCreate() {
        super.onCreate()
        ConnectionLiveData.initializer.initialize(this)
    }
}