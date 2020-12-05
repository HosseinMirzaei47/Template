package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.Article
import com.example.template.home.data.servicemodels.Comment
import com.example.template.home.data.servicemodels.UserRes
import retrofit2.Response
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val userDataSource: UserDataSource,
    private val articleDataSource: ArticleDataSource,
    private val commentDataSource: CommentDataSource
) {
    suspend fun getUsers(page: Int): Response<UserRes> = userDataSource.getUsers(page)

    suspend fun getArticle(userId: Int): Response<List<Article>> =
        articleDataSource.getArticle(userId)

    suspend fun getComments(articleId: String): Response<List<Comment>> =
        commentDataSource.getComments(articleId)


}