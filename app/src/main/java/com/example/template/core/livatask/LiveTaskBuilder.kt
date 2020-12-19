package com.example.template.core.livatask

import androidx.lifecycle.LiveData
import com.example.template.core.Result
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.flow.Flow

interface LiveTaskBuilder<T> {
    val latestValue: Result<T>?
    suspend fun emit(result: Result<T>)
    suspend fun emit(result: Flow<T>)
    suspend fun emit(result: LiveData<LiveTask<T>>): DisposableHandle
    fun retryAttempts(attempts: Int)
    fun autoRetry(bool: Boolean)
    fun cancelable(bool: Boolean)
    fun retryable(bool: Boolean)
    fun retry()
}

interface CombinerBuilder {
    val latestValue: Result<Any>?
    fun cancelable(bool: Boolean)
    fun retryable(bool: Boolean)
}