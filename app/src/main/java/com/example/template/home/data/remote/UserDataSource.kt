package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.UserRes
import retrofit2.Response

interface UserDataSource {
   suspend fun getUsers() : Response<UserRes>
}