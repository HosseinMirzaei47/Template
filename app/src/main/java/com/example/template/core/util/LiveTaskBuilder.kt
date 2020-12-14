package com.example.template.core.util

import com.example.template.core.Result

interface LiveTaskBuilder<T> {
    val latestValue: Result<T>?
    suspend fun emit(result: Result<T>)
    fun retryAttempts(attempts: Int)
    fun autoRetry(bool: Boolean)
    fun cancelable(bool: Boolean)
    fun retryable(bool: Boolean)
    fun retry()
}