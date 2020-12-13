package com.example.template

import androidx.lifecycle.MediatorLiveData
import com.example.template.core.Result

internal const val DEFAULT_RETRY_ATTEMPTS = 1

abstract class BaseLiveTask<T> : MediatorLiveData<Result<T>>() {
    var retryCounts = 1
    var retryAttempts = DEFAULT_RETRY_ATTEMPTS
    private var cancelable = true
    private var retryable = true

    abstract fun retry()
    abstract fun execute()
    abstract fun cancel()

    fun cancelable(bool: Boolean): BaseLiveTask<T> {
        cancelable = bool
        return this
    }

    fun retryable(bool: Boolean): BaseLiveTask<T> {
        retryable = bool
        return this
    }
}