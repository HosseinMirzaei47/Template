package com.example.template.livedataUtils.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.template.core.util.Resource
import com.example.template.core.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CombinerTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val liveData1 = MutableLiveData<Resource<Any>>()
    private val liveData2 = MutableLiveData<Resource<Any>>()
    private val liveData3 = MutableLiveData<Resource<Any>>()
    private val liveData4 = MutableLiveData<Resource<Any>>()
    private val liveData5 = MutableLiveData<Resource<Any>>()
    private val liveData6 = MutableLiveData<Resource<Any>>()
    private val liveData7 = MutableLiveData<Resource<Any>>()

    lateinit var combiner: Combiner

    @Before
    fun setup() {
        liveData1.postValue(Resource.Success(77))
        liveData2.postValue(Resource.Success(5))
        liveData3.postValue(Resource.Success(1))
        liveData4.postValue(Resource.Success(11))
        liveData5.postValue(Resource.Success(40))

        liveData6.postValue(Resource.Error("error"))
        liveData7.postValue(Resource.Loading())

        combiner = Combiner(
            mutableListOf(
                liveData1,
                liveData2,
                liveData3,
                liveData4,
                liveData5,
                liveData6,
                liveData7
            )
        )
    }

    @Test
    fun `test Result Of LiveData`() {
        val resource = combiner.result.getOrAwaitValue()

        assertThat(resource is Resource.Error).isTrue()
    }
}