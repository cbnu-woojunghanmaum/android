package com.devjsg.cj_logistics_future_technology.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckLoginIdResponse(
    val data: Boolean,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)