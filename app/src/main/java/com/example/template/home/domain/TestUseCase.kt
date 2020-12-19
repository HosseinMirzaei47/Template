package com.example.template.home.domain

import com.example.template.core.IoDispatcher
import com.example.template.core.usecases.LiveTaskUseCase
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.UserRes
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TestUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : LiveTaskUseCase<Int, UserRes>(coroutineDispatcher) {

    override suspend fun execute(params: Int): UserRes {
        return homeRepository.getUsers(params)
    }

}