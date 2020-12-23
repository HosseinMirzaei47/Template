package com.example.template.home.domain

import com.example.template.core.IoDispatcher
import com.example.template.core.usecases.LiveTaskUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject

class RegionCodeUseCase @Inject constructor(
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : LiveTaskUseCase<String, MutableList<String>>(coroutineDispatcher) {

    override suspend fun execute(params: String): MutableList<String> {
        if (params != "N/A") {
            delay(3000)
        }
        return when (params) {
            "emamat" -> {
                mutableListOf(
                    "01"
                )
            }
            "vakil abad" -> {
                mutableListOf(
                    "02"
                )
            }
            "qasem abad" -> {
                mutableListOf(
                    "03"
                )
            }
            "hashemie" -> {
                mutableListOf(
                    "04"
                )
            }
            "daneshju" -> {
                mutableListOf(
                    "05"
                )
            }
            "seyed razi" -> {
                mutableListOf(
                    "06"
                )
            }
            "qeytarie" -> {
                mutableListOf(
                    "11"
                )
            }
            "nazi abad" -> {
                mutableListOf(
                    "12"
                )
            }
            "niavaran" -> {
                mutableListOf(
                    "13"
                )
            }
            "tehran pars" -> {
                mutableListOf(
                    "14"
                )
            }
            "hafezie" -> {
                mutableListOf(
                    "21"
                )
            }
            "sadie" -> {
                mutableListOf(
                    "22"
                )
            }
            "takht jamshid" -> {
                mutableListOf(
                    "23"
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