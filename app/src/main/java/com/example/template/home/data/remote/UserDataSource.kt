package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.UserRes
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
   suspend fun getUsers(page: Int): UserRes
   suspend fun getUsersFlow(page: Int): Flow<UserRes>
}