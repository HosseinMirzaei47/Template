package com.example.template.core.usecases

import com.example.template.core.Result
import com.example.template.testutils.MainCoroutineRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoroutineUseCaseTest {


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

        val result = exceptionUseCase(Unit)
        assertThat(result, CoreMatchers.instanceOf(Result.Error::class.java))
    }

    class ExceptionUseCase(dispatcher: CoroutineDispatcher) :
        CoroutineUseCase<Unit, Unit>(dispatcher) {
        override suspend fun execute(parameters: Unit) {
            throw Exception("Test exception")
        }

    }

}