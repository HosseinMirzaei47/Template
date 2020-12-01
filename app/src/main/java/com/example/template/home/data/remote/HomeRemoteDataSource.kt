package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.UserRes
import retrofit2.Response
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val userDataSource: UserDataSource
) {
    suspend fun getUsers(page: Int): Response<UserRes> = userDataSource.getUsers(page)
}