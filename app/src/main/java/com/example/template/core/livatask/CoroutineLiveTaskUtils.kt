package com.example.template.core.livatask

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.template.core.DefaultErrorMapper
import com.example.template.core.ErrorMapper
import com.example.template.core.LiveTaskResult
import com.example.template.core.bindingadapter.ProgressType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
            liveData.applyResult(LiveTaskResult.Error(CancellationException()))
        }
    }
}

internal class LiveTaskBuilderImpl<T>(
    private var target: CoroutineLiveTask<T>,
    context: CoroutineContext,
) : LiveTaskBuilder<T> {
    private var errorMapper: ErrorMapper = DefaultErrorMapper.getMapper()

    override val latestValue: LiveTaskResult<T>?
        get() = target.value?.result()

    private val coroutineContext = context + Dispatchers.Main.immediate

    override suspend fun emit(result: LiveTaskResult<T>) {
        target.clearSource()
        withContext(coroutineContext) {
            target.applyResult(result)
        }
    }

    override suspend fun emit(result: Flow<T>) {
        target.clearSource()
        result
            .onStart {
                target.applyResult(LiveTaskResult.Loading)
            }
            .catch { e ->
                target.applyResult(getError(e))
            }
            .collect {
                target.applyResult(LiveTaskResult.Success(it))
            }
    }

    private fun getError(e: Throwable) = LiveTaskResult.Error(errorMapper.map((Exception(e))))

    override suspend fun emitSource(source: LiveData<LiveTaskResult<T>>): DisposableHandle =
        withContext(coroutineContext) {
            return@withContext target.emitSource(source)
        }

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

    override fun loadingViewType(type: ProgressType) {
        target.loadingViewType = type
    }

    override fun errorMapper(errorMapper: ErrorMapper) {
        this.errorMapper = errorMapper
    }
}

internal class Emitted(
    private val source: LiveData<*>,
    private val mediator: MediatorLiveData<*>
) : DisposableHandle {
    // @MainThread
    private var disposed = false

    /**
     * Unlike [dispose] which cannot be sync because it not a coroutine (and we do not want to
     * lock), this version is a suspend function and does not return until source is removed.
     */
    suspend fun disposeNow() = withContext(Dispatchers.Main.immediate) {
        removeSource()
    }

    override fun dispose() {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            removeSource()
        }
    }

    @MainThread
    private fun removeSource() {
        if (!disposed) {
            mediator.removeSource(source)
            disposed = true
        }
    }
}

internal typealias Block<T> = suspend LiveTaskBuilder<T>.() -> Unit