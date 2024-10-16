package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.data.work.ReportLocationWorker
import com.devjsg.cj_logistics_future_technology.presentation.main.MyApplication
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
                        Log.d(TAG, "Received report location on phone: x=$x, y=$y")

                        val inputData = Data.Builder()
                            .putFloat("x", x)
                            .putFloat("y", y)
                            .build()

                        val workRequest = OneTimeWorkRequestBuilder<ReportLocationWorker>()
                            .setInputData(inputData)
                            .build()

                        WorkManager.getInstance(applicationContext).enqueue(workRequest)
                    }
                }
            }
        }
    }

    private fun showNotification(message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle("New Notification")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(1, notification)
    }

    private fun showReportNotification(x: Float, y: Float) {
        val message = "Report location: x=$x, y=$y"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setContentTitle("Report Location")
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