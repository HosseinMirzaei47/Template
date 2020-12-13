package com.example.template.core.util

import com.example.template.BaseLiveTask
import com.example.template.core.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Suppress("UNCHECKED_CAST")
class TaskCombiner(vararg requests: LiveTask<*>) : BaseLiveTask<Any>() {

    private var taskList = mutableListOf<LiveTask<*>>()
    private var job: Job = Job()

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
                    job.cancel()
                    checkIsThereAnyUnSuccess()
                }
                is Result.Error -> {
                    if (this.value !is Result.Error) {
                        job.cancel()
                        val isNotAuthorized = (result.exception is ServerException &&
                                result.exception.meta.statusCode == 401) ||
                                (result.exception is HttpException &&
                                        result.exception.code() == 401)
                        when {
                            result.exception is NoConnectionException || isNotAuthorized -> {
                                this.postValue(asLiveData.value as Result<Any>?)
                            }
                            else -> {
                                if ((task as BaseLiveTask<Any>).retryCounts > task.retryAttempts) {
                                    this.postValue(task.value)
                                }
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    job = GlobalScope.launch {
                        delay(10000)
                        this@TaskCombiner.postValue(Result.Error(Exception()))
                    }
                    when (this.value) {
                        is Result.Error -> {
                            setLoadingIfNoErrorLeft()
                        }
                        is Result.Success<*> -> {
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

    private fun setLoadingIfNoErrorLeft() {
        val hasError = taskList.any {
            it.result() is Result.Error
        }
        if (!hasError) {
            this.postValue(Result.Loading)
        }
    }

    private fun checkIsThereAnyUnSuccess() {
        val anyRequestLeft = taskList.any {
            it.result() is Result.Loading || it.result() is Result.Error
        }
        if (!anyRequestLeft) {
            this.postValue(Result.Success(Any()))
        } else {
            if (this.value is Result.Loading) {
                val oneOfErrors = taskList.find { coroutineLiveTask ->
                    coroutineLiveTask.result() is Result.Error
                }
                oneOfErrors?.let {
                    this.postValue(it.result() as Result<Any>?)
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
        this.postValue(Result.Loading)
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
            (coroutineLiveTask as BaseLiveTask).postValue(value as Result<Nothing>?)
        }
    }
}