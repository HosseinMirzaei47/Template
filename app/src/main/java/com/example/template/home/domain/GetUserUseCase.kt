package com.example.template.home.domain

import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.UserRes
import com.part.livetaskcore.IoDispatcher
import com.part.livetaskcore.usecases.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CoroutineUseCase<Int, UserRes>(coroutineDispatcher) {
    override suspend fun execute(parameters: Int): UserRes {
        return homeRepository.getUsers(parameters)
    }
}