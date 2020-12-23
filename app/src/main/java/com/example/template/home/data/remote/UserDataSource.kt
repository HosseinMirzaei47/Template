package com.example.template.home.data.remote

import androidx.lifecycle.LiveData
import com.example.template.home.data.servicemodels.UserRes
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getUsersLiveData(page: Int): LiveData<UserRes>
    suspend fun getUsers(page: Int): UserRes
    suspend fun getUsersFlow(page: Int): Flow<UserRes>
}