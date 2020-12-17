package com.example.template.home.domain

import com.example.template.core.CoroutineDispatchers
import com.example.template.core.usecases.FlowUseCase
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.UserRes
import javax.inject.Inject


class GetUserFlowUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val dispatchers: CoroutineDispatchers
) : FlowUseCase<Int, UserRes>(dispatchers.io) {
    override suspend fun execute(params: Int) = homeRepository.getUsersFlow(params)
}