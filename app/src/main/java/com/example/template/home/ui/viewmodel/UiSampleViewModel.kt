package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.template.core.Result
import com.example.template.core.livatask.liveTask
import com.example.template.home.domain.RegionUseCase
import kotlinx.coroutines.delay

class UiSampleViewModel @ViewModelInject constructor(
    val regionUseCase: RegionUseCase
) : ViewModel() {

    val cityList = liveTask {
        emit(Result.Success(cityListFill()))
    }

    private suspend fun cityListFill(): MutableList<String> {
        delay(2000)
        return mutableListOf("N/A", "Mashhad", "Tehran", "Shiraz")
    }

    val regionList = regionUseCase.asLiveTask("N/A")

}