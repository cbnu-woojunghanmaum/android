package com.devjsg.cj_logistics_future_technology.data.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.network.NetworkConstants
import com.devjsg.cj_logistics_future_technology.di.worker.ChildWorkerFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.headers
import kotlinx.coroutines.flow.first

class StepCounterWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataStoreManager: DataStoreManager,
    private val httpClient: HttpClient
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val stepCount = inputData.getInt(EXTRA_STEP_COUNT, 0)
        val token = dataStoreManager.token.first()

        return try {
            val response = httpClient.post("${NetworkConstants.BASE_URL}reporting/step-count") {
                headers {
                    append("Authorization", "Bearer $token")
                    append("Accept", "*/*")
                }
                url {
                    parameters.append("step", stepCount.toString())
                }
            }

            if (response.status.value in 200..299) {
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("StepCounterWorker", "Failed to send step count data", e)
            Result.failure()
        }
    }

    @AssistedFactory
    interface Factory : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): StepCounterWorker
    }

    companion object {
        const val EXTRA_STEP_COUNT = "com.devjsg.cj_logistics_future_technology.EXTRA_STEP_COUNT"

        fun enqueueWork(context: Context, stepCount: Int) {
            val workRequest = OneTimeWorkRequestBuilder<StepCounterWorker>()
                .setInputData(workDataOf(EXTRA_STEP_COUNT to stepCount))
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}