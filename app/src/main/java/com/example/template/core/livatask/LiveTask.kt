package com.example.template.core.livatask

import androidx.lifecycle.LiveData
import com.example.template.core.LiveTaskResult
import com.example.template.core.bindingadapter.ProgressType

interface LiveTask<T> {
    var loadingViewType: ProgressType
    fun result(): LiveTaskResult<T>?
    fun asLiveData(): LiveData<LiveTask<T>>
    fun retry()
    fun run(): LiveTask<T>
    fun cancel()
}
