package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.template.home.domain.*
import com.part.livetaskcore.Resource
import com.part.livetaskcore.livatask.combinedTask
import com.part.livetaskcore.livatask.liveTask

class HomeViewModel @ViewModelInject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val articleUseCase: GetArticleUseCase,
    private val commentUseCase: GetCommentUseCase,
    flowUseCase: GetUserFlowUseCase,
    testUseCase: TestUseCase,
    liveDataUseCase: GetUserUseCaseLiveData,
) : ViewModel() {

    // حالت سینتکس قبلی با قابلیت emit های چندگانه
    val user1 = liveTask {
        emitSource(liveDataUseCase(2))
    }

    // حالت استفاده از flow
    val user2 = flowUseCase.asLiveTask(5)

    // حالت استفاده ی معمولی به صورت سینتکس جدید
    val user3 = testUseCase.asLiveTask(5)


    val user4 = liveTask {
        emit(getUserUseCase(4))
    }


    val combinedTasks = combinedTask(user1, user2, user3, user4)
    val query = 0


    val user = liveData {
        emit(Resource.Loading)
        emit(getUserUseCase(query))
    }


}