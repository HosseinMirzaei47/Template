package com.example.template.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

object Logger : LoggerInterface {

    private val errorEvent = MutableLiveData<ErrorEvent>()
    private val errorEventDistinct = MutableLiveData<ErrorEvent>()
    private var lastShownErrorTime = 1L
    private var lastErrorEvent = Exception("ali")


    override fun notifyError(errorEvent: ErrorEvent) {
        Logger.errorEvent.postValue(errorEvent)
        if (lastShownErrorTime <= System.currentTimeMillis() - 5000 || errorEvent.exception.javaClass != lastErrorEvent.javaClass) {
            errorEventDistinct.postValue(errorEvent)
            lastErrorEvent = errorEvent.exception
            lastShownErrorTime = System.currentTimeMillis()
        }
    }

    fun testFun(): Boolean {
        var flag = false

        // this.addSource(this) {
        //    if (lastShownErrorTime <= System.currentTimeMillis() - 5000 || it.exception.javaClass != lastErrorEvent.javaClass) {
        //         lastErrorEvent = it.exception
        //          lastShownErrorTime = System.currentTimeMillis()
        //         flag = true
        //    }
        //}

        return flag
    }


    fun watch(owner: LifecycleOwner, observer: Observer<ErrorEvent>) =
        errorEvent.observe(owner, observer)


    fun watchDistinct(owner: LifecycleOwner, observer: Observer<ErrorEvent>) =
        errorEventDistinct.observe(owner, observer)


}

interface LoggerInterface {

    fun notifyError(errorEvent: ErrorEvent)

}