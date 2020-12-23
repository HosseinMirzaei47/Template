package com.example.template.core.usecases

import com.example.template.core.LiveTaskResult
import com.example.template.core.util.detectException
import com.example.template.core.util.readServerError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <R> runRequestThrowException(
    coroutineDispatcher: CoroutineDispatcher,
    action: suspend () -> R,
): LiveTaskResult<R> {
    return try {
        withContext(coroutineDispatcher) {
            LiveTaskResult.Success(action())
        }
    } catch (e: CancellationException) {
        LiveTaskResult.Error(e)
    } catch (e: HttpException) {
        val parsedError = try {
            readServerError(e)
        } catch (f: Exception) {
            e
        }
        LiveTaskResult.Error(parsedError)
    } catch (e: IOException) {
        LiveTaskResult.Error(e.detectException())
    } catch (e: Exception) {
        LiveTaskResult.Error(e)
    }
}