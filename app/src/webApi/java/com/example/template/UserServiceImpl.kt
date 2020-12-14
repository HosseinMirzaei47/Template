package com.example.template

import com.example.template.core.util.bodyOrThrow
import com.example.template.home.data.remote.HomeApi
import com.example.template.home.data.remote.UserDataSource
import com.example.template.home.data.servicemodels.UserRes

class UserServiceImpl(private val api: HomeApi) : UserDataSource {

    override suspend fun getUsers(page: Int): UserRes {
        return api.getUsers(page).bodyOrThrow()
    }
}