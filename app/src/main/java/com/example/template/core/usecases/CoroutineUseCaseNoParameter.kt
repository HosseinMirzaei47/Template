package com.example.template.core.usecases

import com.example.template.core.Result
import com.example.template.core.Result.Error
import com.example.template.core.Result.Success
import com.example.template.core.util.readServerError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class CoroutineUseCaseNoParameter<R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                Success(execute())
            }
        } catch (e: CancellationException) {
            Error(e.message!!)
        } catch (e: HttpException) {
            val parsedError = try {
                readServerError(e)
            } catch (f: Exception) {
                e
            }
            Error(parsedError.message!!)
        } catch (e: Exception) {
            Error(e.message!!)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(): R
}
