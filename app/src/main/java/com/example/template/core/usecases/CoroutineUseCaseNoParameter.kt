package com.example.template.core.usecases

import com.example.template.core.util.detectException
import com.example.template.core.util.readServerError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

abstract class CoroutineUseCaseNoParameter<R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(): com.example.template.core.Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                com.example.template.core.Result.Success(execute())
            }
        } catch (e: CancellationException) {
            com.example.template.core.Result.Error(e)
        } catch (e: HttpException) {
            val parsedError = try {
                readServerError(e)
            } catch (f: Exception) {
                e
            }
            com.example.template.core.Result.Error(parsedError)
        } catch (e: IOException) {
            com.example.template.core.Result.Error(e.detectException())
        } catch (e: Exception) {
            com.example.template.core.Result.Error(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(): R
}
