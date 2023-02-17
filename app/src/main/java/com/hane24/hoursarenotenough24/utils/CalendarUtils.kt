package com.hane24.hoursarenotenough24.utils

import java.util.*


object TodayCalendarUtils {
    private var calendar = Calendar.getInstance()
    val year: Int
        get() {
            calendar = Calendar.getInstance()
            return calendar.get(Calendar.YEAR)
        }
    val month: Int
        get() {
            calendar = Calendar.getInstance()
            return calendar.get(Calendar.MONTH) + 1
        }
    val day: Int
        get() {
            calendar = Calendar.getInstance()
            return calendar.get(Calendar.DAY_OF_MONTH)
        }
}

fun Calendar.checkLeapYear(): Boolean {
    val year = get(Calendar.YEAR)
    return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
}

fun Calendar.calculateDaysOfMonth() = when (get(Calendar.MONTH) + 1) {
    1 -> 31
    2 -> if (checkLeapYear()) 29 else 28
    3 -> 31
    4 -> 30
    5 -> 31
    6 -> 30
    7 -> 31
    8 -> 31
    9 -> 30
    10 -> 31
    11 -> 30
    else -> 31
}