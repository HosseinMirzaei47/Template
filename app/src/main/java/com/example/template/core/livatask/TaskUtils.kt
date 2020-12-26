package com.example.template.core.livatask

import com.example.template.connection.ConnectionManager
import com.example.template.core.ErrorObserver
import com.example.template.core.ErrorObserverInterface
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
fun <T> liveTask(
    context: CoroutineContext = EmptyCoroutineContext,
    connectionManager: ConnectionManager = ConnectionManager.connectionManager,
    errorObserver: ErrorObserverInterface = ErrorObserver,
    @BuilderInference block: suspend LiveTaskBuilder<T>.() -> Unit = {}
): LiveTask<T> = CoroutineLiveTask(
    context,
    block = block,
    connectionManager = connectionManager,
    errorObserver = errorObserver
)

@OptIn(ExperimentalTypeInference::class)
fun combinedTask(
    vararg requests: LiveTask<*>,
    @BuilderInference block: suspend CombinerBuilder.() -> Unit = {}
): LiveTask<Any> = TaskCombiner(*requests, block = block)