package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class SendReportWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override suspend fun doWork(): Result {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        val location = getLastLocation()
        return if (location != null) {
            val x = location.latitude.toFloat()
            val y = location.longitude.toFloat()

            val nodeId = getSmartphoneNodeId()
            if (nodeId != null) {
                sendReportToPhone(x, y)
                Result.success()
            } else {
                Log.e(TAG, "No connected node found")
                Result.failure()
            }
        } else {
            Log.e(TAG, "Failed to get location")
            Result.failure()
        }
    }

    private fun getSmartphoneNodeId(): String? {
        val nodeClient = Wearable.getNodeClient(applicationContext)
        val nodes = Tasks.await(nodeClient.connectedNodes)
        return nodes.firstOrNull()?.id
    }

    private suspend fun sendReportToPhone(x: Float, y: Float) {
        val putDataMapRequest = PutDataMapRequest.create("/report_location").apply {
            dataMap.putFloat("x", x)
            dataMap.putFloat("y", y)
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }
        val request = putDataMapRequest.asPutDataRequest().setUrgent()
        val dataClient = Wearable.getDataClient(applicationContext)
        dataClient.putDataItem(request).await()
    }

    private suspend fun getLastLocation(): Location? {
        return if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            null
        } else {
            fusedLocationClient.lastLocation.await()
        }
    }

    companion object {
        private const val TAG = "SendReportWorker"
    }
}

/* class SendReportWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override suspend fun doWork(): Result {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        val location = getLastLocation()
        return if (location != null) {
            val x = location.latitude.toFloat()
            val y = location.longitude.toFloat()

            val nodeId = getSmartphoneNodeId()
            if (nodeId != null) {
                sendReportToPhone(x, y)
                Result.success()
            } else {
                Log.e(TAG, "No connected node found")
                Result.failure()
            }
        } else {
            Log.e(TAG, "Failed to get location")
            Result.failure()
        }
    }

    private fun getSmartphoneNodeId(): String? {
        val nodeClient = Wearable.getNodeClient(applicationContext)
        val nodes = Tasks.await(nodeClient.connectedNodes)
        return nodes.firstOrNull()?.id
    }

    private suspend fun sendReportToPhone(x: Float, y: Float) {
        val putDataMapRequest = PutDataMapRequest.create("/report_location").apply {
            dataMap.putFloat("x", x)
            dataMap.putFloat("y", y)
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }
        val request = putDataMapRequest.asPutDataRequest().setUrgent()
        val dataClient = Wearable.getDataClient(applicationContext)
        dataClient.putDataItem(request).await()
    }

    private suspend fun getLastLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        // 가상의 위치를 생성합니다.
        val location = Location("mockProvider").apply {
            latitude = 37.4219983 // 예: Googleplex 위치의 위도
            longitude = -122.084 // 예: Googleplex 위치의 경도
            time = System.currentTimeMillis()
            accuracy = 1.0f
        }
        return location
    }

    companion object {
        private const val TAG = "SendReportWorker"
    }
}*/