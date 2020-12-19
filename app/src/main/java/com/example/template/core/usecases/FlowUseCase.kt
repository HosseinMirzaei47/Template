package com.example.template.core.usecases

import com.example.template.core.IoDispatcher
import com.example.template.core.livatask.LiveTask
import com.example.template.core.livatask.liveTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(@IoDispatcher private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(parameters: P): Flow<R> =
        execute(parameters)
            .flowOn(coroutineDispatcher)


    private var parameter: P? = null

    private val task: LiveTask<R> by lazy {
        liveTask {
            emit(execute(params = parameter!!))
        }
    }

    abstract suspend fun execute(params: P): Flow<R>

    fun setParams(parameter: P) {
        this.parameter = parameter
    }

    fun asLiveTask(parameter: P): LiveTask<R> {
        setParams(parameter)
        return task
    }
}