package com.example.template.core

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

object Logger : MediatorLiveData<ErrorEvent>() {

    val errorEvent = MutableLiveData<ErrorEvent>()

    private var lastShownErrorTime = 1L
    private var lastErrorEvent = Exception()

    init {
        this.addSource(errorEvent) {
            if (lastShownErrorTime <= System.currentTimeMillis() - 5000 || it.exception.javaClass != lastErrorEvent.javaClass) {
                this.postValue(it)
                lastErrorEvent = it.exception
                lastShownErrorTime = System.currentTimeMillis()
            }
        }
    }
}