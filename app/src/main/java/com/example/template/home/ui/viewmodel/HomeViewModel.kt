package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.template.core.Result
import com.example.template.core.util.liveTask
import com.example.template.home.domain.GetUserUseCase

class HomeViewModel @ViewModelInject constructor(
    private val userUseCase: GetUserUseCase
) : ViewModel() {
    val users = liveTask {
        emit(Result.Loading)
        emit(userUseCase(1))
    }

    val users2 = liveTask {
        emit(Result.Loading)
        emit(userUseCase(2))
    }
}