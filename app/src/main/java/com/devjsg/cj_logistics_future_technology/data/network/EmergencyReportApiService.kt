package com.devjsg.cj_logistics_future_technology.data.network

import com.devjsg.cj_logistics_future_technology.data.model.ApiResponse
import com.devjsg.cj_logistics_future_technology.data.model.EmergencyReport
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.url

interface EmergencyReportApiService {
    suspend fun searchReportsWithDate(loginId: String, start: String, end: String, token: String): ApiResponse<List<EmergencyReport>>
    suspend fun getReportsWithDate(start: String, end: String, token: String): ApiResponse<List<EmergencyReport>>
    suspend fun searchReports(loginId: String, token: String): ApiResponse<List<EmergencyReport>>

    suspend fun memberEmergencyReports(memberId: Int, start: String, end: String, token: String): ApiResponse<List<EmergencyReport>>
}

class EmergencyReportApiServiceImpl(private val httpClient: HttpClient) : EmergencyReportApiService {
    override suspend fun searchReportsWithDate(loginId: String, start: String, end: String, token: String): ApiResponse<List<EmergencyReport>> {
        return httpClient.get {
            url("${NetworkConstants.BASE_URL}fcm/admin/emergency-report/search-with-date")
            parameter("loginId", loginId)
            parameter("start", start)
            parameter("end", end)
            header("Authorization", "Bearer $token")
        }.body()
    }

    override suspend fun getReportsWithDate(start: String, end: String, token: String): ApiResponse<List<EmergencyReport>> {
        return httpClient.get {
            url("${NetworkConstants.BASE_URL}fcm/admin/emergency-report")
            parameter("start", start)
            parameter("end", end)
            header("Authorization", "Bearer $token")
        }.body()
    }

    override suspend fun searchReports(loginId: String, token: String): ApiResponse<List<EmergencyReport>> {
        return httpClient.get {
            url("${NetworkConstants.BASE_URL}fcm/admin/emergency-report/search")
            parameter("loginId", loginId)
            header("Authorization", "Bearer $token")
        }.body()
    }

    override suspend fun memberEmergencyReports(
        memberId: Int,
        start: String,
        end: String,
        token: String
    ): ApiResponse<List<EmergencyReport>> {
        return httpClient.get {
            url("${NetworkConstants.BASE_URL}fcm/admin/emergency-report/$memberId")
            parameter("start", start)
            parameter("end", end)
            header("Authorization", "Bearer $token")
        }.body()
    }
}