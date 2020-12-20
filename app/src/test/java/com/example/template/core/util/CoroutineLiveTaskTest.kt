package com.example.template.core.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CancellationException


class CoroutineLiveTaskTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private val liveData1 = liveTask {

        emit(Result.Loading)
    }
    private val liveData2 = liveTask {
        delay(25000)
        emit(Result.Success("sad"))
    }

    private val liveData3 = liveTask {
        emit(Result.Success(5))
    }

    private val liveData4 = liveTask {
        emit(Result.Success(6))
    }
    private val liveData5 = liveTask {
        emit(Result.Success(785))
    }
    private val liveData6 = liveTask {
        emit(Result.Success(564))
    }
    private val liveData7 = liveTask {
        emit(Result.Success(9))
    }

    @Before
    fun setup() {
    }


    @Test
    fun `test emit loading to live task`() {
        liveData1.run()
        assertThat(liveData1.asLiveData().value?.result() is Result.Loading).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test cancel live task`() {
        liveData2.run()
        liveData2.cancel()

        runBlocking {

            delay(150)
            assertThat((liveData2.result() as Result.Error).exception is CancellationException).isTrue()

        }


    }

}