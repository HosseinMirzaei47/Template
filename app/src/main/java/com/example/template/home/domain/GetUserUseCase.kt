package com.example.template.home.domain

import com.example.template.home.data.repository.HomeRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun getUsers(
        page:Int
    )=homeRepository.getUsers(page)
}