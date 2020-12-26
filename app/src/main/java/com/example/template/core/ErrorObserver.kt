package com.example.template.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

object ErrorObserver : ErrorObserverInterface {

    private val errorEvent = MutableLiveData<ErrorEvent>()
    private val errorEventDistinct = MutableLiveData<ErrorEvent>()
    private var lastShownErrorTime = 1L
    private var timeOut = 5000L
    private var lastErrorEvent = Exception("")

    override fun notifyError(errorEvent: ErrorEvent) {
        ErrorObserver.errorEvent.postValue(errorEvent)
        if (lastShownErrorTime <= System.currentTimeMillis() - timeOut || errorEvent.exception.javaClass != lastErrorEvent.javaClass) {
            errorEventDistinct.postValue(errorEvent)
            lastErrorEvent = errorEvent.exception
            lastShownErrorTime = System.currentTimeMillis()
        }
    }

    fun watch(owner: LifecycleOwner, observer: Observer<ErrorEvent>) =
        errorEvent.observe(owner, observer)

    fun watchDistinct(
        owner: LifecycleOwner,
        mills: Long = timeOut,
        observer: Observer<ErrorEvent>
    ) {
        if (mills in 0..60000) timeOut = mills
        return errorEventDistinct.observe(owner, observer)
    }
}