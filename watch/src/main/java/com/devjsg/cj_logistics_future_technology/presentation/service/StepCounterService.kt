package com.devjsg.cj_logistics_future_technology.presentation.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.devjsg.cj_logistics_future_technology.R
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StepCounterService : Service(), SensorEventListener {

    @Inject
    lateinit var sensorManager: SensorManager

    private var stepCounterSensor: Sensor? = null
    private var stepCount: Int = 0

    companion object {
        private const val NOTIFICATION_ID = 1
    }

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        stepCounterSensor?.also { stepSensor ->
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        handler.postDelayed(stepRunnable, 5000)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val stepRunnable = object : Runnable {
        override fun run() {
            sendStepCountToPhone(stepCount)
            stepCount = 0
            handler.postDelayed(this, 5000)
        }
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "STEP_COUNTER_SERVICE_CHANNEL"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Step Counter Service",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Step Counter Service")
            .setContentText("Monitoring your steps")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private suspend fun getConnectedNodeId(): String? = withContext(Dispatchers.IO) {
        val nodeClient: NodeClient = Wearable.getNodeClient(this@StepCounterService)
        val nodes = try {
            Tasks.await(nodeClient.connectedNodes)
        } catch (e: Exception) {
            Log.e("StepCounterService", "Failed to get connected nodes", e)
            return@withContext null
        }

        if (nodes.isEmpty()) {
            Log.e("StepCounterService", "No connected nodes found")
            return@withContext null
        }

        nodes.firstOrNull()?.id.also {
            Log.d("StepCounterService", "Connected node found: $it")
        }
    }

    private fun sendStepCountToPhone(stepCount: Int) {
        val messageClient: MessageClient = Wearable.getMessageClient(this)

        CoroutineScope(Dispatchers.IO).launch {
            val nodeId = getConnectedNodeId() ?: return@launch
            val payload = stepCount.toString().toByteArray()

            messageClient.sendMessage(nodeId, "/step_counter", payload)
                .addOnSuccessListener {
                    Log.d("StepCounterService", "Step count sent successfully, $stepCount")
                }
                .addOnFailureListener {
                    Log.e("StepCounterService", "Failed to send step count")
                }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        handler.removeCallbacks(stepRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}