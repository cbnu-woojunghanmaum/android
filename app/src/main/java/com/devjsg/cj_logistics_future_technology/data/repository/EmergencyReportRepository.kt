package com.devjsg.cj_logistics_future_technology.data.repository

import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.ApiResponse
import com.devjsg.cj_logistics_future_technology.data.model.EmergencyReport
import com.devjsg.cj_logistics_future_technology.data.network.EmergencyReportApiService
import kotlinx.coroutines.flow.first

class EmergencyReportRepository(
    private val apiService: EmergencyReportApiService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun searchReportsWithDate(loginId: String, start: String, end: String): ApiResponse<List<EmergencyReport>> {
        val token = dataStoreManager.token.first() ?: ""
        return apiService.searchReportsWithDate(loginId, start, end, token)
    }

    suspend fun getReportsWithDate(start: String, end: String): ApiResponse<List<EmergencyReport>> {
        val token = dataStoreManager.token.first() ?: ""
        return apiService.getReportsWithDate(start, end, token)
    }

    suspend fun searchReports(loginId: String): ApiResponse<List<EmergencyReport>> {
        val token = dataStoreManager.token.first() ?: ""
        return apiService.searchReports(loginId, token)
    }

    suspend fun getEmergencyReports(memberId: Int, start: String, end: String): ApiResponse<List<EmergencyReport>> {
        val token = dataStoreManager.token.first() ?: ""
        return apiService.memberEmergencyReports(memberId, start, end, token)
    }
}