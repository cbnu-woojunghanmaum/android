package com.devjsg.cj_logistics_future_technology.data.repository

import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.MyEmergencyReport
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyEmergencyReportRepository @Inject constructor(
    private val apiService: MemberApiService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun getEmergencyReports(start: String, end: String): List<MyEmergencyReport> {
        val token = dataStoreManager.token.first() ?: ""
        val response = apiService.getEmergencyReports(token, start, end)
        return if (response.success) {
            response.data
        } else {
            emptyList()
        }
    }
}