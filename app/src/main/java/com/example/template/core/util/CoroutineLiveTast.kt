package com.example.template.core.util

import com.example.template.BaseLiveTask
import com.example.template.core.ErrorEvent
import com.example.template.core.Logger
import com.example.template.core.MyApp.Companion.connectionLiveData
import com.example.template.core.Result
import com.example.template.core.withError
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.experimental.ExperimentalTypeInference

internal const val DEFAULT_TIMEOUT = 100L

@OptIn(ExperimentalTypeInference::class)
fun <T> liveTask(
    context: CoroutineContext = EmptyCoroutineContext,
    @BuilderInference block: suspend LiveTaskBuilder<T>.() -> Unit = {}
): LiveTask<T> = CoroutineLiveTask(context, block = block)

@OptIn(ExperimentalTypeInference::class)
fun combinedTask(
    vararg requests: LiveTask<*>,
    @BuilderInference block: suspend LiveTaskBuilder<Any>.() -> Unit = {}
): LiveTask<Any> = TaskCombiner(*requests)

class CoroutineLiveTask<T>(
    private val context: CoroutineContext = EmptyCoroutineContext,
    private val timeoutInMs: Long = DEFAULT_TIMEOUT,
    val block: suspend LiveTaskBuilder<T>.() -> Unit = {},
) : BaseLiveTask<T>() {

    private var blockRunner: TaskRunner<T>? = null
    var autoRetry = false

    init {
        this.addSource(this) {
            if (it is Result.Error) {
                it.withError { exception ->
                    Logger.errorEvent.postValue(ErrorEvent((exception)))

                    val isNotAuthorized =
                        (exception is ServerException && exception.meta.statusCode == 401) ||
                                (exception is HttpException && exception.code() == 401)

                    when (exception) {
                        is NoConnectionException -> {
                            if (autoRetry) retryOnNetworkBack()
                        }
                        else -> {
                            if (retryCounts <= retryAttempts && !isNotAuthorized) {
                                retryCounts++
                                this.retry()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun execute() {
        this.postValue(Result.Loading)
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

        blockRunner?.maybeRun()
    }

    private fun retryOnNetworkBack() {
        this.removeSource(connectionLiveData)
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

    override fun retry() {
        this.removeSource(connectionLiveData)
        execute()
    }

    override fun cancel() {
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
            val liveDataScope = LiveTaskBuilderImpl(liveData, coroutineContext)
            block(liveDataScope)
            onDone()
        }
    }

    fun cancel() {
        cancellationJob = scope.launch(Main.immediate) {
            delay(timeoutInMs)
            runningJob?.cancel()
            runningJob = null
        }
    }
}

internal class LiveTaskBuilderImpl<T>(
    private var target: CoroutineLiveTask<T>,
    context: CoroutineContext
) : LiveTaskBuilder<T> {

    override val latestValue: Result<T>?
        get() = target.value

    private val coroutineContext = context + Main.immediate

    override suspend fun emit(userUseCase: Result<T>) = withContext(coroutineContext) {
        target.value = userUseCase
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

