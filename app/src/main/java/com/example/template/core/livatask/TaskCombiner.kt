package com.example.template.core.livatask

import com.example.template.core.LiveTaskResult
import com.example.template.core.util.NoConnectionException
import com.example.template.core.util.ServerException
import kotlinx.coroutines.*
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
                is LiveTaskResult.Success -> {
                    checkIsThereAnyUnSuccess()
                }
                is LiveTaskResult.Error -> {
                    if (this.value?.result() !is LiveTaskResult.Error && result.exception !is CancellationException) {
                        val isNotAuthorized = (result.exception is ServerException &&
                                result.exception.meta.statusCode == 401) ||
                                (result.exception is HttpException &&
                                        result.exception.code() == 401)

                        when {
                            result.exception is NoConnectionException || isNotAuthorized || result.exception is CancellationException -> {
                                applyResult(task)
                            }
                            else -> {
                                task as BaseLiveTask<Any>
                                if ((task).retryCounts > task.retryAttempts) {
                                    applyResult(task.result())
                                }
                            }
                        }
                    } else {
                        checkIsThereAnyUnSuccess()
                    }
                }
                is LiveTaskResult.Loading -> {
                    when (this.value?.result()) {
                        is LiveTaskResult.Error -> {
                            setLoadingIfNoErrorLeft()
                        }
                        is LiveTaskResult.Success<*> -> {
                            applyResult(LiveTaskResult.Loading)
                        }
                        is LiveTaskResult.Loading -> {
                        }
                        else -> {
                            applyResult(LiveTaskResult.Loading)
                        }
                    }
                }
            }
        }
    }

    private fun setLoadingIfNoErrorLeft() {
        val hasError = taskList.any {
            it.result() is LiveTaskResult.Error && (it.result() as LiveTaskResult.Error).exception !is CancellationException
        }
        if (!hasError) {
            applyResult(LiveTaskResult.Loading)
        }
    }

    private fun checkIsThereAnyUnSuccess() {
        val anyRequestLeft = taskList.any {
            it.result() is LiveTaskResult.Loading || (it.result() is LiveTaskResult.Error && (it.result() as LiveTaskResult.Error).exception !is CancellationException)
        }
        if (!anyRequestLeft) {
            applyResult(LiveTaskResult.Success(Any()))
        } else {
            if (this.value?.result() is LiveTaskResult.Loading) {
                val oneOfErrors = taskList.find { coroutineLiveTask ->
                    coroutineLiveTask.result() is LiveTaskResult.Error && (coroutineLiveTask.result() as LiveTaskResult.Error).exception !is CancellationException
                }
                oneOfErrors?.let {
                    applyResult(it)
                }
            }
        }
    }

    override fun retry() {
        applyResult(LiveTaskResult.Loading)
        val listOfUnSuccesses = taskList.filter { coroutineLiveTask ->
            coroutineLiveTask.result() is LiveTaskResult.Error && (coroutineLiveTask.result() as LiveTaskResult.Error).exception !is CancellationException
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
            coroutineLiveTask.result() is LiveTaskResult.Loading
        }

        listOfUnLoading.forEach { coroutineLiveTask ->
            coroutineLiveTask.cancel()
        }
    }

    private fun applyResult(result: LiveTaskResult<Any>?) {
        this.latestState = result
        postValue(this)
    }

    private fun applyResult(task: LiveTask<*>) {
        this.latestState = task.result() as LiveTaskResult<Any>?
        postValue(this)
    }

    private fun printStats() {
        var successes = 0
        var failed = 0
        var loading = 0
        taskList.forEach { coroutineLiveTask ->
            when (coroutineLiveTask.result()) {
                is LiveTaskResult.Success -> {
                    successes++
                }
                is LiveTaskResult.Error -> {
                    failed++
                }
                LiveTaskResult.Loading -> {
                    loading++
                }
            }
        }
        println("mmb successful: $successes failed :$failed loading :$loading all requests: ${taskList.size}")
    }
}