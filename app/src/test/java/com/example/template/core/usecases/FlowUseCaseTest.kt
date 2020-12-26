package com.example.template.core.usecases

import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.liveTask
import com.example.template.testutils.MainCoroutineRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FlowUseCaseTest {

    // Overrides Dispatchers.Main used in Coroutines
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private val testDispatcher = coroutineRule.testDispatcher

    lateinit var exceptionUseCase: ExceptionUseCase

    @Before
    fun setUp() {
        exceptionUseCase = ExceptionUseCase(testDispatcher)
    }

    @Test
    fun `exception emits Result#Error`() = runBlocking {

        val test = liveTask {
            emit(exceptionUseCase(Unit))
        }
        test.run()
        delay(200L)
        val result = test.result()

        assertThat(result, CoreMatchers.instanceOf(LiveTaskResult.Error::class.java))

    }

    class ExceptionUseCase(dispatcher: CoroutineDispatcher) : FlowUseCase<Unit, Unit>(dispatcher) {
        override suspend fun execute(params: Unit): Flow<Unit> = flow {
            throw Exception("Test exception")
        }
    }

}