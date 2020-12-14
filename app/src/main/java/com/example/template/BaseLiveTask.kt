package com.example.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.template.core.Result
import com.example.template.core.util.LiveTask

internal const val DEFAULT_RETRY_ATTEMPTS = 1

abstract class BaseLiveTask<T> : MediatorLiveData<LiveTask<T>>(), LiveTask<T> {
    var retryCounts = 1
    var retryAttempts = DEFAULT_RETRY_ATTEMPTS
    var latestState: Result<T>? = null

    var cancelable = true
    var retryable = true

    fun cancelable(bool: Boolean): BaseLiveTask<T> {
        cancelable = bool
        return this
    }

    fun retryable(bool: Boolean): BaseLiveTask<T> {
        retryable = bool
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun asLiveData() = this as LiveData<LiveTask<Result<T>>>

    override fun result() = latestState
}