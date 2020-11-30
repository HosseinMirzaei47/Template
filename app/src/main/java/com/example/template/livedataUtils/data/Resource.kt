package com.example.template.livedataUtils.data

import okhttp3.ResponseBody

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val code: Int = 0,
    val errorBody: ResponseBody? = null
) {
    companion object {
        fun <T> success(data: T?, code: Int, errorBody: ResponseBody?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, code, errorBody)
        }

        fun <T> error(msg: String, data: T?, code: Int): Resource<T> {
            return Resource(Status.ERROR, data, msg, code)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}