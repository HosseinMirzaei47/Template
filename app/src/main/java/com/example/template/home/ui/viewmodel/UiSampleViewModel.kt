package com.example.template.home.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.template.core.LiveTaskResult
import com.example.template.core.livatask.combinedTask
import com.example.template.core.livatask.liveTask
import com.example.template.home.domain.CityUseCase
import com.example.template.home.domain.RegionCodeUseCase
import com.example.template.home.domain.RegionUseCase
import kotlinx.coroutines.delay

class UiSampleViewModel @ViewModelInject constructor(
    val regionUseCase: RegionUseCase ,
    val regionCodeUseCase: RegionCodeUseCase,
    val cityUseCase: CityUseCase
) : ViewModel() {

    val cityList = liveTask {
        emit(LiveTaskResult.Success(cityListFill()))
    }

    private suspend fun cityListFill(): MutableList<String> {
        delay(1000)
        return mutableListOf("N/A", "Mashhad", "Tehran", "Shiraz")
    }

    val regionList = regionUseCase.asLiveTask("N/A")

    val regionCode = regionCodeUseCase.asLiveTask("N/A")

    val combined = combinedTask(cityList , regionList,regionCode)

    val cities = cityUseCase.asLiveTask("N/A")
}