package com.example.template.core

import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import org.junit.Test


class LoggerTest {
    val errorEvent = MutableLiveData<ErrorEvent>()

    @Test
    fun test1() {
//        errorEvent.apply {
//            ErrorEvent(Exception("ali"))
//        }
//        Logger.errorEvent = errorEvent
//        Logger.lastShownErrorTime = 1
//        println(System.currentTimeMillis() )
//        Logger.lastErrorEvent = Exception("ali")
        Logger.errorEvent.postValue(ErrorEvent((Exception("alii"))))
        assertThat(Logger.testFun()).isTrue()


    }

}