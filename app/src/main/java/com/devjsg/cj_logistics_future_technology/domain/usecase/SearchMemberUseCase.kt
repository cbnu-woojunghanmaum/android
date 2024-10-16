package com.devjsg.cj_logistics_future_technology.domain.usecase

import com.devjsg.cj_logistics_future_technology.data.model.MemberResponse
import com.devjsg.cj_logistics_future_technology.data.repository.SearchMemberRepository
import javax.inject.Inject

class SearchMemberUseCase @Inject constructor(
    private val repository: SearchMemberRepository
) {
    suspend operator fun invoke(loginId: String): MemberResponse {
        return repository.searchMember(loginId)
    }
}