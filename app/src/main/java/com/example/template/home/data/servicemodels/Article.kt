package com.example.template.home.data.servicemodels

data class Article(
    val id: String,
    val title: String,
    val body: String,
    val author: String,
    val releaseDate: Int,
    val isFavorite: Boolean
)