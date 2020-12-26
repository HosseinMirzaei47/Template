package com.example.template.core.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.template.core.LiveTaskResult
import com.example.template.testutils.MainCoroutineRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LiveDataUseCaseTest {

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
        assertThat(result.value, CoreMatchers.instanceOf(LiveTaskResult.Error::class.java))
    }

    class ExceptionUseCase(dispatcher: CoroutineDispatcher) :
        LiveDataUseCase<Unit, Unit>(dispatcher) {
        override fun execute(parameters: Unit): LiveData<Unit> = liveData {
            throw Exception("Test Exception")
        }

    }
}