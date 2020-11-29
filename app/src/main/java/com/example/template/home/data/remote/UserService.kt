package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.UserRes
import retrofit2.Response

interface UserService {
   suspend fun getUsers() : Response<UserRes>
}