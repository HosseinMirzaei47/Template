package com.example.template.core.util

import com.example.template.core.Result
import retrofit2.HttpException

class RequestsObserver private constructor() : CoroutineLiveTask<Any>() {

    private var taskList = mutableListOf<CoroutineLiveTask<Result<Any>>>()

    fun addLiveData(task: CoroutineLiveTask<Result<Any>>) {
        taskList.add(task)

        this.addSource(task) { result ->
            printStats()
            when (result) {
                is Result.Success -> {
                    this.removeSource(task)
                    taskList.remove(task)
                    checkIsThereAnyUnSuccess()
                }
                is Result.Error -> {
                    if (this.value !is Result.Error) {
                        val b = (result.exception is ServerException &&
                                result.exception.meta.statusCode == 401) ||
                                (result.exception is HttpException &&
                                        result.exception.code() == 401)
                        when {
                            result.exception is NoConnectionException -> {
                                this.postValue(task.value)
                            }
                            b -> {
                                this.postValue(task.value)
                                cancelAll(task.value)
                            }
                            else -> {
                                if (task.retryCounts > task.retryAttempts) {
                                    this.postValue(task.value)
                                }
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    when (this.value) {
                        is Result.Error -> {
                            setLoadingIfNoErrorLeft()
                        }
                        is Result.Success -> {
                            this.postValue(Result.Loading)
                        }
                        is Result.Loading -> {
                        }
                        else -> {
                            this.postValue(Result.Loading)
                        }
                    }
                }
            }
        }
    }

    fun retryAll() {
        this.postValue(Result.Loading)
        val listOfUnSuccesses = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.value is Result.Error
        }
        listOfUnSuccesses.forEachIndexed { _, coroutineLiveTask ->
            coroutineLiveTask.retry()
        }
    }

    private fun cancelAll(value: Result<Result<Any>>?) {
        val listOfUnLoading = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.value is Result.Loading
        }

        listOfUnLoading.forEachIndexed { _, coroutineLiveTask ->
            coroutineLiveTask.cancel()
            coroutineLiveTask.postValue(value)
        }
    }

    private fun setLoadingIfNoErrorLeft() {
        val hasError = taskList.any {
            it.value is Result.Error
        }
        if (!hasError) {
            this.postValue(Result.Loading)
        }
    }

    private fun checkIsThereAnyUnSuccess() {
        val anyRequestLeft = taskList.any {
            it.value is Result.Loading || it.value is Result.Error
        }
        if (!anyRequestLeft) {
            this.postValue(Result.Success(Any()))
        } else {
            if (this.value is Result.Loading) {
                val oneOfErrors = taskList.find { coroutineLiveTask ->
                    coroutineLiveTask.value is Result.Error
                }
                oneOfErrors?.let {
                    this.postValue(it.value)
                }
            }
        }
    }

    private fun printStats() {
        var successes = 0
        var failed = 0
        var loading = 0
        taskList.forEachIndexed { _, coroutineLiveTask ->
            when (coroutineLiveTask.value) {
                is Result.Success -> {
                    successes++
                }
                is Result.Error -> {
                    failed++
                }
                Result.Loading -> {
                    loading++
                }
            }
        }
        println("jalil successful: $successes failed :$failed loading :$loading all requests: ${taskList.size}")
    }

    companion object {
        private var singleInstance: RequestsObserver = RequestsObserver()
        fun getInstance() = singleInstance
    }
}