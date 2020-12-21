package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.template.core.Result
import com.example.template.core.livatask.liveTask
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CoroutineLiveTaskHelpTest {

    var flagRetry = true

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val liveData3 = liveTask {
        if (flagRetry) {
            emit(Result.Error(Exception("fake error")))
        } else {
            emit(Result.Success("success"))
        }
    }

    @Test
    fun testRetryOfLiveTask() {
        val result1 = liveData3.run()
        flagRetry = false
        liveData3.retry()

        assert((result1.result() as Result.Error).exception.message == "fake error")
//        assert(liveData3.result() is Result.Success)
    }
}