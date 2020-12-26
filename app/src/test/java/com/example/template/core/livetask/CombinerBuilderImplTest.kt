package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.CombinerBuilderImpl
import com.example.template.core.livatask.TaskCombiner
import com.example.template.core.livatask.liveTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CombinerBuilderImplTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var combinerBuilderImpl: CombinerBuilderImpl
    private lateinit var taskCombiner: TaskCombiner

    private val liveTask1 = liveTask {
        emit(LiveTaskResult.Success("ok"))
    }

    @Before
    fun setup() = runBlockingTest {

        taskCombiner = TaskCombiner(liveTask1) {
            cancelable(false)
            retryable(false)
        }
        combinerBuilderImpl = CombinerBuilderImpl(taskCombiner)
        taskCombiner.run()
    }

    @Test
    fun `test cancellation_default value is true `() {
        assertFalse(taskCombiner.cancelable)
    }

    /* @Test
     fun `test retryable_ default value is true `() {
         assertFalse(taskCombiner.retryable)
     }*/

    /* @Test
     fun `test last value _ it must be equal with Result Success "ok"`() {

         runBlockingTest {
             delay(2_000L)
         }
         assertThat((combinerBuilderImpl.latestValue as Result.Success).data).isEqualTo("ok")
     }*/

}