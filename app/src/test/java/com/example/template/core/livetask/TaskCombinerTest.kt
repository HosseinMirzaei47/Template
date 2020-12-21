package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.Result
import com.example.template.core.livatask.CoroutineLiveTask
import com.example.template.core.livatask.TaskCombiner
import com.example.template.core.livatask.liveTask
import com.example.template.core.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskCombinerTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var taskCombiner: TaskCombiner

    private val liveData1 = liveTask {
        emit(Result.Success(6))
    }
    private val liveData2 = liveTask {
        emit(Result.Success(65))
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
        taskCombiner = TaskCombiner(
            liveData1,
            liveData2,
            liveData3,
            liveData4,
            liveData5,
            liveData6

        )
    }

    @Test
    fun `task combiner test`() {
        runBlocking {
            taskCombiner.run()
            delay(100)
            val result = taskCombiner.getOrAwaitValue().result()
            assertThat(result is Result.Success).isTrue()
        }
    }

    @Test
    fun `test run of live data`() {
        val result = liveData7.run()
        runBlocking {
            delay(100L)
        }
        assertThat(result.result() is Result.Success).isTrue()
    }

    @Test
    fun `test cancel method _ result error must be cancellation exception`() {
        taskCombiner.run()

        runBlocking {
            taskCombiner.cancel()
            delay(100L)
            assertThat(
                (taskCombiner.getOrAwaitValue()
                    .result() as Result.Error).exception is CancellationException
            ).isTrue()

        }
    }

    @Test
    fun `test apply result of live data`() {
        (liveData1 as CoroutineLiveTask).latestState =
            Result.Error(Exception("some error happened"))

        assertThat((liveData1.result() as Result.Error).exception.message).isEqualTo("some error happened")
    }


}