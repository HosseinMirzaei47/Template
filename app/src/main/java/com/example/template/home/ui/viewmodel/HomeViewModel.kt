package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.template.core.bindingadapter.ProgressType
import com.example.template.core.livatask.combinedTask
import com.example.template.core.livatask.liveTask
import com.example.template.home.domain.*

class HomeViewModel @ViewModelInject constructor(
    private val useCase: GetUserUseCase,
    private val articleUseCase: GetArticleUseCase,
    private val commentUseCase: GetCommentUseCase,
    flowUseCase: GetUserFlowUseCase,
    testUseCase: TestUseCase,
    liveDataUseCase: GetUserUseCaseLiveData,
) : ViewModel() {

    val users = liveTask {
        loadingViewType(ProgressType.SANDY_CLOCK)
        emitSource(liveDataUseCase(1))
    }

    private val user2 = flowUseCase.asLiveTask(5)
    private val user3 = testUseCase.asLiveTask(5)
    private val user4 = liveTask { emit(useCase(4)) }

    val combinedTasks = combinedTask(users, user2, user3, user4)
}