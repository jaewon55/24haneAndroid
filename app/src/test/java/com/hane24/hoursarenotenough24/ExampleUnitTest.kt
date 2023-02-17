package com.hane24.hoursarenotenough24

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun calendar_isCoreect() {
        val date = Calendar.getInstance().apply { set(2022, 11, 1) }.time
        val monthName = SimpleDateFormat("MMM", Locale("en")).format(date)
        println(monthName)
    }
}