package com.devjsg.cj_logistics_future_technology.data.repository

import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.HeartRateResponse
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MyHeartRateDataRepository @Inject constructor(
    private val apiService: MemberApiService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun getMyHeartRateData(start: String, end: String): HeartRateResponse {
        val token = dataStoreManager.token.first() ?: ""
        return apiService.getMyHeartRateData(token, start, end)
    }
}