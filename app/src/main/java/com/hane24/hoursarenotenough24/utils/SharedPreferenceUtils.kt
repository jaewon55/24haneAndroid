package com.hane24.hoursarenotenough24.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.hane24.hoursarenotenough24.App

object SharedPreferenceUtils {
    private val accessTokenSharedPreferences
            by lazy { init(App.instance.applicationContext, "accessToken") }
    private val targetTimeSharedPreferences
            by lazy { init(App.instance.applicationContext, "targetTime") }

    private fun init(context: Context, key: String): SharedPreferences =
        context.getSharedPreferences(key, Context.MODE_PRIVATE)

    fun getAccessToken() = accessTokenSharedPreferences.getString("accessToken", "")

    fun saveAccessToken(accessToken: String) {
        val editor = accessTokenSharedPreferences.edit()
        editor.putString("accessToken", "Bearer $accessToken")
        editor.apply()
    }

    fun getDayTargetTime() = targetTimeSharedPreferences.getLong("dayTargetTime", 5 * 3600)

    fun saveDayTargetTime(time: Long) {
        val editor = targetTimeSharedPreferences.edit()
        editor.putLong("dayTargetTime", time)
        editor.apply()
    }

    fun getMonthTargetTime() = targetTimeSharedPreferences.getLong("monthTargetTime", 80 * 3600)

    fun saveMonthTargetTime(time: Long) {
        val editor = targetTimeSharedPreferences.edit()
        editor.putLong("monthTargetTime", time)
        editor.apply()
    }
}
