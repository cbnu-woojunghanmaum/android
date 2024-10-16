package com.devjsg.cj_logistics_future_technology.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val loginId: String,
    val password: String,
    val phone: String,
    val gender: String,
    val email: String,
    val employeeName: String,
    val year: Int,
    val month: Int,
    val day: Int
)