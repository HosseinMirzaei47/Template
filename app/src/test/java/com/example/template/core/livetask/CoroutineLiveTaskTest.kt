package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.CoroutineLiveTask
import com.example.template.core.livatask.liveTask
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CoroutineLiveTaskTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val liveData1 = liveTask {

        emit(LiveTaskResult.Loading)
    }
    private val liveData2 = liveTask {
        emit(LiveTaskResult.Success("abc"))
    }

    private val liveData3 = liveTask {
        emit(LiveTaskResult.Loading)
    }


    @Before
    fun setup() {
    }

    @Test
    fun `test emit loading to live task`() {
        liveData1.run()
        assertTrue(liveData1.asLiveData().value?.result() is LiveTaskResult.Loading)
    }

    /* @Test
     fun `test cancel live task`() {
         liveData2.run()

         //if runBlocking changes to runBlockingTest error happened why?!
         runBlocking {
             liveData2.cancel()
             delay(1500L)
             assertTrue((liveData2.result() as LiveTaskResult.Error).exception is CancellationException)
         }
     }
 */
    @Test
    fun `test apply result of live task`() {
        (liveData3 as CoroutineLiveTask).latestState =
            LiveTaskResult.Error(Exception("some error happened"))

        assertEquals(
            "some error happened",
            (liveData3.result() as LiveTaskResult.Error).exception.message
        )
    }

}