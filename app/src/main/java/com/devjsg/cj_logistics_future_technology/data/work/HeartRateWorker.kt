package com.devjsg.cj_logistics_future_technology.data.work

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.network.NetworkConstants
import com.devjsg.cj_logistics_future_technology.di.worker.ChildWorkerFactory
import com.google.android.gms.location.LocationServices
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HeartRateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataStoreManager: DataStoreManager,
    private val httpClient: HttpClient
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val heartRateAvg = inputData.getInt(EXTRA_HEART_RATE_AVG, 0)

        val token = dataStoreManager.token.first()
        val heartRateThreshold = dataStoreManager.heartRateThreshold.first() ?: Int.MAX_VALUE
        Log.d("HeartRateWorker", "heartRateAvg: $heartRateAvg, heartRateThreshold: $heartRateThreshold")

        val requestBody = HeartRateRequest(heartRateAvg)

        if (heartRateAvg > heartRateThreshold) {
            val location = getCurrentLocation() ?: return Result.failure()

            val thresholdResponse = sendEmergencyAlarm(token!!, location.latitude, location.longitude)

            httpClient.post(NetworkConstants.BASE_URL + "heart-rate") {
                headers {
                    append("Authorization", "Bearer $token")
                    append("Content-Type", "application/json")
                }
                setBody(requestBody)
            }

            return if (thresholdResponse) {
                Result.success()
            } else {
                Result.failure()
            }
        }

        return try {
            val response = httpClient.post(NetworkConstants.BASE_URL + "heart-rate") {
                headers {
                    append("Authorization", "Bearer $token")
                    append("Content-Type", "application/json")
                }
                setBody(requestBody)
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

    @Serializable
    data class HeartRateRequest(val heartRate: Int)

    private suspend fun getCurrentLocation(): Location? {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
        return suspendCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                continuation.resume(location)
            }.addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
        }
    }

    private suspend fun sendEmergencyAlarm(
        token: String,
        latitude: Double,
        longitude: Double
    ): Boolean {
        return try {
            val response = httpClient.post(NetworkConstants.BASE_URL + "fcm/emergency-alarm") {
                headers {
                    append("Authorization", "Bearer $token")
                    append("Content-Type", "application/json")
                }
                url {
                    parameters.append("x", latitude.toString())
                    parameters.append("y", longitude.toString())
                    parameters.append("emergency", "HEART_RATE")
                }
            }

            response.status.value in 200..299
        } catch (e: Exception) {
            false
        }
    }

    @AssistedFactory
    interface Factory : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): HeartRateWorker
    }

    companion object {
        const val EXTRA_HEART_RATE_AVG =
            "com.devjsg.cj_logistics_future_technology.EXTRA_HEART_RATE_AVG"

        fun enqueueWork(context: Context, heartRateAvg: Int) {
            val workRequest = OneTimeWorkRequestBuilder<HeartRateWorker>()
                .setInputData(workDataOf(EXTRA_HEART_RATE_AVG to heartRateAvg))
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}