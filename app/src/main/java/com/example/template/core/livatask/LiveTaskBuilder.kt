package com.example.template.core.livatask

import androidx.lifecycle.LiveData
import com.example.template.core.LiveTaskResult
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.flow.Flow

interface LiveTaskBuilder<T> {
    val latestValue: LiveTaskResult<T>?
    suspend fun emit(result: LiveTaskResult<T>)
    suspend fun emit(result: Flow<T>)
    suspend fun emitSource(source: LiveData<LiveTaskResult<T>>): DisposableHandle
    fun retryAttempts(attempts: Int)
    fun autoRetry(bool: Boolean)
    fun cancelable(bool: Boolean)
    fun retryable(bool: Boolean)
}

interface CombinerBuilder {
    val latestValue: LiveTaskResult<Any>?
    fun cancelable(bool: Boolean)
    fun retryable(bool: Boolean)
}