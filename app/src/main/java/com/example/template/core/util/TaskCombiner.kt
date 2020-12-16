package com.example.template.core.util

import com.example.template.BaseLiveTask
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
    private val context: CoroutineContext = EmptyCoroutineContext,
    val block: suspend CombinerBuilder.() -> Unit
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
                is com.example.template.core.Result.Success -> {
                    checkIsThereAnyUnSuccess()
                }
                is com.example.template.core.Result.Error -> {
                    if (this.value?.result() !is com.example.template.core.Result.Error) {
                        val isNotAuthorized = (result.exception is ServerException &&
                                result.exception.meta.statusCode == 401) ||
                                (result.exception is HttpException &&
                                        result.exception.code() == 401)

                        when {
                            result.exception is NoConnectionException || isNotAuthorized -> {
                                applyResult(task)
                            }
                            else -> {
                                task as BaseLiveTask<Any>
                                if ((task).retryCounts > task.retryAttempts) {
                                    applyResult(task.result())
                                }
                            }
                        }
                    }
                }
                is com.example.template.core.Result.Loading -> {
                    when (this.value?.result()) {
                        is com.example.template.core.Result.Error -> {
                            setLoadingIfNoErrorLeft()
                        }
                        is com.example.template.core.Result.Success<*> -> {
                            applyResult(com.example.template.core.Result.Loading)
                        }
                        is com.example.template.core.Result.Loading -> {
                        }
                        else -> {
                            applyResult(com.example.template.core.Result.Loading)
                        }
                    }
                }
            }
        }
    }

    private fun setLoadingIfNoErrorLeft() {
        val hasError = taskList.any {
            it.result() is com.example.template.core.Result.Error
        }
        if (!hasError) {
            applyResult(com.example.template.core.Result.Loading)
        }
    }

    private fun checkIsThereAnyUnSuccess() {
        val anyRequestLeft = taskList.any {
            it.result() is com.example.template.core.Result.Loading || it.result() is com.example.template.core.Result.Error
        }
        if (!anyRequestLeft) {
            applyResult(com.example.template.core.Result.Success(Any()))
        } else {
            if (this.value?.result() is com.example.template.core.Result.Loading) {
                val oneOfErrors = taskList.find { coroutineLiveTask ->
                    coroutineLiveTask.result() is com.example.template.core.Result.Error
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
                is com.example.template.core.Result.Success -> {
                    successes++
                }
                is com.example.template.core.Result.Error -> {
                    failed++
                }
                com.example.template.core.Result.Loading -> {
                    loading++
                }
            }
        }
        println("mmb successful: $successes failed :$failed loading :$loading all requests: ${taskList.size}")
    }

    override fun retry() {
        applyResult(com.example.template.core.Result.Loading)
        val listOfUnSuccesses = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.result() is com.example.template.core.Result.Error
        }
        listOfUnSuccesses.forEach { coroutineLiveTask ->
            coroutineLiveTask.retry()
        }
    }

    override fun run(): LiveTask<Any> {

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

        taskList.forEach { it.run() }
        return this
    }

    override fun cancel() {
        val listOfUnLoading = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.result() is com.example.template.core.Result.Loading
        }

        listOfUnLoading.forEach { coroutineLiveTask ->
            coroutineLiveTask.cancel()
        }
    }

    private fun applyResult(result: com.example.template.core.Result<Any>?) {
        this.latestState = result
        postValue(this)
    }

    private fun applyResult(task: LiveTask<*>) {
        this.latestState = task.result() as com.example.template.core.Result<Any>?
        postValue(this)
    }
}