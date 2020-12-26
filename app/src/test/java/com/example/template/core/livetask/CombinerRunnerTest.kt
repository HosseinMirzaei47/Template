package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.CombineRunner
import com.example.template.core.livatask.CombinerBuilder
import com.example.template.core.livatask.TaskCombiner
import com.example.template.core.livatask.liveTask
import kotlinx.coroutines.GlobalScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CombinerRunnerTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var combinerRunner: CombineRunner
    private lateinit var taskCombiner: TaskCombiner

    private val liveTask1 = liveTask {
        emit(LiveTaskResult.Success("ok"))
    }
    val block1: suspend CombinerBuilder.() -> Unit = {}


    @Before
    fun setup() {
        taskCombiner = TaskCombiner(liveTask1) {}
        combinerRunner = CombineRunner(taskCombiner, block1, 100L, GlobalScope) {
            println("do something")
        }
    }

    @Test
    fun `test maybeRun method _ it must print do something `() {
        combinerRunner.maybeRun()
    }

}