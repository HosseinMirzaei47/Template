package com.example.template.core.usecases

import com.example.template.core.Result
import com.example.template.core.Result.Error
import com.example.template.core.Result.Success
import com.example.template.core.util.detectException
import com.example.template.core.util.readServerError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

abstract class CoroutineUseCaseNoParameter<R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                Success(execute())
            }
        } catch (e: CancellationException) {
            Error(e)
        } catch (e: HttpException) {
            val parsedError = try {
                readServerError(e)
            } catch (f: Exception) {
                e
            }
            Error(parsedError)
        } catch (e: IOException) {
            Error(e.detectException())
        } catch (e: Exception) {
            Error(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(): R
}
