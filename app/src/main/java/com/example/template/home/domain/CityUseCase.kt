package com.example.template.home.domain

import com.example.template.core.IoDispatcher
import com.example.template.core.usecases.LiveTaskUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject

class CityUseCase @Inject constructor(
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : LiveTaskUseCase<String, MutableList<String>>(coroutineDispatcher) {

    override suspend fun execute(params: String): MutableList<String> {
        val list = mutableListOf<String>()
        delay(1000)
        if ("خراسان رضوی".startsWith(params)) {
            list.add("مشهد")
            list.add("کاشمر")
            list.add("نیشابور")
        }
        if ("خراسان شمالی".startsWith(params)) {
            list.add("بجنورد")
        }
        if ("خراسان جنوبی".startsWith(params)) {
            list.add("بیرجند")
        }
        if ("تهران".startsWith(params)) {
            list.add("تهران")
            list.add("کرج")
        }
        return list
    }

}