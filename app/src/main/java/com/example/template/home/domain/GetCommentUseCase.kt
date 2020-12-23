package com.example.template.home.domain

import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.Comment
import com.part.livetaskcore.IoDispatcher
import com.part.livetaskcore.usecases.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetCommentUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, List<Comment>>(coroutineDispatcher) {
    override suspend fun execute(parameters: String): List<Comment> =
        homeRepository.getComments(parameters)
}