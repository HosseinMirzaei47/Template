package com.example.template.core.livatask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.template.core.LiveTaskResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal const val DEFAULT_RETRY_ATTEMPTS = 0

abstract class BaseLiveTask<T> : MediatorLiveData<LiveTask<T>>(), LiveTask<T> {
    var retryCounts = 1
    var retryAttempts = DEFAULT_RETRY_ATTEMPTS
    var latestState: LiveTaskResult<T>? = null

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
    override fun asLiveData(): LiveData<LiveTask<T>> = this as LiveData<LiveTask<T>>

    override fun result() = latestState


    internal suspend fun addDisposableEmit(
        source: LiveData<LiveTaskResult<T>>,
    ): Emitted = withContext(Dispatchers.Main.immediate) {
        addSource(source) {
            latestState = it
            value = this@BaseLiveTask
        }
        Emitted(source = source, mediator = this@BaseLiveTask)
    }
}
