package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.devjsg.cj_logistics_future_technology.MyApplication
import com.devjsg.cj_logistics_future_technology.R
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService

class MyWearableListenerService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                when (dataItem.uri.path) {
                    "/notification" -> {
                        val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                        val message = dataMap.getString("message")
                        Log.d(TAG, "Received notification on wearable: $message")
                        showNotification(message)
                    }
                    "/report_location" -> {
                        val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                        val x = dataMap.getFloat("x")
                        val y = dataMap.getFloat("y")
                        Log.d(TAG, "Received report location on wearable: x=$x, y=$y")
                        showReportNotification(x, y)
                    }
                }
            }
        }
    }

    private fun showNotification(message: String?) {
        Log.d(TAG, "showNotification: $message")
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setContentTitle("사용자 신고 알림")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(1, notification)
    }

    private fun showReportNotification(x: Float, y: Float) {
        val message = "GPS 좌표 값: x=$x, y=$y"
        Log.d(TAG, "showReportNotification: $message")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setContentTitle("사용자 신고 위치")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(2, notification)
    }

    companion object {
        private const val TAG = "MyWearableListenerService"
    }
}