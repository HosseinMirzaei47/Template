
package com.example.template

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        for (i in 0..10) {
            println("i $i")
            outer@ for (j in 0..10) {

                println("j $j")

                if (j == 4) break@outer
            }
            println("k $i")
        }
    }
}