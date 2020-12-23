package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.Result
import com.example.template.core.livatask.CoroutineLiveTask
import com.example.template.core.livatask.liveTask
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        assertTrue(liveData1.asLiveData().value?.result() is Result.Loading)
    }

    @Test
    fun `test cancel live task`() {
        liveData2.run()
        liveData2.cancel()
        //if runBlocking changes to runBlockingTest error happened why?!
        runBlocking {
            delay(150)
            assertTrue((liveData2.result() as Result.Error).exception is CancellationException)
        }
    }

    @Test
    fun `test apply result of live task`() {
        (liveData3 as CoroutineLiveTask).latestState =
            Result.Error(Exception("some error happened"))

        assertEquals("some error happened", (liveData3.result() as Result.Error).exception.message)
    }

}