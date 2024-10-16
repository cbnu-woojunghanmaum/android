package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable

class SendHeartRateAvgWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val heartRateAvg = inputData.getInt("heartRateAvg", 0)

        val nodeId = getSmartphoneNodeId()
        return if (nodeId != null) {
            Log.d(TAG, "get Node Id : $nodeId")
            sendHeartRateAvgToPhone(heartRateAvg, nodeId)
            Result.success()
        } else {
            Log.e(TAG, "No connected node found")
            Result.failure()
        }
    }

    private fun getSmartphoneNodeId(): String? {
        val nodeClient: NodeClient = Wearable.getNodeClient(applicationContext)
        val nodes = Tasks.await(nodeClient.connectedNodes)
        return nodes.firstOrNull()?.id
    }

    @SuppressLint("VisibleForTests")
    private fun sendHeartRateAvgToPhone(heartRateAvg: Int, nodeId: String) {
        val messageClient: MessageClient = Wearable.getMessageClient(applicationContext)
        val payload = heartRateAvg.toString().toByteArray()

        messageClient.sendMessage(nodeId, "/heart_rate_avg", payload)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully sent heart rate avg data to phone: $heartRateAvg")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to send heart rate avg data to phone", e)
            }
    }
}