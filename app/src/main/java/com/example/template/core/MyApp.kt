package com.example.template.core

import android.app.Application
import com.example.template.connection.ConnectionManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()

        /*AABuilder.create()
            .setErrorMapper(DefaultErrorMapper)
            .serErrorObserver(ErrorObserver)*/

        DefaultErrorMapper.setMapper(DefaultErrorMapper)
        ConnectionManager.initialize(this)
    }
}