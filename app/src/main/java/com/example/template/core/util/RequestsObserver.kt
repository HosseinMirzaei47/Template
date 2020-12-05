package com.example.template.core.util

import androidx.lifecycle.MediatorLiveData
import com.example.template.core.Result

object RequestsObserver : MediatorLiveData<Result<*>>() {

    var taskList = mutableListOf<CoroutineLiveTask<Result<*>>>()

    fun addLiveData(task: CoroutineLiveTask<Result<*>>) {
        taskList.add(task)

        this.addSource(task) { result ->
            //printStats()
            when (result) {
                is Result.Success -> {
                    this.removeSource(task)
                    taskList.remove(task)
                    checkIsThereAnyUnSuccess()
                }
                is Result.Error -> {
                    if (this.value !is Result.Error && (task.retryCounts > task.retryAttempts)) {
                        this.postValue(task.value)
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

    private fun setLoadingIfNoErrorLeft() {
        val hasError = taskList.any { it ->
            it.value is Result.Error
        }
        if (!hasError) {
            this.postValue(Result.Loading)
        }
    }

    private fun printStats() {
        var sucessess = 0
        var failed = 0
        var loading = 0
        taskList.forEachIndexed { _, coroutineLiveTask ->
            when (coroutineLiveTask.value) {
                is Result.Success -> {
                    sucessess++
                }
                is Result.Error -> {
                    failed++
                }
                Result.Loading -> {
                    loading++
                }
            }
        }
        println("mmb successha=$sucessess failedha=$failed loadingha=$loading allcount=${taskList.size}")
    }

    fun retry() {
        this.postValue(Result.Loading)
        val listOfUnSuccesses = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.value is Result.Error
        }
        listOfUnSuccesses.forEachIndexed { _, coroutineLiveTask ->
            coroutineLiveTask.retry()
        }
    }

    private fun checkIsThereAnyUnSuccess() {
        val anyRequestLeft = taskList.any { it ->
            it.value is Result.Loading || it.value is Result.Error
        }
        if (!anyRequestLeft) {
            this.postValue(Result.Success(null))
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
}