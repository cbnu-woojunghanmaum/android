package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.devjsg.cj_logistics_future_technology.data.repository.HeartRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyHeartRateViewModel @Inject constructor(
    private val heartRateRepository: HeartRateRepository
) : ViewModel() {

    private val _heartRateAvg = MutableStateFlow<Int>(0)
    val heartRateAvg: StateFlow<Int> = _heartRateAvg

    init {
        setHeartRateAvg(_heartRateAvg.value)
    }

    fun setHeartRateAvg(heartRateAvg: Int) {
        _heartRateAvg.value = heartRateAvg
        Log.d("MyHeartRateViewModel", "setHeartRateAvg: ${_heartRateAvg.value}")
        heartRateRepository.handleReceivedHeartRateAvg(heartRateAvg)
    }
}