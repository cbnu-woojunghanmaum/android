package com.devjsg.cj_logistics_future_technology.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val data: LoginData,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)

@Serializable
data class LoginData(
    val token: String,
    val refreshToken: String
)

@Serializable
data class LoginRequest(
    val loginId: String,
    val password: String,
    val token: String
)