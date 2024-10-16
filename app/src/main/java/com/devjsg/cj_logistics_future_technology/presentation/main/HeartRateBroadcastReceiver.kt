package com.devjsg.cj_logistics_future_technology.presentation.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.devjsg.cj_logistics_future_technology.data.source.remote.HeartRateListenerService
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MyHeartRateViewModel

class HeartRateBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == HeartRateListenerService.ACTION_HEART_RATE_AVG_UPDATE) {
            val heartRateAvg = intent.getIntExtra(HeartRateListenerService.EXTRA_HEART_RATE_AVG, 0)
            Log.d(TAG, "Received heartRateAvg in BroadcastReceiver: $heartRateAvg")
            val viewModel = ViewModelProvider(context as ViewModelStoreOwner)[MyHeartRateViewModel::class.java]
            viewModel.setHeartRateAvg(heartRateAvg)
        }
    }
    companion object {
        private const val TAG = "HeartRateBroadcastReceiver"
    }
}