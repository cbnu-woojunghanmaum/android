package com.devjsg.cj_logistics_future_technology.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HeartRateResponse(
    val data: List<HeartRateData>,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)

@Serializable
data class HeartRateData(
    val averageHeartRate: Float,
    val maxHeartRate: Int,
    val minHeartRate: Int,
    val dateTime: String
)