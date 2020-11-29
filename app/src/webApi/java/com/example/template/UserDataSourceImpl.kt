package com.example.template

import com.example.template.home.data.remote.HomeApi
import com.example.template.home.data.remote.UserDataSource
import com.example.template.home.data.servicemodels.UserRes
import retrofit2.Response
import retrofit2.Retrofit

class UserDataSourceImpl(private val retrofit: Retrofit) : UserDataSource {
    private val api=retrofit.create(HomeApi::class.java)

    override suspend fun getUsers(): Response<UserRes>{
        return api.getUsers(1)
    }
}