package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.Article
import retrofit2.Response

interface ArticleDataSource {

    suspend fun getArticle(userId: Int): Response<List<Article>>

}