package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.util.Log
import androidx.work.WorkManager
import com.devjsg.cj_logistics_future_technology.data.work.StepCounterWorker
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepListenerService : WearableListenerService() {

    @Inject
    lateinit var workManager: WorkManager

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/step_counter") {
            val stepCount = String(messageEvent.data).toIntOrNull() ?: 0
            Log.d("StepListenerService", "Received step count: $stepCount")

            StepCounterWorker.enqueueWork(this, stepCount)
        } else {
            super.onMessageReceived(messageEvent)
        }
    }
}