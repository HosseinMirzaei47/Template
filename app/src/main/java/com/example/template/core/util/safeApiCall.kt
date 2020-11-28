package com.example.template.core.util

import retrofit2.HttpException
import retrofit2.Response

suspend inline fun <T> safeApiCall(responseFunction: () -> Response<T>): Resource<T> {

    try {
        val result = responseFunction.invoke()
        if (result.isSuccessful) {
            result.body()?.let {
                it.let {
                    return Resource.Success(it)
                }
            }
            return Resource.Error("error")
        }
        return Resource.Error("error")
    } catch (e: HttpException) {
        return Resource.Error("error")
    } catch (e: Exception) {
        return Resource.Error("error")
    }
}