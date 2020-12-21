package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.Result
import com.example.template.core.livatask.CoroutineLiveTask
import com.example.template.core.livatask.liveTask
import com.google.common.truth.Truth.assertThat
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
        delay(4_000L)
        emit(Result.Success("abc"))
    }

    private val liveData3 = liveTask {
        emit(Result.Loading)
    }


    @Before
    fun setup() {
    }

    @Test
    fun `test emit loading to live task`() {
        liveData1.run()
        assertThat(liveData1.asLiveData().value?.result() is Result.Loading).isTrue()
    }

    @Test
    fun `test cancel live task`() {
        liveData2.run()
        liveData2.cancel()
        //if runBlocking changes to runBlockingTest error happened why?!
        runBlocking {
            delay(150)
            assertThat((liveData2.result() as Result.Error).exception is CancellationException).isTrue()
        }
    }

    @Test
    fun `test apply result of live task`() {
        (liveData3 as CoroutineLiveTask).latestState =
            Result.Error(Exception("some error happened"))

        assertThat((liveData3.result() as Result.Error).exception.message).isEqualTo("some error happened")
    }

}