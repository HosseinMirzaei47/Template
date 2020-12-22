package com.example.template.core.usecases

import com.example.template.core.IoDispatcher
import com.example.template.core.LiveTaskResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class CoroutineUseCase<in P, R>(@IoDispatcher private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters: P): LiveTaskResult<R> {
        return withContext(coroutineDispatcher) {
            runRequestThrowException(coroutineDispatcher) { execute(parameters) }
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}