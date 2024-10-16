package com.devjsg.cj_logistics_future_technology.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchStaff (
    val id: Int,
    val memberName: String,
    val moveWork: Int,
    val heartRate:Float,
    val km:Float,
    val createdAt: String,
    val isOverHeartRate: Boolean,
)

@Serializable
data class ContestSearchResponse(
    val data: List<SearchStaff>,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)