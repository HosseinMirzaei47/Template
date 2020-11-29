package com.example.template.home.domain

import com.example.template.core.CoroutineDispatchers
import com.example.template.core.usecases.CoroutineUseCase
import com.example.template.core.util.bodyOrThrow
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.UserRes
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, UserRes>(dispatchers.io) {
    override suspend fun execute(parameters: Int): UserRes =
        homeRepository.getUsers(parameters).bodyOrThrow()

}