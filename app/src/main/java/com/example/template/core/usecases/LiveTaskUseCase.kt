package com.example.template.core.usecases

import com.example.template.core.IoDispatcher
import com.example.template.core.Result
import com.example.template.core.livatask.LiveTask
import com.example.template.core.livatask.liveTask
import kotlinx.coroutines.CoroutineDispatcher

abstract class LiveTaskUseCase<in P, R>(
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    private var parameter: P? = null

    private val task: LiveTask<R> by lazy {
        liveTask {
            parameter?.let {
                val result =
                    runRequestThrowException(coroutineDispatcher) { execute(params = it) }
                emit(result)
            } ?: kotlin.run {
                emit(Result.Error(KotlinNullPointerException()))
            }
        }
    }

    abstract suspend fun execute(params: P): R
    fun setParams(parameter: P): LiveTaskUseCase<P, R> {
        this.parameter = parameter
        return this
    }

    fun asLiveTask(parameter: P): LiveTask<R> {
        setParams(parameter)
        return task
    }
}
