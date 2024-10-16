package com.devjsg.cj_logistics_future_technology.domain.usecase

import androidx.paging.PagingData
import com.devjsg.cj_logistics_future_technology.data.model.Member
import com.devjsg.cj_logistics_future_technology.data.repository.FindAllMemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMembersUseCase @Inject constructor(
    private val repository: FindAllMemberRepository
) {
    operator fun invoke(): Flow<PagingData<Member>> {
        return repository.getMembers()
    }
}