package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.core.util.Resource
import com.example.template.home.data.repository.HomeRepository
import com.example.template.home.data.servicemodels.UserRes
import com.example.template.home.domain.GetUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel @ViewModelInject constructor(private val userUseCase: GetUserUseCase) :
    ViewModel() {
    init {
        getUsers(1)
    }

     val userResResponse = MutableLiveData<Resource<UserRes>>()

    private fun getUsers(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        userResResponse.postValue(Resource.Loading())
        val result = userUseCase.getUsers(page)
        userResResponse.postValue(result)
    }

}