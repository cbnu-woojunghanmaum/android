package com.devjsg.cj_logistics_future_technology.domain.usecase

import com.devjsg.cj_logistics_future_technology.data.repository.MemberRepository
import javax.inject.Inject

class CheckLoginIdUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(loginId: String) = repository.checkLoginId(loginId)
}