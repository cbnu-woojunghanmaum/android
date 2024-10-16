package com.devjsg.cj_logistics_future_technology.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.Member
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import com.devjsg.cj_logistics_future_technology.data.source.pagingsource.MemberPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FindAllMemberRepository @Inject constructor(
    private val apiService: MemberApiService,
    private val dataStoreManager: DataStoreManager
) {
    fun getMembers(): Flow<PagingData<Member>> = flow {
        val token = dataStoreManager.token.first() ?: ""
        emitAll(
            Pager(
                config = PagingConfig(pageSize = 20, prefetchDistance = 2),
                pagingSourceFactory = { MemberPagingSource(apiService, token) }
            ).flow
        )
    }
}