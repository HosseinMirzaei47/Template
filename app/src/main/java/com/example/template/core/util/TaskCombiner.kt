package com.example.template.core.util

import com.example.template.BaseLiveTask
import com.example.template.core.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("UNCHECKED_CAST")
class TaskCombiner(
    vararg requests: LiveTask<*>,
    val context: CoroutineContext = EmptyCoroutineContext,
    val block: suspend CombinerBuilder.() -> Unit = {}
) : BaseLiveTask<Any>() {

    private var taskList = mutableListOf<LiveTask<*>>()
    private var blockRunner: CombineRunner? = null

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
                                applyResult(task)
                            }
                            else -> {
                                if ((task as BaseLiveTask<Any>).retryCounts > task.retryAttempts) {
                                    applyResult(task.result())
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
                            applyResult(Result.Loading)
                        }
                        is Result.Loading -> {
                        }
                        else -> {
                            applyResult(Result.Loading)
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
            applyResult(Result.Loading)
        }
    }

    private fun checkIsThereAnyUnSuccess() {
        val anyRequestLeft = taskList.any {
            it.result() is Result.Loading || it.result() is Result.Error
        }
        if (!anyRequestLeft) {
            applyResult(Result.Success(Any()))
        } else {
            if (this.value?.result() is Result.Loading) {
                val oneOfErrors = taskList.find { coroutineLiveTask ->
                    coroutineLiveTask.result() is Result.Error
                }
                oneOfErrors?.let {
                    applyResult(it)
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
        applyResult(Result.Loading)
        val listOfUnSuccesses = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.result() is Result.Error
        }
        listOfUnSuccesses.forEach { coroutineLiveTask ->
            coroutineLiveTask.retry()
        }
    }

    override fun execute() {
        taskList.forEach { it.execute() }

        val supervisorJob = SupervisorJob(context[Job])
        val scope = CoroutineScope(Dispatchers.IO + context + supervisorJob)
        blockRunner = CombineRunner(
            liveData = this,
            block = block,
            timeoutInMs = DEFAULT_TIMEOUT,
            scope = scope
        ) {
            blockRunner = null
        }

        blockRunner?.maybeRun()
    }

    override fun cancel() {
        val listOfUnLoading = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.result() is Result.Loading
        }

        listOfUnLoading.forEach { coroutineLiveTask ->
            coroutineLiveTask.cancel()
        }
    }

    private fun applyResult(result: Result<Any>?) {
        this.latestState = result
        postValue(this)
    }

    private fun applyResult(task: LiveTask<*>) {
        this.latestState = task.result() as Result<Any>?
        postValue(this)
    }
}