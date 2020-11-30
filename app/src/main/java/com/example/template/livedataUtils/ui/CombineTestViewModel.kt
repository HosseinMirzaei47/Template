package com.example.template.livedataUtils.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.livedataUtils.data.Combiner
import com.example.template.livedataUtils.data.Resource
import com.example.template.livedataUtils.data.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CombineTestViewModel :
    ViewModel() {

    private var list = mutableListOf<MutableLiveData<Resource<Any>>>()
    var live1 = MutableLiveData<Resource<String>>()
    var live2 = MutableLiveData<Resource<String>>()
    var mediator: Combiner

    init {
        list.add(live1 as MutableLiveData<Resource<Any>>)
        list.add(live2 as MutableLiveData<Resource<Any>>)
        mediator = Combiner(list)
    }

    fun liveChange1(text : String){
        viewModelScope.launch {
            live1.postValue(Resource(Status.LOADING , "" , "before delay"))
            delay(7000)
            live1.postValue(Resource(Status.SUCCESS , text , "after delay"))
        }
    }

    fun liveChange2(text : String){
        viewModelScope.launch {
            live2.value = Resource(Status.LOADING , "" , "before delay")
            delay(7000)
            live2.value = Resource(Status.SUCCESS , text , "after delay")
        }
    }

}