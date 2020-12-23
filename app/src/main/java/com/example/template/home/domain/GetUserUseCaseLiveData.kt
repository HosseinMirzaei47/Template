package com.example.template.home.domain

import androidx.lifecycle.LiveData
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.UserRes
import com.part.livetaskcore.IoDispatcher
import com.part.livetaskcore.usecases.LiveDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetUserUseCaseLiveData @Inject constructor(
    private val homeRepository: HomeRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : LiveDataUseCase<Int, UserRes>(coroutineDispatcher) {
    override fun execute(parameters: Int): LiveData<UserRes> {
        return homeRepository.getUsersLiveData(parameters)
    }
}