package com.devjsg.cj_logistics_future_technology.domain.usecase

import com.devjsg.cj_logistics_future_technology.data.model.HeartRateResponse
import com.devjsg.cj_logistics_future_technology.data.repository.MyHeartRateDataRepository
import javax.inject.Inject

class GetMyHeartRateDataUseCase @Inject constructor(
    private val repository: MyHeartRateDataRepository
) {
    suspend operator fun invoke(start: String, end: String): HeartRateResponse {
        return repository.getMyHeartRateData(start, end)
    }
}