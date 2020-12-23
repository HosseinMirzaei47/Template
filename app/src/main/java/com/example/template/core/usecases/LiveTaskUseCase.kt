package com.example.template.core.usecases

import com.example.template.core.IoDispatcher
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.LiveTask
import com.example.template.core.livatask.LiveTaskBuilder
import com.example.template.core.livatask.liveTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.experimental.ExperimentalTypeInference

abstract class LiveTaskUseCase<in P, R>(@IoDispatcher private val coroutineDispatcher: CoroutineDispatcher) {
    private var parameter: P? = null

    private var block: suspend LiveTaskBuilder<R>.() -> Unit = {}

    private val task: LiveTask<R> by lazy {
        liveTask {
            block.invoke(this)
            parameter?.let {
                val result =
                    runRequestThrowException(coroutineDispatcher) { execute(params = it) }
                emit(result)
            } ?: this.run {
                emit(LiveTaskResult.Error(KotlinNullPointerException()))
            }
        }
    }

    abstract suspend fun execute(params: P): R

    @OptIn(ExperimentalTypeInference::class)
    fun asLiveTask(
        parameter: P,
        @BuilderInference block: suspend LiveTaskBuilder<R>.() -> Unit = {},
    ): LiveTask<R> {
        this.block = block
        setParams(parameter)
        return task
    }

    fun setParams(parameter: P): LiveTaskUseCase<P, R> {
        this.parameter = parameter
        return this
    }
}

