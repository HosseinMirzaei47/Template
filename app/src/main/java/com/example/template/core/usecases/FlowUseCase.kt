package com.example.template.core.usecases

import com.example.template.core.IoDispatcher
import com.example.template.core.livatask.LiveTask
import com.example.template.core.livatask.LiveTaskBuilder
import com.example.template.core.livatask.liveTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.experimental.ExperimentalTypeInference

abstract class FlowUseCase<in P, R>(@IoDispatcher private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(parameters: P): Flow<R> =
        execute(parameters)
            .flowOn(coroutineDispatcher)

    private var block: suspend LiveTaskBuilder<R>.() -> Unit = {}


    private var parameter: P? = null

    private val task: LiveTask<R> by lazy {
        liveTask {
            block.invoke(this)
            emit(execute(params = parameter!!))
        }
    }

    abstract suspend fun execute(params: P): Flow<R>

    private fun setParams(parameter: P): FlowUseCase<P, R> {
        this.parameter = parameter
        return this
    }

    @OptIn(ExperimentalTypeInference::class)
    fun asLiveTask(
        parameter: P,
        @BuilderInference block: suspend LiveTaskBuilder<R>.() -> Unit = {},
    ): LiveTask<R> {
        this.block = block
        setParams(parameter)
        return task
    }
}