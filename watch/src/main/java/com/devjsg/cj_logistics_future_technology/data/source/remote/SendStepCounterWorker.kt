package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable

class SendStepCounterWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val stepCount = inputData.getInt("stepCount", 0)

        val nodeId = getSmartphoneNodeId()
        return if (nodeId != null) {
            Log.d(ContentValues.TAG, "get Node Id : $nodeId")
            sendStepCounterToPhone(stepCount, nodeId)
            Result.success()
        } else {
            Log.e(ContentValues.TAG, "No connected node found")
            Result.failure()
        }
    }

    private fun getSmartphoneNodeId(): String? {
        val nodeClient: NodeClient = Wearable.getNodeClient(applicationContext)
        val nodes = Tasks.await(nodeClient.connectedNodes)
        return nodes.firstOrNull()?.id
    }

    @SuppressLint("VisibleForTests")
    private fun sendStepCounterToPhone(stepCount: Int, nodeId: String) {
        val messageClient: MessageClient = Wearable.getMessageClient(applicationContext)
        val payload = stepCount.toString().toByteArray()

        messageClient.sendMessage(nodeId, "/step_counter", payload)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Successfully sent step count data to phone: $stepCount")
            }
            .addOnFailureListener { e ->
                Log.e(ContentValues.TAG, "Failed to send step count data to phone", e)
            }
    }
}