package com.example.template.home.domain

import com.example.template.core.CoroutineDispatchers
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.UserRes
import com.example.template.home.ui.viewmodel.LiveTaskUseCase
import javax.inject.Inject

class TestUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    dispatchers: CoroutineDispatchers
) : LiveTaskUseCase<Int, UserRes>(dispatchers.io) {

    override suspend fun execute(params: Int): UserRes {
        return homeRepository.getUsers(params)
    }

}