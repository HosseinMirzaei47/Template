package com.example.template.livedataUtils.data

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.template.core.util.Resource

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
            it.value is Resource.Loading
        }
        if (loading) {
            result.postValue(Resource.Loading())
        } else {
            result.postValue(Resource.Success("null"))
        }
    }

    private fun checkAllForError() {
        val error = lives.any {
            it.value is Resource.Error
        }
        if (error) {
            var totalError = ""
            lives.forEach {
                if (it.value is Resource.Error) {
                    totalError += "\n" + it.value!!.data.toString()
                }
            }
            result.postValue(Resource.Error(totalError))
        } else {
            checkAllForLoading()
        }
    }
}