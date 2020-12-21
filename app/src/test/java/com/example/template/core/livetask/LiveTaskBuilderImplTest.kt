package com.example.template.core.livetask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.template.core.Result
import com.example.template.core.livatask.CoroutineLiveTask
import com.example.template.core.livatask.LiveTaskBuilderImpl
import com.example.template.core.livatask.liveTask
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LiveTaskBuilderImplTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var liveTaskBuilderImpl: LiveTaskBuilderImpl<*>

    private val target = liveTask {
        emit(Result.Success("ok"))
    }


    @Before
    fun setup() = runBlocking {


        liveTaskBuilderImpl =
            LiveTaskBuilderImpl((target as CoroutineLiveTask<*>).run(), Dispatchers.IO)
        delay(100L)
    }

    @Test
    fun `test last value _ it must be equal with Result Success "ok"`() {
        assertThat((liveTaskBuilderImpl.latestValue as Result.Success).data).isEqualTo("ok")
    }

    @Test
    fun `test emit `() = runBlockingTest {
        liveTaskBuilderImpl.emit(Result.Error(Exception("network error")))

        assertThat((liveTaskBuilderImpl.latestValue as Result.Error).exception.message).isEqualTo("network error")
    }

    @Test
    fun `retry attempts work fine `() {
        liveTaskBuilderImpl.retryAttempts(13)
        assertThat((target as CoroutineLiveTask).retryAttempts).isEqualTo(13)
    }

    @Test
    fun `test auto retry _ first value is true`() {
        liveTaskBuilderImpl.autoRetry(false)
        assertThat((target as CoroutineLiveTask).autoRetry).isFalse()
    }

    @Test
    fun `test cancelable_first value is true `() {
        liveTaskBuilderImpl.cancelable(false)
        assertThat((target as CoroutineLiveTask).cancelable).isFalse()
    }

    @Test
    fun `test retryable value is true `() {
        liveTaskBuilderImpl.retryable(false)
        assertThat((target as CoroutineLiveTask).retryable).isFalse()
    }
}