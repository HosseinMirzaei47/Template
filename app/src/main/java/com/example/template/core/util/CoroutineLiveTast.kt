package com.example.template.core.util

import androidx.lifecycle.MediatorLiveData
import com.example.template.core.MyApp.Companion.connectionLiveData
import com.example.template.core.Result
import com.example.template.core.withError
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.experimental.ExperimentalTypeInference

internal const val DEFAULT_TIMEOUT = 100L
internal const val DEFAULT_RETRY_ATTEMPTS = 1

@OptIn(ExperimentalTypeInference::class)
fun <T> liveTask(
    retryAttempts: Int = DEFAULT_RETRY_ATTEMPTS,
    context: CoroutineContext = EmptyCoroutineContext,
    @BuilderInference block: suspend LiveTaskScope<T>.() -> Unit
): CoroutineLiveTask<T> = CoroutineLiveTask(retryAttempts, context, block = block)

class CoroutineLiveTask<T>(
    var retryAttempts: Int = DEFAULT_RETRY_ATTEMPTS,
    private val context: CoroutineContext = EmptyCoroutineContext,
    private val timeoutInMs: Long = DEFAULT_TIMEOUT,
    val block: suspend LiveTaskScope<T>.() -> Unit,
) : MediatorLiveData<Result<T>>() {

    private var blockRunner: TaskRunner<T>? = null
    var retryCounts = 1

    init {
        initBlockRunner()

        this.addSource(this) {
            if (it is Result.Error) {
                it.withError { exception ->
                    when (exception) {
                        is NoConnectionException -> {
                            retryOnNetworkBack()
                        }
                        else -> {
                            if (retryCounts <= retryAttempts) {
                                retryCounts++
                                this.retry()
                            }
                        }
                    }
                }
            }
        }

        RequestsObserver.addLiveData(this as CoroutineLiveTask<Result<*>>)
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


    private fun retryOnNetworkBack() {
        this.addSource(connectionLiveData) { hasConnection ->
            if (hasConnection) {
                retry()
            }
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
        this.removeSource(connectionLiveData)
        initBlockRunner()
        blockRunner?.maybeRun()
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
            //  error("Cancel call cannot happen without a maybeRun")
        }
        cancellationJob = scope.launch(Main.immediate) {
            delay(timeoutInMs)
            //  if (!liveData.hasActiveObservers()) {
                runningJob?.cancel()
                runningJob = null
            //  }
        }
    }
}

internal class LiveTaskScopeImpl<T>(
    private var target: CoroutineLiveTask<T>,
    context: CoroutineContext
) : LiveTaskScope<T> {

    override val latestValue: Result<T>?
        get() = target.value

    private val coroutineContext = context + Main.immediate

    override suspend fun emit(userUseCase: Result<T>) = withContext(coroutineContext) {
        target.value = userUseCase
    }

    override fun retrySexy() {}
}

internal typealias Block<T> = suspend LiveTaskScope<T>.() -> Unit

interface LiveTaskScope<T> {
    val latestValue: Result<T>?
    suspend fun emit(userUseCase: Result<T>)
    fun retrySexy()
}