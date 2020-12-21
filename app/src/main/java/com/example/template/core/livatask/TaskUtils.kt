package com.example.template.core.livatask

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
fun <T> liveTask(
    context: CoroutineContext = EmptyCoroutineContext,
    @BuilderInference block: suspend LiveTaskBuilder<T>.() -> Unit = {}
): LiveTask<T> = CoroutineLiveTask(context, block = block)

@OptIn(ExperimentalTypeInference::class)
fun combinedTask(
    vararg requests: LiveTask<*>,
    @BuilderInference block: suspend CombinerBuilder.() -> Unit = {}
): LiveTask<Any> = TaskCombiner(*requests, block = block)