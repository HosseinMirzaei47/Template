package com.example.template.connection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.template.testutils.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
class ConnectionLiveDataTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var connectionLiveData: ConnectionLiveData

    @Before
    fun setUp() {
        connectionLiveData = ConnectionLiveData(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun connectionTest() {
        val bool = connectionLiveData.getOrAwaitValue()

        assert(bool)

//        connectionLiveData.getConnectivityManagerCallback().onAvailable(Network.fromNetworkHandle(400)w)
    }


}