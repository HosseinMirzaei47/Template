package com.example.template.core.util

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
    @BuilderInference block: suspend LiveTaskBuilder<Any>.() -> Unit = {}
): LiveTask<Any> = TaskCombiner(*requests)