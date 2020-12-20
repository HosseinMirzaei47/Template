package com.example.template.core.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.template.core.IoDispatcher
import com.example.template.core.Result
import kotlinx.coroutines.CoroutineDispatcher

abstract class LiveDataUseCase<in P, R>(@IoDispatcher private val coroutineDispatcher: CoroutineDispatcher) {


    operator fun invoke(parameters: P): LiveData<Result<R>> {
        return try {
            execute(parameters).map {
                Result.Success(it)
            }
        } catch (e: Exception) {
            liveData {
                Result.Error(e)
            }
        }
    }

    protected abstract fun execute(parameters: P): LiveData<R>

}