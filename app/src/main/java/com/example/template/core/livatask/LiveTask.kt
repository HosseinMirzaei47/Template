package com.example.template.core.livatask

import androidx.lifecycle.LiveData
import com.example.template.core.Result

interface LiveTask<T> {
    fun result(): com.example.template.core.Result<T>?
    fun asLiveData(): LiveData<LiveTask<Result<T>>>
    fun retry()
    fun run(): LiveTask<T>
    fun cancel()
}
