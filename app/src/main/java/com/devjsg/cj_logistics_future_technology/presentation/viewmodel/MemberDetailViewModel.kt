package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.model.EmergencyReport
import com.devjsg.cj_logistics_future_technology.data.model.HeartRateData
import com.devjsg.cj_logistics_future_technology.data.model.MemberInfo
import com.devjsg.cj_logistics_future_technology.domain.usecase.EmergencyReportUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetHeartRateDataUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetMemberInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberDetailViewModel @Inject constructor(
    private val getHeartRateDataUseCase: GetHeartRateDataUseCase,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val getMemberEmergencyReportsUseCase: EmergencyReportUseCase
) : ViewModel() {
    private val _selectedMember = MutableStateFlow<MemberInfo?>(null)
    val selectedMember: StateFlow<MemberInfo?> = _selectedMember

    private val _heartRateData = MutableStateFlow<List<HeartRateData>>(emptyList())
    val heartRateData: StateFlow<List<HeartRateData>> = _heartRateData

    private val _memberEmergencyReports = MutableStateFlow<List<EmergencyReport>>(emptyList())
    val memberEmergencyReports: StateFlow<List<EmergencyReport>> = _memberEmergencyReports

    fun getMemberInfo(memberId: String) {
        viewModelScope.launch {
            val response = getMemberInfoUseCase(memberId)
            _selectedMember.value = response.data
        }
    }

    fun getHeartRateData(memberId: Int, start: String, end: String) {
        viewModelScope.launch {
            val response = getHeartRateDataUseCase(memberId, start, end)
            if (response.success) {
                _heartRateData.value = response.data
            } else {
                // Handle error
            }
        }
    }

    fun loadEmergencyReports(memberId: Int, start: String, end: String) {
        viewModelScope.launch {
            val reports = getMemberEmergencyReportsUseCase(memberId, start, end)
            if (reports.success){
                _memberEmergencyReports.value = reports.data
            } else {
                // Handle error
            }
        }
    }
}