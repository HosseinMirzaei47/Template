package com.example.template.core.util

import com.example.template.core.Result
import kotlinx.coroutines.*

internal class CombineRunner(
    private val liveData: TaskCombiner,
    private val block: CombinerBlock,
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
            val liveDataScope = CombinerBuilderImpl(liveData)
            block(liveDataScope)
            onDone()
        }
    }

    fun cancel() {
        cancellationJob = scope.launch(Dispatchers.Main.immediate) {
            delay(timeoutInMs)
            runningJob?.cancel()
            runningJob = null
        }
    }
}

internal class CombinerBuilderImpl(
    private var target: TaskCombiner,
) : CombinerBuilder {

    override val latestValue: Result<Any>?
        get() = target.value?.result()

    override fun retry() {}

    override fun cancelable(bool: Boolean) {
        target.cancelable(bool)
    }

    override fun retryable(bool: Boolean) {
        target.retryable(bool)
    }
}

internal typealias CombinerBlock = suspend CombinerBuilder.() -> Unit