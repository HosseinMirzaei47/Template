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

    private var list1 = mutableListOf<MutableLiveData<Resource<Any>>>()
    private var list2 = mutableListOf<MutableLiveData<Resource<Any>>>()
    private var list3 = mutableListOf<MutableLiveData<Resource<Any>>>()
    var live1 = MutableLiveData<Resource<String>>()
    var live2 = MutableLiveData<Resource<String>>()
    var live3 = MutableLiveData<Resource<String>>()
    var live4 = MutableLiveData<Resource<String>>()
    var combiner: Combiner
    var combiner2: Combiner
    var combiner3: Combiner

    init {
        list1.add(live1 as MutableLiveData<Resource<Any>>)
        list1.add(live2 as MutableLiveData<Resource<Any>>)
        list2.add(live3 as MutableLiveData<Resource<Any>>)
        list2.add(live4 as MutableLiveData<Resource<Any>>)
        combiner = Combiner(list1)
        combiner2 = Combiner(list2)
        list3.add(combiner.isLoading)
        list3.add(combiner2.isLoading)
        combiner3 = Combiner(list3)
    }

    fun liveChange1(text: String) {
        viewModelScope.launch {
            live1.postValue(Resource(Status.LOADING, "", "before delay"))
            delay(7000)
            live1.postValue(Resource(Status.SUCCESS, text, "after delay"))
        }
    }

    fun liveChange2(text: String) {
        viewModelScope.launch {
            live2.value = Resource(Status.LOADING, "", "before delay")
            delay(7000)
            live2.value = Resource(Status.SUCCESS, text, "after delay")
        }
    }

    fun liveChange3(text: String) {
        viewModelScope.launch {
            live3.postValue(Resource(Status.LOADING, "", "before delay"))
            delay(7000)
            live3.postValue(Resource(Status.SUCCESS, text, "after delay"))
        }
    }

    fun liveChange4(text: String) {
        viewModelScope.launch {
            live4.value = Resource(Status.LOADING, "", "before delay")
            delay(7000)
            live4.value = Resource(Status.SUCCESS, text, "after delay")
        }
    }

}