package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.CoroutineLiveTask
import com.example.template.core.livatask.TaskCombiner
import com.example.template.core.livatask.liveTask
import com.example.template.testutils.getOrAwaitValue
import kotlinx.coroutines.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskCombinerTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var taskCombiner: TaskCombiner

    private val liveData1 = liveTask {
        emit(LiveTaskResult.Success(6))
    }
    private val liveData2 = liveTask {
        emit(LiveTaskResult.Success(65))
    }

    private val liveData3 = liveTask {
        emit(LiveTaskResult.Success(5))
    }
    private val liveData4 = liveTask {
        emit(LiveTaskResult.Success(6))
    }
    private val liveData5 = liveTask {
        emit(LiveTaskResult.Success(785))
    }
    private val liveData6 = liveTask {
        emit(LiveTaskResult.Success(67))
    }
    private val liveData7 = liveTask {
        emit(LiveTaskResult.Success(9))
    }


    @Before
    fun setup() {
        taskCombiner = TaskCombiner(
            liveData1,
            liveData2,
            liveData3,
            liveData4,
            liveData5,
            liveData6
        ) {}
    }

    @Test
    fun `task combiner test`() {
        runBlocking {
            taskCombiner.run()
            delay(200)
            val result = taskCombiner.getOrAwaitValue().result()
            assertTrue(result is LiveTaskResult.Success)
        }
    }

    @Test
    fun `test run of live data`() {
        val result = liveData7.run()
        runBlocking {
            delay(100L)
        }
        assertTrue(result.result() is LiveTaskResult.Success)
    }

    @Test
    fun `test cancel method _ result error must be cancellation exception`() = runBlocking<Unit> {
        taskCombiner.run()

        launch(Dispatchers.Main.immediate) {
            withTimeout(200L) {
                if (isActive) cancel()
                taskCombiner.cancel()
                delay(150L)
                assertTrue(
                    (taskCombiner.result() as LiveTaskResult.Error).exception is CancellationException
                )

            }

        }

    }

    @Test
    fun `test apply result of live data`() {
        (liveData1 as CoroutineLiveTask).latestState =
            LiveTaskResult.Error(Exception("some error happened"))

        assertEquals(
            "some error happened",
            (liveData1.result() as LiveTaskResult.Error).exception.message
        )
    }


}