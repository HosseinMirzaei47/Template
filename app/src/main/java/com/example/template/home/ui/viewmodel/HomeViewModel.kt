package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.template.core.util.combinedTask
import com.example.template.core.util.liveTask
import com.example.template.home.domain.*

class HomeViewModel @ViewModelInject constructor(
    private val useCase: GetUserUseCase,
    private val articleUseCase: GetArticleUseCase,
    private val commentUseCase: GetCommentUseCase,
    flowUseCase: GetUserFlowUseCase,
    testUseCase: TestUseCase
) : ViewModel() {


    // حالت سینتکس قبلی با قابلیت emit های چندگانه
    val user1 = liveTask {
        emit(useCase(1))
    }

    // حالت استفاده از flow
    val user2 = flowUseCase.asLiveTask(5)

    // حالت استفاده ی معمولی به صورت سینتکس جدید
    val user3 = testUseCase.asLiveTask("5")


    val combinedTasks = combinedTask(user1, user2, user3)


}