package com.example.template.livedataUtils.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.template.livedataUtils.data.Combiner
import com.example.template.livedataUtils.data.Resource

class CombineTestViewModel :
    ViewModel() {

    private var list = mutableListOf<MutableLiveData<Resource<Any>>>()
    var live1 = MutableLiveData<Resource<String>>()
    var live2 = MutableLiveData<Resource<String>>()
    var mediator: Combiner

    init {
        list.add(live1)
        mediator = Combiner(list)
    }


}