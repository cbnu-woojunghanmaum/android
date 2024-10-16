package com.devjsg.cj_logistics_future_technology.domain.usecase

import com.devjsg.cj_logistics_future_technology.data.model.EditableMember
import com.devjsg.cj_logistics_future_technology.data.repository.ManageMemberRepository
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class UpdateMemberUseCase @Inject constructor(
    private val repository: ManageMemberRepository
) {
    suspend operator fun invoke(member: EditableMember): HttpResponse {
        return repository.updateMember(member)
    }
}