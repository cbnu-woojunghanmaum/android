package com.devjsg.cj_logistics_future_technology.data.repository

import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.ContestResponse
import com.devjsg.cj_logistics_future_technology.data.model.ContestSearchResponse
import com.devjsg.cj_logistics_future_technology.data.model.HeartRateResponse
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AdminMemberRepository @Inject constructor(
    private val apiService: MemberApiService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun getHeartRateData(memberId: Int, start: String, end: String): HeartRateResponse {
        val token = dataStoreManager.token.first() ?: ""
        return apiService.getHeartRateData(token, memberId, start, end)
    }

    suspend fun getStaff(
        token: String,
        page: Int,
        offset: Int,
        sortings: List<String>,
        reportCondition: String
    ): ContestResponse {
        return apiService.getStaff(
            token = token,
            page = page,
            offset = offset,
            sortings = sortings,
            reportCondition = reportCondition
        )
    }

    suspend fun searchStaff(token: String, name: String): ContestSearchResponse {
        return apiService.searchStaff(token, name)
    }
}