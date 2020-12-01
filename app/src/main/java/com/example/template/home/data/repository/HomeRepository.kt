package com.example.template.home.data.repository

import com.example.template.home.data.remote.HomeRemoteDataSource
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource
) {
    suspend fun getUsers(page: Int) = homeRemoteDataSource.getUsers(page)
}