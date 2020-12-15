package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.template.core.Result
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
        retryAttempts(10)
        emit(useCase(1))

    }

    val users2 = liveTask {
        delay(2000)
        emit(useCase(1))
    }

    private val liveData1 = liveTask<Int> {
        delay(2000)
        emit(Result.Success(6))
    }
    private val liveData2 = liveTask<Int> {
        delay(2000)
        emit(Result.Success(96))
    }

    private val liveData3 = liveTask<Int> {
        delay(2000)
        emit(Result.Success(5))
    }
    private val liveData4 = liveTask<Int> {
        delay(2000)
        emit(Result.Success(6))
    }
    private val liveData5 = liveTask<Int> {
        delay(2000)
        emit(Result.Success(785))
    }
    private val liveData6 = liveTask<Int> {
        delay(2000)
        emit(Result.Success(95))
    }
    private val liveData7 = liveTask<Int> {
        delay(2000)
        emit(Result.Success(9))
    }

    val combinedTasks =
        combinedTask(liveData1, liveData2, liveData3, liveData4, liveData5, liveData6, liveData7)
}