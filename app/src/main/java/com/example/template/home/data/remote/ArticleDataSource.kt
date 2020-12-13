package com.example.template.home.data.remote

import com.example.template.home.data.servicemodels.Article

interface ArticleDataSource {

    suspend fun getArticle(userId: Int): List<Article>

}