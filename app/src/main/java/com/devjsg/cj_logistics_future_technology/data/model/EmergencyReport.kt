package com.devjsg.cj_logistics_future_technology.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)

@Serializable
data class EmergencyReport(
    val id: Int,
    val createdAt: String,
    val reporter: String,
    val reporterId: Int,
    val x: Double,
    val y: Double,
    val emergency: String,
    val loginId: String,
    val phone: String
)