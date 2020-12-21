package com.example.template.core.livatask

import com.example.template.core.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.CoroutineContext

internal class TaskRunner<T>(
    private val liveData: CoroutineLiveTask<T>,
    private val block: Block<T>,
    private val timeoutInMs: Long = DEFAULT_TIMEOUT,
    private val scope: CoroutineScope,
    private val onDone: () -> Unit
) {
    private var runningJob: Job? = null

    private var cancellationJob: Job? = null

    fun maybeRun() {
        cancellationJob?.cancel()
        cancellationJob = null
        if (runningJob != null) {
            return
        }
        runningJob = scope.launch {
            val liveDataScope = LiveTaskBuilderImpl(liveData, coroutineContext)
            block(liveDataScope)
            onDone()
        }
    }

    fun cancel() {
        cancellationJob = scope.launch(Dispatchers.Main.immediate) {
            delay(timeoutInMs)
            runningJob?.cancel()
            runningJob = null
            liveData.applyResult(Result.Error(CancellationException()))
        }
    }
}

internal class LiveTaskBuilderImpl<T>(
    private var target: CoroutineLiveTask<T>,
    context: CoroutineContext
) : LiveTaskBuilder<T> {

    override val latestValue: com.example.template.core.Result<T>?
        get() = target.value?.result()

    private val coroutineContext = context + Dispatchers.Main.immediate

    override suspend fun emit(result: com.example.template.core.Result<T>) =
        withContext(coroutineContext) {
            target.applyResult(result)
        }

    override suspend fun emit(result: Flow<T>) {
        result
            .onStart {
                target.applyResult(com.example.template.core.Result.Loading)
            }
            .catch { e ->
                target.applyResult(com.example.template.core.Result.Error(Exception(e)))
            }.collect {
                target.applyResult(Result.Success(it))
            }
    }

    override fun retry() {}

    override fun retryAttempts(attempts: Int) {
        target.retryAttempts = attempts
    }

    override fun autoRetry(bool: Boolean) {
        target.autoRetry = bool
    }

    override fun cancelable(bool: Boolean) {
        target.cancelable(bool)
    }

    override fun retryable(bool: Boolean) {
        target.retryable(bool)
    }
}

internal typealias Block<T> = suspend LiveTaskBuilder<T>.() -> Unit