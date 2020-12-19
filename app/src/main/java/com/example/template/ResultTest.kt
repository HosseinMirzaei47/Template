package com.example.template

sealed class ResultTest<out R> {
    data class CustomSuccess<out T>(val data: T) : ResultTest<T>()
    data class CustomError(val exception: Exception) : ResultTest<Nothing>()
    object CustomLoading : ResultTest<Nothing>()
}
