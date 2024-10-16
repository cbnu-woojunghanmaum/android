package com.devjsg.cj_logistics_future_technology.domain.usecase

import com.devjsg.cj_logistics_future_technology.data.model.MemberInfoResponse
import com.devjsg.cj_logistics_future_technology.data.repository.ManageMemberRepository
import javax.inject.Inject

class GetMemberInfoUseCase @Inject constructor(
    private val repository: ManageMemberRepository
) {
    suspend operator fun invoke(memberId: String): MemberInfoResponse {
        return repository.getMemberInfo(memberId)
    }
}