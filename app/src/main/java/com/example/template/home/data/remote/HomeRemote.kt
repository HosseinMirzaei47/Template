package com.example.template.home.data.remote

import com.example.template.core.util.Resource
import com.example.template.core.util.safeApiCall
import com.example.template.home.data.servicemodels.UserRes
import javax.inject.Inject


class HomeRemote @Inject constructor(private val userDataSource: UserDataSource) {
    suspend fun getUsers(page: Int) : Resource<UserRes> {
        return safeApiCall { userDataSource.getUsers() }
    }
}