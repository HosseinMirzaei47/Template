package com.example.template.home.data.repository

import com.example.template.home.data.remote.HomeRemoteDataSource
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource
) {
    suspend fun getUsers(page: Int) = homeRemoteDataSource.getUsers(page)

    suspend fun getUsersFlow(page: Int) = homeRemoteDataSource.getUsersFlow(page)

    suspend fun getArticles(userId: Int) = homeRemoteDataSource.getArticle(userId)

    suspend fun getComments(articleId: String) = homeRemoteDataSource.getComments(articleId)

    fun getUsersLiveData(page: Int) = homeRemoteDataSource.getUsersLiveData(page)
}