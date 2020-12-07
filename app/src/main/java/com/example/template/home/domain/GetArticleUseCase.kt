package com.example.template.home.domain

import com.example.template.core.CoroutineDispatchers
import com.example.template.core.usecases.CoroutineUseCase
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.Article
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, List<Article>>(dispatchers.io) {
    override suspend fun execute(parameters: Int): List<Article> =
        homeRepository.getArticles(parameters)

}