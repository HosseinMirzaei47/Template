package com.example.template.core

sealed class Result<out R> {
    data class Success<out T>(val data: T) : com.example.template.core.Result<T>()
    data class Error(val exception: Exception) : com.example.template.core.Result<Nothing>()
    object Loading : com.example.template.core.Result<Nothing>()
}


inline fun <T> com.example.template.core.Result<T>.withResult(
    onLoading: (Boolean) -> Unit,
    onSuccess: (T) -> Unit,
    onError: (Exception) -> Unit
) {
    when (this) {
        is com.example.template.core.Result.Success -> {
            onLoading(false)
            onSuccess(data)
        }
        is com.example.template.core.Result.Error -> {
            onLoading(false)
            onError(exception)
        }
        is Result.Loading -> {
            onLoading(true)
        }
    }
}

inline fun <T> com.example.template.core.Result<T>.customMap(
    onLoading: (Boolean) -> Unit,
    onSuccess: (T) -> Unit,
    onError: (Exception) -> Unit
) {
    when (this) {
        is com.example.template.core.Result.Success -> {
            onLoading(false)
            onSuccess(data)
        }
        is com.example.template.core.Result.Error -> {
            onLoading(false)
            onError(exception)
        }
        is Result.Loading -> {
            onLoading(true)
        }
    }
}

inline fun <T> com.example.template.core.Result<T>.withError(
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

fun <T> Result<T>.state(): String {
    return when (this) {
        is Result.Loading -> {
            "Loading..."
        }
        is Result.Error -> {
            "Error..."

        }
        is Result.Success -> {
            "Success..."
        }
    }
}



