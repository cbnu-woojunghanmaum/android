package com.devjsg.cj_logistics_future_technology.data.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
data class Staff (
    val id: Int,
    val memberName: String,
    val moveWork: Int,
    val heartRate:Float,
    val km:Float,
    val createdAt: String,
    val isOverHeartRate: Boolean,
)

@Serializable
data class ContestResponse(
    val data: Data,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)

@Serializable
@Immutable
data class Data(
    val nowPage: Int,
    val allPageCount: Int,
    val value: List<Staff>,
    val elementCount: Int
)