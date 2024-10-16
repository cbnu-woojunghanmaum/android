package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

object WearableNotificationSender {

    private const val TAG = "WearableNotificationSender"
    private const val WEARABLE_PATH = "/notification"
    private const val NOTIFICATION_KEY = "message"

    fun sendNotification(context: Context, message: String) {
        val dataClient = Wearable.getDataClient(context)
        val putDataMapRequest = PutDataMapRequest.create(WEARABLE_PATH).apply {
            dataMap.putString(NOTIFICATION_KEY, message)
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }
        val request = putDataMapRequest.asPutDataRequest().setUrgent()
        dataClient.putDataItem(request).addOnSuccessListener {
            Log.d(TAG, "Successfully sent notification to wearable: $message")
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to send notification to wearable", e)
        }
    }
}