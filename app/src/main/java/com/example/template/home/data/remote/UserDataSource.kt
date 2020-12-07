package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.UserRes

interface UserDataSource {
   suspend fun getUsers(page: Int): UserRes
}