package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.Result
import com.example.template.core.livatask.CoroutineLiveTask
import com.example.template.core.livatask.LiveTaskBuilder
import com.example.template.core.livatask.TaskRunner
import com.example.template.core.livatask.liveTask
import com.example.template.core.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskRunnerTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var taskRunner1: TaskRunner<*>
    lateinit var taskRunner2: TaskRunner<*>

    val block1: suspend LiveTaskBuilder<*>.() -> Unit = {}
    val block2: suspend LiveTaskBuilder<*>.() -> Unit = {}

    private val liveTask1 = liveTask {
        emit(Result.Error(Exception("some error happened")))
    }
    private val liveTask2 = liveTask {
        emit(Result.Success(8))
    }

    @Before
    fun setup() {


        runBlocking {
            taskRunner1 = TaskRunner(
                liveTask1 as CoroutineLiveTask, block1, 100L, GlobalScope

            ) {
                println("maybeRun method works fine!")
            }

            taskRunner2 = TaskRunner(
                liveTask2 as CoroutineLiveTask, block2, 100L, GlobalScope

            ) {
            }


        }

    }

    @Test
    fun `test maybe run method of task runner_ it must print "maybeRun method works fine!"`() {
        taskRunner1.maybeRun()
    }

    @Test
    fun `test cancel method of runner`() {

        liveTask2.run()
        runBlocking {
            taskRunner2.cancel()
            delay(200L)
        }
        assertThat(
            (liveTask2.asLiveData().getOrAwaitValue()
                .result() as Result.Error).exception is CancellationException
        ).isTrue()
    }

}

