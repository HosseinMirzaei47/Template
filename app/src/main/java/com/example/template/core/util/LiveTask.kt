package com.example.template.core.util

import androidx.lifecycle.LiveData
import com.example.template.core.Result

interface LiveTask<T> {
    fun result(): Result<T>?
    fun asLiveData(): LiveData<LiveTask<Result<T>>>
    fun retry()
    fun execute()
    fun cancel()
}
