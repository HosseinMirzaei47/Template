package com.example.template.livedataUtils.data

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class Combiner(private val lives: MutableList<MutableLiveData<Resource<Any>>>) {

    var isLoading = MediatorLiveData<Boolean>()

    init {
        lives.forEach { myLive ->
            isLoading.addSource(myLive) {
                checkAll()
            }
        }
    }

    private fun checkAll() {
        isLoading.value = lives.any {
            it.value?.status == Status.LOADING
        }
    }
}