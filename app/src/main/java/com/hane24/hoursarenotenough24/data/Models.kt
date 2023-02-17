package com.hane24.hoursarenotenough24.data

import com.hane24.hoursarenotenough24.R
import com.hane24.hoursarenotenough24.utils.TodayCalendarUtils
import com.hane24.hoursarenotenough24.utils.calculateDaysOfMonth
import java.util.*


data class CalendarItem(
    val day: Int,
    val durationTime: Long,
    val isNextDay: Boolean,
) {
    val color
        get() = when {
            isNextDay -> R.color.next_day_background
            durationTime == 0L -> R.color.white
            durationTime <= 3L * 3600 -> R.color.calendar_color1
            durationTime <= 6L * 3600 -> R.color.calendar_color2
            durationTime <= 9L * 3600 -> R.color.calendar_color3
            durationTime <= 12L * 3600 -> R.color.calendar_color4
            else -> R.color.calendar_color5
        }
}

data class LogTableItem(
    val inTime: String,
    val outTime: String,
    val durationTime: String
)

data class MonthTimeLogContainer(
    val year: Int,
    val month: Int,
    var monthLog: List<TimeLogItem>
)

data class TimeLogItem(
    val day: Int,
    val inTime: String,
    val outTime: String,
    val durationTime: Long
)

fun MonthTimeLogContainer.getLogTableList(day: Int): List<LogTableItem> {
    return monthLog.filter { it.day == day }.map { log ->
        val durationString = log.durationTime.let { time ->
            var second = time
            val hour = second / 3600
            second -= hour * 3600
            val min = second / 60
            second -= min * 60
            String.format("%02d:%02d:%02d", hour, min, second)
        }
        LogTableItem(log.inTime, log.outTime, durationString)
    }
}

fun MonthTimeLogContainer.getCalendarList(): List<CalendarItem> {
    val calendar = Calendar.getInstance().apply { set(year, month - 1, 1) }
    val newList = mutableListOf<CalendarItem>()
    for (i in 1 until calendar.get(Calendar.DAY_OF_WEEK)) {
        newList.add(CalendarItem(0, 0, false))
    }
    for (i in 1..calendar.calculateDaysOfMonth()) {
        val durationTime = monthLog.filter { it.day == i }.sumOf { it.durationTime }
        val isNextDay = when {
            calendar.get(Calendar.YEAR) < TodayCalendarUtils.year -> false
            calendar.get(Calendar.MONTH) + 1 < TodayCalendarUtils.month -> false
            i > TodayCalendarUtils.day -> true
            else -> false
        }
        newList.add(CalendarItem(i, durationTime, isNextDay))
    }
    return newList
}
