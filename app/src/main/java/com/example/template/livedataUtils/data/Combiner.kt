package com.example.template.livedataUtils.data

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class Combiner(private val lives: MutableList<MutableLiveData<Resource<Any>>>) {

    var isLoading = MediatorLiveData<Resource<Any>>()

    init {
        lives.forEach { myLive ->
            isLoading.addSource(myLive) {
                checkAll()
            }
        }
    }

    private fun checkAll() {
        val loading = lives.any {
            it.value?.status == Status.LOADING
        }
        if (loading) {
            isLoading.postValue(Resource(Status.LOADING, null, null))
        } else {
            isLoading.postValue(Resource(Status.SUCCESS, null, null))
        }

    }
}