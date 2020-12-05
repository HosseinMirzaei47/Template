package com.example.template.core.util

import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

fun readServerError(exception: HttpException): ServerException {
    val errorString = exception.response()?.errorBody()?.string()
        ?: return ServerException(
            meta = Meta(
                message = "know error ",
                statusCode = exception.code(),
                requestTime = 0
            )
        )
    val errorBodyObject = JSONObject(errorString)
    val errorData = errorBodyObject.getJSONObject("meta")
    val errorMessage = errorData.getString("message")
    val errorStatusCode = errorData.getInt("statusCode")
    val errorRequestTime = errorData.getInt("requestTime")
    return ServerException(
        meta = Meta(
            message = errorMessage,
            statusCode = errorStatusCode,
            requestTime = errorRequestTime
        )
    )
}

@Throws(ServerException::class)
fun <T> Response<T>.bodyOrThrow(): T {
    if (!isSuccessful) throw HttpException(this)
    return body()!!
}

object NetworkStatusCode {
    const val STATUS_DEFAULT = -1
    const val STATUS_CONNECTION_LOST = 0
}