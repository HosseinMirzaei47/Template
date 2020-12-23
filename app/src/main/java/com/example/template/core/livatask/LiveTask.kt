package com.example.template.core.livatask

import androidx.lifecycle.LiveData
import com.example.template.core.LiveTaskResult

interface LiveTask<T> {
    fun result(): LiveTaskResult<T>?
    fun asLiveData(): LiveData<LiveTask<T>>
    fun retry()
    fun run(): LiveTask<T>
    fun cancel()
}
