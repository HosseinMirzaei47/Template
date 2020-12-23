package com.example.template.home.domain

import com.example.template.core.IoDispatcher
import com.example.template.core.usecases.LiveTaskUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject

class RegionUseCase @Inject constructor(
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : LiveTaskUseCase<String, MutableList<String>>(coroutineDispatcher) {

    override suspend fun execute(params: String): MutableList<String> {
        if (params != "N/A") {
            delay(2000)
        }
        return when (params) {
            "Mashhad" -> {
                mutableListOf(
                    "N/A",
                    "emamat",
                    "vakil abad",
                    "qasem abad",
                    "hashemie",
                    "daneshju",
                    "seyed razi"
                )
            }
            "Tehran" -> {
                mutableListOf(
                    "N/A",
                    "qeytarie",
                    "nazi abad",
                    "niavaran",
                    "tehran pars"
                )
            }
            "Shiraz" -> {
                mutableListOf(
                    "N/A",
                    "hafezie",
                    "sadie",
                    "takht jamshid"
                )
            }
            else -> {
//                throw Exception("N/A")
                mutableListOf(
                    "N/A"
                )
            }
        }
    }

}