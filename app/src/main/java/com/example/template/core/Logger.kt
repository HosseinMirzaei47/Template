package com.example.template.core

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

object Logger : MediatorLiveData<ErrorEvent>() {

    var errorEvent = MutableLiveData<ErrorEvent>()

    var lastShownErrorTime = 1L
    var lastErrorEvent = Exception("ali")

    init {

    }

    fun testFun(): Boolean {
        var flag = false

        this.addSource(errorEvent) {
            if (lastShownErrorTime <= System.currentTimeMillis() - 5000 || it.exception.javaClass != lastErrorEvent.javaClass) {
                this.postValue(it)
                lastErrorEvent = it.exception
                lastShownErrorTime = System.currentTimeMillis()
                flag = true
            }
        }
        return flag
    }
}