package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.Article
import com.example.template.home.data.servicemodels.Comment
import com.example.template.home.data.servicemodels.UserRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val userDataSource: UserDataSource,
    private val articleDataSource: ArticleDataSource,
    private val commentDataSource: CommentDataSource
) {
    suspend fun getUsers(page: Int): UserRes = userDataSource.getUsers(page)

    suspend fun getUsersFlow(page: Int): Flow<UserRes> = userDataSource.getUsersFlow(page)

    suspend fun getArticle(userId: Int): List<Article> =
        articleDataSource.getArticle(userId)

    suspend fun getComments(articleId: String): List<Comment> =
        commentDataSource.getComments(articleId)


}