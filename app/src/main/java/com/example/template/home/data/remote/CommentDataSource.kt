package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.Comment
import retrofit2.Response

interface CommentDataSource {

    suspend fun getComments(articleId: String): Response<List<Comment>>
}