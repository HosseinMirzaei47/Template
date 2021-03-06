package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.Comment

interface CommentDataSource {
    suspend fun getComments(articleId: String): List<Comment>
}