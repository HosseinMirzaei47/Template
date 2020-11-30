package com.example.template.livedataUtils.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.template.livedataUtils.data.Combiner
import com.example.template.livedataUtils.data.Resource

class CombineTestViewModel :
    ViewModel() {

    val list = mutableListOf<MutableLiveData<Resource<Any>>>()

    fun test() {
        val a = Combiner(list)
    }
}