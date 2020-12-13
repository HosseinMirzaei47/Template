package com.example.template.home.domain

import com.example.template.core.CoroutineDispatchers
import com.example.template.core.usecases.CoroutineUseCase
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.Comment
import javax.inject.Inject

class GetCommentUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, List<Comment>>(dispatchers.io) {
    override suspend fun execute(parameters: String): List<Comment> =
        homeRepository.getComments(parameters)

}