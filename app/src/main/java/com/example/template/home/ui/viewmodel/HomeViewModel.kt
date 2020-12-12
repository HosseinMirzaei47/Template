package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.template.core.util.TaskCombiner
import com.example.template.core.util.liveTask
import com.example.template.home.domain.GetArticleUseCase
import com.example.template.home.domain.GetCommentUseCase
import com.example.template.home.domain.GetUserUseCase

class HomeViewModel @ViewModelInject constructor(
    private val useCase: GetUserUseCase,
    private val articleUseCase: GetArticleUseCase,
    private val commentUseCase: GetCommentUseCase
) : ViewModel() {


    val users1 = liveTask {
        emit(useCase(1))
    }

    val users2 = liveTask {
        retryAttempts(10)
        autoRetry(true)
        emit(useCase(1))
    }

    val combinedTasks = TaskCombiner(users1, users2)
}