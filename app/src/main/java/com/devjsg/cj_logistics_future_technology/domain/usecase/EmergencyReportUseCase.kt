package com.devjsg.cj_logistics_future_technology.domain.usecase

import com.devjsg.cj_logistics_future_technology.data.model.ApiResponse
import com.devjsg.cj_logistics_future_technology.data.model.EmergencyReport
import com.devjsg.cj_logistics_future_technology.data.repository.EmergencyReportRepository
import javax.inject.Inject

class EmergencyReportUseCase @Inject constructor(
    private val repository: EmergencyReportRepository
) {
    suspend fun searchReportsWithDate(
        loginId: String,
        start: String,
        end: String
    ): ApiResponse<List<EmergencyReport>> {
        return repository.searchReportsWithDate(loginId, start, end)
    }

    suspend fun getReportsWithDate(start: String, end: String): ApiResponse<List<EmergencyReport>> {
        return repository.getReportsWithDate(start, end)
    }

    suspend fun searchReports(loginId: String): ApiResponse<List<EmergencyReport>> {
        return repository.searchReports(loginId)
    }

    suspend operator fun invoke(
        memberId: Int,
        start: String,
        end: String
    ): ApiResponse<List<EmergencyReport>> {
        return repository.getEmergencyReports(memberId, start, end)
    }
}