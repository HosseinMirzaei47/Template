package com.example.template.livedataUtils.data

import okhttp3.ResponseBody

sealed class MyResource<out T> {

    data class Success<out T>(val data: T?, val code: Int, val errorBody: ResponseBody?) :
        MyResource<T>()

    data class Error<out T>(val message: String, val data: T?, val code: Int) : MyResource<T>()

    data class Loading<out T>(val data: T?) : MyResource<T>()

}