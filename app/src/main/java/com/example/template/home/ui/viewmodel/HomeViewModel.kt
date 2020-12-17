package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.template.core.util.combinedTask
import com.example.template.core.util.liveTask
import com.example.template.home.domain.GetArticleUseCase
import com.example.template.home.domain.GetCommentUseCase
import com.example.template.home.domain.GetUserUseCase
import kotlinx.coroutines.delay

class HomeViewModel @ViewModelInject constructor(
    private val useCase: GetUserUseCase,
    private val articleUseCase: GetArticleUseCase,
    private val commentUseCase: GetCommentUseCase
) : ViewModel() {

    val users1 = liveTask {
        delay(2500)
        emit(useCase(1))
    }

    val users2 = liveTask {
        delay(5000)
        emit(useCase(1))
    }

    val combinedTasks = combinedTask(users1, users2)

}