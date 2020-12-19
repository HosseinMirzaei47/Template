package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.template.core.Result
import com.example.template.core.livatask.combinedTask
import com.example.template.core.livatask.liveTask
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
        //emit(useCase(1))
        emit(livedatatask.run().asLiveData())
    }

    val livedata= liveData {
        emit(Result.Loading)
        kotlinx.coroutines.delay(25000)
        emit(Result.Success("sad"))
    }

    val livedatatask= liveTask {
        emit(Result.Loading)
        kotlinx.coroutines.delay(25000)
        emit(Result.Success("A"))
    }

    // حالت استفاده از flow
    val user2 = flowUseCase.asLiveTask(5)

    // حالت استفاده ی معمولی به صورت سینتکس جدید
    val user3 = testUseCase.asLiveTask(5)


    val combinedTasks = combinedTask(user1, user2, user3) {

    }



}