package com.devjsg.cj_logistics_future_technology.domain.usecase

import com.devjsg.cj_logistics_future_technology.data.model.SignUpRequest
import com.devjsg.cj_logistics_future_technology.data.repository.MemberRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(signUpRequest: SignUpRequest) = repository.signUp(signUpRequest)
}