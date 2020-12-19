package com.example.template.core.livatask

import com.example.template.core.Result
import kotlinx.coroutines.flow.Flow

interface LiveTaskBuilder<T> {
    val latestValue: Result<T>?
    suspend fun emit(result: Result<T>)
    suspend fun emit(result: Flow<T>)
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