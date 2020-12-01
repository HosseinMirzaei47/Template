package com.example.template.core

import com.example.template.core.util.NetworkStatusCode.STATUS_DEFAULT

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int = STATUS_DEFAULT) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

inline fun <T> Result<T>.withResult(
    onLoading: (Boolean) -> Unit,
    onSuccess: (T) -> Unit,
    onError: (String, Int) -> Unit
) {
    when (this) {
        is Result.Success -> {
            onLoading(false)
            onSuccess(data)
        }
        is Result.Error -> {
            onLoading(false)
            onError(message, code)
        }
    }
}


inline fun <T> Result<T>.withError(
    onError: (String, Int) -> Unit
) {
    if (this is Result.Error) {
        onError(message, code)
    }
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

inline fun <T> Result<T>.onError(action: () -> Unit): Result<T> {
    if (this is Result.Error) {
        action()
    }
    return this
}