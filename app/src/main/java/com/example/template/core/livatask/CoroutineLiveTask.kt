package com.example.template.core.livatask

import androidx.lifecycle.LiveData
import com.example.template.connection.ConnectionManager
import com.example.template.core.ErrorEvent
import com.example.template.core.LiveTaskResult
import com.example.template.core.LoggerInterface
import com.example.template.core.util.NoConnectionException
import com.example.template.core.util.ServerException
import com.example.template.core.withError
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal const val DEFAULT_TIMEOUT = 100L

class CoroutineLiveTask<T>(
    private val context: CoroutineContext = EmptyCoroutineContext,
    private val timeoutInMs: Long = DEFAULT_TIMEOUT,
    private val connectionManager: ConnectionManager,
    private val logger: LoggerInterface,
    val block: suspend LiveTaskBuilder<T>.() -> Unit = {},
) : BaseLiveTask<T>() {

    private var blockRunner: TaskRunner<T>? = null
    var autoRetry = true
    private var emittedSource: Emitted? = null

    init {
        this.addSource(this) {
            val taskResult = it.result()
            if (taskResult is LiveTaskResult.Error) {
                taskResult.withError { exception ->
                    logger.notifyError(ErrorEvent((exception)))

                    val isNotAuthorized =
                        (exception is ServerException && exception.meta.statusCode == 401) ||
                                (exception is HttpException && exception.code() == 401)

                    when (exception) {
                        is NoConnectionException -> {
                            if (autoRetry) retryOnNetworkBack()
                        }
                        else -> {
                            if (retryCounts <= retryAttempts && !isNotAuthorized && exception !is CancellationException) {
                                retryCounts++
                                this.retry()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun run(): CoroutineLiveTask<T> {
        applyResult(LiveTaskResult.Loading)
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
        return this
    }

    private fun retryOnNetworkBack() {
        this.removeSource(connectionManager)
        this.addSource(connectionManager) { hasConnection ->
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
        this.removeSource(connectionManager)
        run()
    }

    override fun cancel() {
        blockRunner?.cancel()
    }


    internal suspend fun emitSource(source: LiveData<LiveTaskResult<T>>): DisposableHandle {
        clearSource()
        val newSource = addDisposableEmit(source)
        emittedSource = newSource
        return newSource
    }

    internal suspend fun clearSource() {
        emittedSource?.disposeNow()
        emittedSource = null
    }

    fun applyResult(result: LiveTaskResult<T>?) {
        this.latestState = result
        postValue(this)
    }

    fun applyResult(task: LiveTask<T>) {
        this.latestState = task.result() as LiveTaskResult<T>?
        postValue(this)
    }
}