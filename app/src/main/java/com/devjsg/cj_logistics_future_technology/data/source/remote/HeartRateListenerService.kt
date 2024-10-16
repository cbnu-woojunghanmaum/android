package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.devjsg.cj_logistics_future_technology.data.work.HeartRateWorker
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeartRateListenerService : WearableListenerService() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "HeartRateListenerService created")
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d(TAG, "onMessageReceived called with path: ${messageEvent.path}")
        if (messageEvent.path == "/heart_rate_avg") {
            val heartRateAvg = String(messageEvent.data).toInt()
            Log.d(TAG, "Received heart rate avg data: $heartRateAvg")
            handleHeartRateAvgData(heartRateAvg)
        } else {
            Log.d(TAG, "Received unknown path: ${messageEvent.path}")
        }
    }

    private fun handleHeartRateAvgData(heartRateAvg: Int) {
        Log.d(TAG, "Handling heart rate avg data: $heartRateAvg")

        val inputData = Data.Builder()
            .putInt(EXTRA_HEART_RATE_AVG, heartRateAvg)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<HeartRateWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)

        val intent = Intent(ACTION_HEART_RATE_AVG_UPDATE).apply {
            putExtra(EXTRA_HEART_RATE_AVG, heartRateAvg)
            setPackage(packageName)
        }
        Log.d(TAG, "Sending broadcast with heartRateAvg: $heartRateAvg")
        sendBroadcast(intent)
    }

    companion object {
        private const val TAG = "HeartRateListenerService"
        const val ACTION_HEART_RATE_AVG_UPDATE = "com.devjsg.cj_logistics_future_technology.ACTION_HEART_RATE_AVG_UPDATE"
        const val EXTRA_HEART_RATE_AVG = "com.devjsg.cj_logistics_future_technology.EXTRA_HEART_RATE_AVG"
    }
}