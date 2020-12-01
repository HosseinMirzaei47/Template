package com.example.template.core.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.experimental.ExperimentalTypeInference


internal const val DEFAULT_TIMEOUT = 5000L

@OptIn(ExperimentalTypeInference::class)
fun <T> liveTask(
    context: CoroutineContext = EmptyCoroutineContext,
    @BuilderInference block: suspend LiveTaskScope<T>.() -> Unit
): CoroutineLiveTask<T> = CoroutineLiveTask(context, block = block)

class CoroutineLiveTask<T>(
    private val context: CoroutineContext = EmptyCoroutineContext,
    private val timeoutInMs: Long = DEFAULT_TIMEOUT,
    val block: suspend LiveTaskScope<T>.() -> Unit,
) : MutableLiveData<com.example.template.core.Result<T>>() {

    private var blockRunner: TaskRunner<T>? = null

    init {
        initBlockRunner()
    }

    private fun initBlockRunner() {
        val supervisorJob = SupervisorJob(context[Job])
        val scope = CoroutineScope(Dispatchers.IO + context + supervisorJob)
        blockRunner = TaskRunner(
            liveData = this,
            block = block,
            timeoutInMs = timeoutInMs,
            scope = scope
        ) {
            blockRunner = null
        }
    }

    override fun onActive() {
        super.onActive()
        blockRunner?.maybeRun()
    }

    override fun onInactive() {
        super.onInactive()
        blockRunner?.cancel()
    }

    fun retry() {
        initBlockRunner()
        blockRunner?.maybeRun()
        Log.d("retry", "retry: done ")
    }

    fun cancel() {
        blockRunner?.cancel()
    }
}

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
            val liveDataScope = LiveTaskScopeImpl(liveData, coroutineContext)
            block(liveDataScope)
            onDone()
        }
    }


    fun cancel() {
        if (cancellationJob != null) {
            error("Cancel call cannot happen without a maybeRun")
        }
        cancellationJob = scope.launch(Dispatchers.Main.immediate) {
            delay(timeoutInMs)
            if (!liveData.hasActiveObservers()) {
                runningJob?.cancel()
                runningJob = null
            }
        }
    }
}

internal class LiveTaskScopeImpl<T>(
    private var target: CoroutineLiveTask<T>,
    context: CoroutineContext
) : LiveTaskScope<T> {

    override val latestValue: com.example.template.core.Result<T>?
        get() = target.value

    private val coroutineContext = context + Dispatchers.Main.immediate

    override suspend fun emit(userUseCase: com.example.template.core.Result<T>) =
        withContext(coroutineContext) {
            target.value = userUseCase
        }

    override fun retrySexy() {}
}

internal typealias Block<T> = suspend LiveTaskScope<T>.() -> Unit

interface LiveTaskScope<T> {
    val latestValue: com.example.template.core.Result<T>?
    suspend fun emit(userUseCase: com.example.template.core.Result<T>)
    fun retrySexy()
}