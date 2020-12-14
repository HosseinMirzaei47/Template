package com.example.template.core.util

import com.example.template.BaseLiveTask
import com.example.template.core.Result
import retrofit2.HttpException

@Suppress("UNCHECKED_CAST")
class TaskCombiner(vararg requests: LiveTask<*>) : BaseLiveTask<Any>() {

    private var taskList = mutableListOf<LiveTask<*>>()

    init {
        requests.forEach { addTaskAsSource(it) }
    }

    private fun addTaskAsSource(task: LiveTask<*>) {
        taskList.add(task)
        val asLiveData = task.asLiveData()
        this.addSource(asLiveData) { liveTask ->
            printStats()

            when (val result = liveTask.result()) {
                is Result.Success -> {
                    checkIsThereAnyUnSuccess()
                }
                is Result.Error -> {
                    if (this.value?.result() !is Result.Error) {
                        val isNotAuthorized = (result.exception is ServerException &&
                                result.exception.meta.statusCode == 401) ||
                                (result.exception is HttpException &&
                                        result.exception.code() == 401)
                        when {
                            result.exception is NoConnectionException || isNotAuthorized -> {
                                this.latestState = task.result() as Result<Any>?
                                postValue(this)
                            }
                            else -> {
                                if ((task as BaseLiveTask<Any>).retryCounts > task.retryAttempts) {
                                    this.latestState = task.result()
                                    postValue(this)
                                }
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    when (this.value?.result()) {
                        is Result.Error -> {
                            setLoadingIfNoErrorLeft()
                        }
                        is Result.Success<*> -> {
                            this.latestState = Result.Loading
                            postValue(this)
                        }
                        is Result.Loading -> {
                        }
                        else -> {
                            this.latestState = Result.Loading
                            postValue(this)
                        }
                    }
                }
            }
        }
    }

    private fun setLoadingIfNoErrorLeft() {
        val hasError = taskList.any {
            it.result() is Result.Error
        }
        if (!hasError) {
            this.latestState = Result.Loading
            postValue(this)
        }
    }

    private fun checkIsThereAnyUnSuccess() {
        val anyRequestLeft = taskList.any {
            it.result() is Result.Loading || it.result() is Result.Error
        }
        if (!anyRequestLeft) {
            this.latestState = Result.Success(Any())
            postValue(this)
        } else {
            if (this.value?.result() is Result.Loading) {
                val oneOfErrors = taskList.find { coroutineLiveTask ->
                    coroutineLiveTask.result() is Result.Error
                }
                oneOfErrors?.let {
                    this.latestState = it.result() as Result<Any>?
                    postValue(this)
                }
            }
        }
    }

    private fun printStats() {
        var successes = 0
        var failed = 0
        var loading = 0
        taskList.forEach { coroutineLiveTask ->
            when (coroutineLiveTask.result()) {
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

    override fun retry() {
        this.latestState = Result.Loading
        postValue(this)
        val listOfUnSuccesses = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.result() is Result.Error
        }
        listOfUnSuccesses.forEach { coroutineLiveTask ->
            coroutineLiveTask.retry()
        }
    }

    override fun execute() = taskList.forEach { it.execute() }

    override fun cancel() {
        val listOfUnLoading = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.result() is Result.Loading
        }

        listOfUnLoading.forEach { coroutineLiveTask ->
            coroutineLiveTask.cancel()
            // (coroutineLiveTask as BaseLiveTask).postValue(value as Result<Nothing>?)
        }
    }
}