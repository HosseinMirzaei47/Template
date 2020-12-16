package com.example.template.home.domain

import com.example.template.core.CoroutineDispatchers
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.Comment
import com.example.template.home.ui.viewmodel.LiveTaskUseCase
import javax.inject.Inject

class TestUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    dispatchers: CoroutineDispatchers
) : LiveTaskUseCase<String, List<Comment>>(dispatchers.io) {

    override suspend fun execute(params: String): List<Comment> {
        return homeRepository.getComments(params)
    }

}