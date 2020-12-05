package com.example.template.core.usecases

import com.example.template.core.Result
import com.example.template.core.util.detectException
import com.example.template.core.util.readServerError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

abstract class CoroutineUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters: P): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                Result.Success(execute(parameters))
            }
        } catch (e: CancellationException) {
            Result.Error(e)
        } catch (e: HttpException) {
            val parsedError = try {
                readServerError(e)
            } catch (f: Exception) {
                e
            }
            Result.Error(parsedError)
        } catch (e: IOException) {
            Result.Error(e.detectException())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}
