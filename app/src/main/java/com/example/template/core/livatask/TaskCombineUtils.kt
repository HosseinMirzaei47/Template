package com.example.template.core.livatask

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import kotlinx.coroutines.*


class CombineRunner(
    @VisibleForTesting(otherwise = PRIVATE)
    val liveData: TaskCombiner,
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
        cancellationJob = scope.launch(Dispatchers.IO) {
            delay(timeoutInMs)
            runningJob?.cancel()
            runningJob = null
        }
    }
}

class CombinerBuilderImpl(
    private var target: TaskCombiner,
) : CombinerBuilder {

    override val latestValue: com.example.template.core.Result<Any>?
        get() = target.value?.result()

    override fun cancelable(bool: Boolean) {
        target.cancelable(bool)
    }

    override fun retryable(bool: Boolean) {
        target.retryable(bool)
    }
}

internal typealias CombinerBlock = suspend CombinerBuilder.() -> Unit