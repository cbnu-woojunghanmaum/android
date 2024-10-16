package com.devjsg.cj_logistics_future_technology.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MyEmergencyReport(
    val id: Int,
    val createdAt: String,
    val reporter: String,
    val x: Double,
    val y: Double,
    val emergency: String,
    val loginId: String,
    val phone: String
)

@Serializable
data class MyEmergencyReportResponse(
    val data: List<MyEmergencyReport>,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)
