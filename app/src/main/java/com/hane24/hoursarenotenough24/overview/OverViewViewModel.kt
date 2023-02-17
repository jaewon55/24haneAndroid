package com.hane24.hoursarenotenough24.overview

import android.util.Log
import androidx.lifecycle.*
import com.hane24.hoursarenotenough24.login.State
import com.hane24.hoursarenotenough24.network.Hane42Apis
import com.hane24.hoursarenotenough24.utils.SharedPreferenceUtils
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OverViewViewModel : ViewModel() {

    private val _state = MutableLiveData<State?>(null)
    val state: LiveData<State?>
        get() = _state

    private val accessToken by lazy { SharedPreferenceUtils.getAccessToken() }

    private val _intraId = MutableLiveData("")
    val intraId: LiveData<String>
        get() = _intraId

    private val _dayAccumulationTime = MutableLiveData(0L)
    val dayAccumulationTime: LiveData<String> =
        Transformations.map(_dayAccumulationTime) { parseTime(it) }

    private val _dayTargetTime = MutableLiveData(0L)
    val dayTargetTime: LiveData<String> =
        Transformations.map(_dayTargetTime) { parseTime(it) }

    private val _dayProgressPercent = MutableLiveData(0)
    val dayProgressPercent: LiveData<Int>
        get() = _dayProgressPercent
    val dayProgressPercentText: LiveData<String> =
        Transformations.map(_dayProgressPercent) { "$it%" }

    private val _monthAccumulationTime = MutableLiveData(0L)
    val monthAccumulationTime: LiveData<String> =
        Transformations.map(_monthAccumulationTime) { parseTime(it) }

    private val _monthTargetTime = MutableLiveData(0L)
    val monthTargetTime: LiveData<String> =
        Transformations.map(_monthTargetTime) { parseTime(it) }

    private val _monthProgressPercent = MutableLiveData(0)
    val monthProgressPercent: LiveData<Int>
        get() = _monthProgressPercent
    val monthProgressPercentText: LiveData<String> =
        Transformations.map(_monthProgressPercent) { "$it%" }

    private val _latestTagTime = MutableLiveData("")
    val latestTagTime: LiveData<String>
        get() = _latestTagTime

    private val _inOutState = MutableLiveData(false)
    val inOutState: LiveData<Boolean>
        get() = _inOutState

    private val _initState = MutableLiveData(false)
    val initState: LiveData<Boolean>
        get() = _initState

    private val _refreshLoading = MutableLiveData(false)
    val refreshLoading: LiveData<Boolean>
        get() = _refreshLoading

    init {
        _dayTargetTime.value = SharedPreferenceUtils.getDayTargetTime()
        _monthTargetTime.value = SharedPreferenceUtils.getMonthTargetTime()
        viewModelScope.launch {
            useGetMainInfoApi()
            useGetAccumulationInfoApi()
            _initState.value = true
        }
    }

    private suspend fun useGetAccumulationInfoApi() {
        try {
            val accumulationTime = Hane42Apis.hane42ApiService.getAccumulationTime(accessToken)
            _monthAccumulationTime.value = accumulationTime.monthAccumulationTime
            _dayAccumulationTime.value = accumulationTime.todayAccumulationTime
            _dayProgressPercent.value =
                getProgressPercent(accumulationTime.todayAccumulationTime, _dayTargetTime.value)
            _monthProgressPercent.value =
                getProgressPercent(accumulationTime.monthAccumulationTime, _monthTargetTime.value)
            _state.value = State.SUCCESS
        } catch (err: HttpException) {
            val isLoginFail = err.code() == 401
            val isServerError = err.code() == 500

            when {
                isLoginFail -> _state.value = State.LOGIN_FAIL
                isServerError -> _state.value = State.SERVER_FAIL
                else -> _state.value = State.UNKNOWN_ERROR
            }
        } catch (e: Exception) {
            _state.value = State.UNKNOWN_ERROR
        }
    }

    private suspend fun useGetMainInfoApi() {
        try {
            val mainInfo = Hane42Apis.hane42ApiService.getMainInfo(accessToken)
            _intraId.value = mainInfo.login
            if (mainInfo.inoutState == "IN") {
                _inOutState.value = true
                _latestTagTime.value = mainInfo.tagAt
                    .substringAfter('T')
                    .split(":")
                    .let { "${it[0].toInt() + 9}:${it[1]}" }
            } else {
                _inOutState.value = false
                _latestTagTime.value = ""
            }
            _state.value = State.SUCCESS
        } catch (err: HttpException) {
            val isLoginFail = err.code() == 401
            val isServerError = err.code() == 500

            when {
                isLoginFail -> _state.value = State.LOGIN_FAIL
                isServerError -> _state.value = State.SERVER_FAIL
                else -> _state.value = State.UNKNOWN_ERROR
            }
        } catch (e: Exception) {
            _state.value = State.UNKNOWN_ERROR
        }
    }

    fun refreshButtonOnClick() {
        _refreshLoading.value = true
        viewModelScope.launch {
            useGetMainInfoApi()
            useGetAccumulationInfoApi()
            _refreshLoading.value = false
        }
    }

    fun changeTargetTime(time: Long, isMonth: Boolean) {
        if (isMonth) {
            SharedPreferenceUtils.saveMonthTargetTime(time)
            _monthTargetTime.value = time
            _monthProgressPercent.value =
                getProgressPercent(_monthAccumulationTime.value ?: 0, time)
        } else {
            SharedPreferenceUtils.saveDayTargetTime(time)
            _dayTargetTime.value = time
            _dayProgressPercent.value =
                getProgressPercent(_dayAccumulationTime.value ?: 0, time)
        }
    }

    fun getMTargetTime() = _monthTargetTime.value

    fun getDTargetTime() = _dayTargetTime.value

    private fun getProgressPercent(time: Long, targetTime: Long?): Int {
        val targetDouble: Double = targetTime?.toDouble() ?: 1.0
        val percent = (time / targetDouble * 100).toInt()
        if (percent >= 100) return 100
        return percent
    }

    private fun parseTime(time: Long): String {
        var second = time
        val hour = second / 3600
        second -= hour * 3600
        val min = second / 60
        return String.format("%d:%02d", hour, min)
    }
}
