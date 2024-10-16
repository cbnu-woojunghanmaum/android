package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.devjsg.cj_logistics_future_technology.data.source.remote.HeartRateService
import com.devjsg.cj_logistics_future_technology.data.source.remote.SendReportWorker
import com.devjsg.cj_logistics_future_technology.presentation.service.StepCounterService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _heartRate = MutableStateFlow(0)
    val heartRate: StateFlow<Int> = _heartRate

    private val _heartRateAvg = MutableStateFlow(0)
    val heartRateAvg: StateFlow<Int> = _heartRateAvg

    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val heartRateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == "com.devjsg.watch.HEART_RATE_UPDATE") {
                    val heartRate = it.getIntExtra("heartRate", 0)
                    _heartRate.value = heartRate
                }
                if (it.action == "com.devjsg.watch.HEART_RATE_AVG_UPDATE") {
                    val heartRateAvg = it.getIntExtra("heartRateAvg", 0)
                    _heartRateAvg.value = heartRateAvg
                }
            }
        }
    }

    private val stepCountReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == "com.devjsg.watch.STEP_COUNT_UPDATE") {
                    val stepCount = it.getIntExtra("stepCount", 0)
                    Log.d(TAG, "Step Count: $stepCount")
                }
            }
        }
    }

    fun report() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Report started")
            val location = getLastLocation()
            if (location != null) {
                Log.d(TAG, "Location obtained: (${location.latitude}, ${location.longitude})")
                scheduleSendReportWork(location.latitude.toFloat(), location.longitude.toFloat())
            } else {
                Log.d(TAG, "Location denied")
            }
        }
    }

    private fun scheduleSendReportWork(x: Float, y: Float) {
        Log.d(TAG, "Scheduling report work with coordinates: x=$x, y=$y")
        val inputData = Data.Builder()
            .putFloat("x", x)
            .putFloat("y", y)
            .build()

        val sendReportWorkRequest = OneTimeWorkRequestBuilder<SendReportWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(sendReportWorkRequest)
    }

    private suspend fun getLastLocation(): Location? {
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "Location permissions not granted")
            null
        } else {
            fusedLocationClient.lastLocation.await().also {
                if (it != null) {
                    Log.d(TAG, "Last location: (${it.latitude}, ${it.longitude})")
                } else {
                    Log.e(TAG, "Last location is null")
                }
            }
        }
    }

    /*private suspend fun getLastLocation(): Location? {
        // 가상의 위치를 생성합니다.
        val location = Location("mockProvider").apply {
            latitude = 37.4219983 // 예: Googleplex 위치의 위도
            longitude = -122.084 // 예: Googleplex 위치의 경도
            time = System.currentTimeMillis()
            accuracy = 1.0f
        }
        Log.d(TAG, "Returning mock location: (${location.latitude}, ${location.longitude})")
        return location
    }*/

    init {
        val intentFilter = IntentFilter().apply {
            addAction("com.devjsg.watch.HEART_RATE_UPDATE")
            addAction("com.devjsg.watch.HEART_RATE_AVG_UPDATE")
            addAction("com.devjsg.watch.STEP_COUNT_UPDATE")
        }
        context.registerReceiver(heartRateReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        context.registerReceiver(stepCountReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onCleared() {
        super.onCleared()
        context.unregisterReceiver(heartRateReceiver)
        context.unregisterReceiver(stepCountReceiver)
    }

    fun startService() {
        HeartRateService.startService(context)
        startStepCounterService()
        _isMonitoring.value = true
        Log.d(TAG, "Heart rate monitoring service started")
    }

    fun stopService() {
        HeartRateService.stopService(context)
        stopStepCounterService()
        _isMonitoring.value = false
        Log.d(TAG, "Heart rate monitoring service stopped")
    }

    private fun startStepCounterService() {
        val intent = Intent(context, StepCounterService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    private fun stopStepCounterService() {
        val intent = Intent(context, StepCounterService::class.java)
        context.stopService(intent)
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}