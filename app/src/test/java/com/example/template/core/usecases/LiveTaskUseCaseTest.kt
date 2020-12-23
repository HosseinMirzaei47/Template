package com.example.template.core.usecases

import com.example.template.core.Result
import com.example.template.testutils.MainCoroutineRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LiveTaskUseCaseTest {
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private val testDispatcher = coroutineRule.testDispatcher

    lateinit var exceptionUseCase: ExceptionUseCase

    @Before
    fun setUp() {
        exceptionUseCase = ExceptionUseCase(testDispatcher)


    }

    @ExperimentalCoroutinesApi
    @Test
    fun `exception emits Result#Error`() = runBlocking {

        val result = exceptionUseCase.asLiveTask(Unit).run()
        delay(100L)
        Assert.assertThat(result.result(), CoreMatchers.instanceOf(Result.Error::class.java))
    }

    class ExceptionUseCase(dispatcher: CoroutineDispatcher) :
        LiveTaskUseCase<Unit, Unit>(dispatcher) {
        override suspend fun execute(params: Unit) {
            throw Exception("Test exception")
        }

    }

}