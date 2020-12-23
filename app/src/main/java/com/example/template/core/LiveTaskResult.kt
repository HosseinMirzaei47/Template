package com.example.template.core

sealed class LiveTaskResult<out R> {
    data class Success<out T>(val data: T) : LiveTaskResult<T>()
    data class Error(val exception: Exception) : com.example.template.core.LiveTaskResult<Nothing>()
    object Loading : com.example.template.core.LiveTaskResult<Nothing>()
}

inline fun <T> LiveTaskResult<T>.withResult(
    onLoading: (Boolean) -> Unit,
    onSuccess: (T) -> Unit,
    onError: (Exception) -> Unit,
) {
    when (this) {
        is LiveTaskResult.Success -> {
            onLoading(false)
            onSuccess(data)
        }
        is LiveTaskResult.Error -> {
            onLoading(false)
            onError(exception)
        }
        is LiveTaskResult.Loading -> {
            onLoading(true)
        }
    }
}

inline fun <T> LiveTaskResult<T>.customMap(
    onLoading: (Boolean) -> Unit,
    onSuccess: (T) -> Unit,
    onError: (Exception) -> Unit,
) {
    when (this) {
        is LiveTaskResult.Success -> {
            onLoading(false)
            onSuccess(data)
        }
        is LiveTaskResult.Error -> {
            onLoading(false)
            onError(exception)
        }
        is LiveTaskResult.Loading -> {
            onLoading(true)
        }
    }
}

inline fun <T> LiveTaskResult<T>.withError(
    onError: (Exception) -> Unit,
) {
    if (this is LiveTaskResult.Error) onError(exception)
}


inline fun <T> LiveTaskResult<T>.onSuccess(action: (T) -> Unit): LiveTaskResult<T> {
    if (this is LiveTaskResult.Success) {
        action(data)
    }
    return this
}

inline fun <T> LiveTaskResult<T>.onError(action: (Exception) -> Unit): LiveTaskResult<T> {
    if (this is LiveTaskResult.Error) {
        action(exception)
    }
    return this
}

fun <T> LiveTaskResult<T>.state(): String {
    return when (this) {
        is LiveTaskResult.Loading -> {
            "Loading..."
        }
        is LiveTaskResult.Error -> {
            "Error..."

        }
        is LiveTaskResult.Success -> {
            "Success..."
        }
    }
}



