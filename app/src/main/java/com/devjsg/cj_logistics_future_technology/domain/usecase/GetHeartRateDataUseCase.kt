package com.devjsg.cj_logistics_future_technology.domain.usecase

import com.devjsg.cj_logistics_future_technology.data.model.HeartRateResponse
import com.devjsg.cj_logistics_future_technology.data.repository.AdminMemberRepository
import javax.inject.Inject

class GetHeartRateDataUseCase @Inject constructor(
    private val repository: AdminMemberRepository
) {
    suspend operator fun invoke(memberId: Int, start: String, end: String): HeartRateResponse {
        return repository.getHeartRateData(memberId, start, end)
    }
}