package com.devjsg.cj_logistics_future_technology.data.repository

import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.MemberResponse
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchMemberRepository @Inject constructor(
    private val apiService: MemberApiService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun searchMember(loginId: String): MemberResponse {
        val token = dataStoreManager.token.first() ?: ""
        return apiService.searchMember(token, loginId)
    }
}