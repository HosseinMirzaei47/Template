package com.example.template

import com.example.template.core.util.bodyOrThrow
import com.example.template.home.data.remote.HomeApi
import com.example.template.home.data.remote.UserDataSource
import com.example.template.home.data.servicemodels.UserRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserServiceImpl(private val api: HomeApi) : UserDataSource {

    override suspend fun getUsers(page: Int): UserRes {
        return api.getUsers(page).bodyOrThrow()
    }

    override suspend fun getUsersFlow(page: Int): Flow<UserRes> {
        return flow {
            emit(api.getUsers(3).bodyOrThrow())
        }
    }
}