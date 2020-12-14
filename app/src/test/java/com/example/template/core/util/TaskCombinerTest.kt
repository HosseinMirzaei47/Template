package com.example.template.core.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.template.BaseLiveTask
import com.example.template.core.Result
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskCombinerTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private val liveData1 = MutableLiveData<Result<Int>>() as BaseLiveTask<Int>
    private val liveData2 = MutableLiveData<Result<Int>>() as BaseLiveTask<Int>
    private val liveData3 = MutableLiveData<Result<Int>>() as BaseLiveTask<Int>
    private val liveData4 = MutableLiveData<Result<Int>>() as BaseLiveTask<Int>
    private val liveData5 = MutableLiveData<Result<Int>>() as BaseLiveTask<Int>
    private val liveData6 = MutableLiveData<Result<Int>>() as BaseLiveTask<Int>
    private val liveData7 = MutableLiveData<Result<Int>>() as BaseLiveTask<Int>

    lateinit var taskCombiner: TaskCombiner


    @Before
    fun setup() {
        liveData1.postValue(Result.Loading)
        liveData2.postValue(Result.Error(Exception("error")))

        liveData3.postValue(Result.Success(0))
        liveData4.postValue(Result.Success(20))
        liveData5.postValue(Result.Success(30))
        liveData6.postValue(Result.Success(40))
        liveData7.postValue(Result.Success(50))

        taskCombiner = TaskCombiner(
            liveData1,
            liveData1,
            liveData1,
            liveData1,
            liveData1,
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
    fun `test task combiner class return error taskCombiner class`() {


    }


}