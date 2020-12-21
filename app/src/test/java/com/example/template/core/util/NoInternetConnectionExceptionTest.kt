package com.example.template.core.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class NoInternetConnectionExceptionTest {

    private val noConnectionException = Exception("Unable to resolve host")

    private val testException = Exception("error ")

    @Test
    fun `test exception is from no connection exception kind`() {
        assertThat(noConnectionException.detectException() is NoConnectionException).isTrue()
    }

    @Test
    fun `test normal exception`() {
        assertThat(testException.detectException() is NoConnectionException).isFalse()

    }
}