package com.hane24.hoursarenotenough24.widget

import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.hane24.hoursarenotenough24.data.AccumulationTimeInfo
import com.hane24.hoursarenotenough24.network.Hane42Apis
import com.hane24.hoursarenotenough24.utils.SharedPreferenceUtils

internal fun updateRefreshAnimationOn(views: RemoteViews, progressId: Int, buttonId: Int) {
    views.setViewVisibility(progressId, View.VISIBLE)
    views.setViewVisibility(buttonId, View.GONE)
}

internal fun updateRefreshAnimationOff(views: RemoteViews, progressId: Int, buttonId: Int) {
    views.setViewVisibility(progressId, View.GONE)
    views.setViewVisibility(buttonId, View.VISIBLE)
}

internal suspend fun getAccumulationInfo(): AccumulationTimeInfo? {
    return try {
        Hane42Apis.hane42ApiService.getAccumulationTime(SharedPreferenceUtils.getAccessToken())
    } catch (err: Exception) {
        null
    }
}

internal suspend fun getInOutState(): String? {
    return try {
        Hane42Apis.hane42ApiService.getMainInfo(SharedPreferenceUtils.getAccessToken()).inoutState
    } catch (err: Exception) {
        null
    }
}

fun getProgressPercent(accumulationTime: Long): Int {
    val targetDouble = 80.0 * 3600

    val percent = (accumulationTime / targetDouble * 100).toInt()
    if (percent >= 100) return 100
    return percent
}

fun parseTimeToday(accumulationTime: Long, isCalendarText: Boolean = false): String {
    var second = accumulationTime
    val hour = second / 3600
    second -= hour * 3600
    val min = second / 60
    second -= min * 60
    return if (isCalendarText) {
        String.format("%02d:%02d", hour, min)
    } else {
        String.format("%02d : %02d", hour, min)
    }
}

fun parseTimeMonth(accumulationTime: Long, isCalendarText: Boolean = false): String {
    var second = accumulationTime
    val hour = second / 3600
    second -= hour * 3600
    val min = second / 60
    second -= min * 60
    return if (isCalendarText && hour >= 100) {
        String.format("%03d:%02d", hour, min)
    } else {
        String.format("%02d : %02d", hour, min)
    }
}
