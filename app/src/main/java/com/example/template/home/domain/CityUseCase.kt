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
        if ("khorasan razavi".startsWith(params)) {
            list.add("mashhad")
            list.add("kashmar")
            list.add("neyshabur")
        }
        if ("khorasan shomali".startsWith(params)) {
            list.add("bojnord")
        }
        if ("khorasan jonubi".startsWith(params)) {
            list.add("birjand")
        }
        if ("tehran".startsWith(params)) {
            list.add("tehran")
            list.add("karaj")
        }
        return list
    }

}