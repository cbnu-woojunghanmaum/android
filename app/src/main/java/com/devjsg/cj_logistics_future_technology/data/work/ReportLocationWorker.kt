package com.devjsg.cj_logistics_future_technology.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.network.NetworkConstants
import com.devjsg.cj_logistics_future_technology.di.worker.ChildWorkerFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.flow.first

class ReportLocationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val dataStoreManager: DataStoreManager,
    private val httpClient: HttpClient
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val x = inputData.getFloat("x", 0f)
        val y = inputData.getFloat("y", 0f)

        val token = dataStoreManager.token.first()

        return try {
            val response = httpClient.post(NetworkConstants.BASE_URL + "fcm/emergency-alarm") {
                parameter("x", x)
                parameter("y", y)
                parameter("emergency", "REPORT")
                headers {
                    append("Authorization", "Bearer $token")
                    append("Accept", "application/json;charset=UTF-8")
                }
            }

            if (response.status.value in 200..299) {
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    @AssistedFactory
    interface Factory : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ReportLocationWorker
    }
}