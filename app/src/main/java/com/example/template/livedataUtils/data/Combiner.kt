package com.example.template.livedataUtils.data

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class Combiner(private val lives: MutableList<MutableLiveData<Resource<Any>>>) {

    var result = MediatorLiveData<Resource<Any>>()

    init {
        lives.forEach { myLive ->
            result.addSource(myLive) {
                checkAllForError()
            }
        }
    }

    private fun checkAllForLoading() {
        val loading = lives.any {
            it.value?.status == Status.LOADING
        }
        if (loading) {
            result.postValue(Resource(Status.LOADING, null, null))
        } else {
            result.postValue(Resource(Status.SUCCESS, null, null))
        }
    }

    private fun checkAllForError() {
        val error = lives.any {
            it.value?.status == Status.ERROR
        }
        if (error) {
            var totalError = ""
            lives.forEach {
                if (it.value?.status == Status.ERROR) {
                    totalError += "\n" + it.value!!.data.toString()
                }
            }
            result.postValue(Resource(Status.ERROR, totalError, null))
        } else {
            checkAllForLoading()
        }
    }
}