package com.example.template.core.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskCombinerTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var taskCombiner: TaskCombiner

    private val liveData1 = liveTask<Int> {
        emit(Result.Success(6))
    }
    private val liveData2 = liveTask<Int> {
        emit(Result.Success(96))
    }

    private val liveData3 = liveTask<Int> {
        emit(Result.Success(5))
    }
    private val liveData4 = liveTask<Int> {
        emit(Result.Success(6))
    }
    private val liveData5 = liveTask<Int> {
        emit(Result.Success(785))
    }
    private val liveData6 = liveTask<Int> {
        emit(Result.Success(564))
    }
    private val liveData7 = liveTask<Int> {
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
            liveData6,
            liveData7

        )
    }

    @Test
    fun `task combiner test`() {
        runBlocking {
            taskCombiner.execute()
            delay(100)
            val orAwaitValue = taskCombiner.getOrAwaitValue()
            val result = orAwaitValue.result()
            assertThat(result is Result.Success).isTrue()
        }
    }
}