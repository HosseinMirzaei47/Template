package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.UserRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {
    @GET("users?delay=2")
    suspend fun getUsers(
        @Query("page") page: Int,
    ): Response<UserRes>
}