package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.CoroutineLiveTask
import com.example.template.core.livatask.LiveTaskBuilderImpl
import com.example.template.core.livatask.liveTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LiveTaskBuilderImplTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var liveTaskBuilderImpl: LiveTaskBuilderImpl<*>

    private val target = liveTask {
        emit(LiveTaskResult.Success("ok"))
    }


    @Before
    fun setup() = runBlocking {
        liveTaskBuilderImpl =
            LiveTaskBuilderImpl((target as CoroutineLiveTask<*>).run(), Dispatchers.IO)
        delay(100L)
    }

    @Test
    fun `test last value _ it must be equal with Result Success "ok"`() {

        assertEquals("ok", (liveTaskBuilderImpl.latestValue as LiveTaskResult.Success).data)
    }

    @Test
    fun `test emit `() = runBlockingTest {
        liveTaskBuilderImpl.emit(LiveTaskResult.Error(Exception("network error")))

        assertEquals(
            "network error",
            (liveTaskBuilderImpl.latestValue as LiveTaskResult.Error).exception.message
        )
    }

    @Test
    fun `retry attempts work fine `() {
        liveTaskBuilderImpl.retryAttempts(13)
        assertEquals(13, (target as CoroutineLiveTask).retryAttempts)
    }

    @Test
    fun `test auto retry _ first value is true`() {
        liveTaskBuilderImpl.autoRetry(false)
        assertFalse((target as CoroutineLiveTask).autoRetry)
    }

    @Test
    fun `test cancelable_first value is true `() {
        liveTaskBuilderImpl.cancelable(false)
        assertFalse((target as CoroutineLiveTask).cancelable)
    }

    @Test
    fun `test retryable value is true `() {
        liveTaskBuilderImpl.retryable(false)
        assertFalse((target as CoroutineLiveTask).retryable)
    }
}
