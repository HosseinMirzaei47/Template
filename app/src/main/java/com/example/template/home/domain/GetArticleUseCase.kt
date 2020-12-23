package com.example.template.home.domain

import com.example.template.core.IoDispatcher
import com.example.template.core.usecases.CoroutineUseCase
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.Article
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<Int, List<Article>>(coroutineDispatcher) {
    override suspend fun execute(parameters: Int): List<Article> =
        homeRepository.getArticles(parameters)
}