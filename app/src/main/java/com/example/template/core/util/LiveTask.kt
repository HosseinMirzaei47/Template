package com.example.template.core.util

import androidx.lifecycle.LiveData

interface LiveTask<T> {
    fun result(): com.example.template.core.Result<T>?
    fun asLiveData(): LiveData<LiveTask<com.example.template.core.Result<T>>>
    fun retry()
    fun run(): LiveTask<T>
    fun cancel()
}
