package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.devjsg.cj_logistics_future_technology.data.biometric.KeystoreHelper
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.EditableMember
import com.devjsg.cj_logistics_future_technology.data.model.EmergencyReport
import com.devjsg.cj_logistics_future_technology.data.model.Member
import com.devjsg.cj_logistics_future_technology.data.model.MemberInfo
import com.devjsg.cj_logistics_future_technology.domain.usecase.EmergencyReportUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetAllMembersUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetMemberInfoUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.SearchMemberUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.UpdateMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getMembersUseCase: GetAllMembersUseCase,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val updateMemberUseCase: UpdateMemberUseCase,
    private val searchMemberUseCase: SearchMemberUseCase,
    private val dataStoreManager: DataStoreManager,
    private val keystoreHelper: KeystoreHelper,
    private val emergencyReportUseCase: EmergencyReportUseCase
) : ViewModel() {

    val members: Flow<PagingData<Member>> = getMembersUseCase()
        .cachedIn(viewModelScope)

    private val _selectedMember = MutableStateFlow<EditableMember?>(null)
    val selectedMember: StateFlow<EditableMember?> = _selectedMember

    private val _searchResult = MutableStateFlow<List<Member>?>(null)
    val searchResult: StateFlow<List<Member>?> = _searchResult

    private var _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage

    private val _emergencyReports = MutableStateFlow<List<EmergencyReport>?>(null)
    val emergencyReports: StateFlow<List<EmergencyReport>?> = _emergencyReports

    fun getMemberInfo(memberId: String) {
        viewModelScope.launch {
            val response = getMemberInfoUseCase(memberId)
            _selectedMember.value = response.data.toEditableMember()
        }
    }

    fun searchMembers(loginId: String) {
        viewModelScope.launch {
            try {
                val response = searchMemberUseCase(loginId)
                _searchResult.value = response.data.value
            } catch (e: Exception) {
                _snackBarMessage.value = "검색에 실패했습니다. 아이디를 확인해주세요."
            }
        }
    }

    fun updateMember(member: EditableMember, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val response = updateMemberUseCase(member)
                response.headers["Authorization"]?.let { token ->
                    response.headers["Refresh-Token"]?.let { refreshToken ->
                        dataStoreManager.saveToken(token, refreshToken)
                    }
                }
                dataStoreManager.saveHeartRateThreshold(member.heartRateThreshold)
                getMembersUseCase()
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun searchEmergencyReports(loginId: String, start: String, end: String) {
        viewModelScope.launch {
            try {
                val response = emergencyReportUseCase.searchReportsWithDate(loginId, start, end)
                _emergencyReports.value = response.data
            } catch (e: Exception) {
                _snackBarMessage.value = "신고 이력 조회에 실패했습니다."
            }
        }
    }

    fun getEmergencyReportsWithDate(start: String, end: String) {
        viewModelScope.launch {
            try {
                val response = emergencyReportUseCase.getReportsWithDate(start, end)
                _emergencyReports.value = response.data
            } catch (e: Exception) {
                _snackBarMessage.value = "신고 이력 조회에 실패했습니다."
            }
        }
    }

    fun searchEmergencyReports(loginId: String) {
        viewModelScope.launch {
            try {
                val response = emergencyReportUseCase.searchReports(loginId)
                _emergencyReports.value = response.data
            } catch (e: Exception) {
                _snackBarMessage.value = "신고 이력 조회에 실패했습니다."
            }
        }
    }


    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            dataStoreManager.clearTokens()
            dataStoreManager.clearHeaderData()
            onLogoutComplete()
        }
    }

    private fun MemberInfo.toEditableMember(): EditableMember {
        return EditableMember(
            memberId = this.memberId,
            employeeName = this.employeeName,
            phone = this.phone,
            gender = this.gender,
            email = this.email,
            authority = this.authorities.firstOrNull() ?: "",
            year = this.year,
            month = this.month,
            day = this.day,
            heartRateThreshold = this.heartRateThreshold
        )
    }
}