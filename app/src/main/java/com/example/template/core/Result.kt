package com.example.template.core

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

inline fun <T> Result<T>.withResult(
    onLoading: (Boolean) -> Unit,
    onSuccess: (T) -> Unit,
    onError: (Exception) -> Unit
) {
    when (this) {
        is Result.Success -> {
            onLoading(false)
            onSuccess(data)
        }
        is Result.Error -> {
            onLoading(false)
            onError(exception)
        }
    }
}

inline fun <T> Result<T>.withError(
    onError: (Exception) -> Unit
) {
    if (this is Result.Error) onError(exception)
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

inline fun <T> Result<T>.onError(action: (Exception) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(exception)
    }
    return this
}