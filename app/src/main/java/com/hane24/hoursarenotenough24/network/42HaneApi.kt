package com.hane24.hoursarenotenough24.network

import com.hane24.hoursarenotenough24.BuildConfig
import com.hane24.hoursarenotenough24.data.AccumulationTimeInfo
import com.hane24.hoursarenotenough24.data.InOutTimeContainer
import com.hane24.hoursarenotenough24.data.MainInfo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val BASE_URL = BuildConfig.BASE_URL

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface hane42Api {
    @GET("/user/login/islogin")
    suspend fun isLogin(
        @Header("Authorization") token: String?
    ): Response<String?>

    @GET("/v1/tag-log/maininfo")
    suspend fun getMainInfo(
        @Header("Authorization") token: String?
    ): MainInfo

    @GET("/v1/tag-log/perday")
    suspend fun getInOutInfoPerDay(
        @Header("Authorization") token: String?,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): InOutTimeContainer

    @GET("/v1/tag-log/permonth")
    suspend fun getInOutInfoPerMonth(
        @Header("Authorization") token: String?,
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): InOutTimeContainer

    @GET("/v1/tag-log/accumulationTimes")
    suspend fun getAccumulationTime(
        @Header("Authorization") token: String?,
    ): AccumulationTimeInfo
}

object Hane42Apis {
    val hane42ApiService by lazy { retrofit.create(hane42Api::class.java) }
}