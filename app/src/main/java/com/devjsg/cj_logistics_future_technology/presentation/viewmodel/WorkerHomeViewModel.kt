package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.biometric.KeystoreHelper
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.HeartRateData
import com.devjsg.cj_logistics_future_technology.data.model.MyEmergencyReport
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetMyEmergencyReportsUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetMyHeartRateDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val keystoreHelper: KeystoreHelper,
    private val getMyEmergencyReportsUseCase: GetMyEmergencyReportsUseCase,
    private val getMyHeartRateDataUseCase: GetMyHeartRateDataUseCase,
) : ViewModel() {

    private val _myEmergencyReports = MutableStateFlow<List<MyEmergencyReport>>(emptyList())
    val myEmergencyReports: StateFlow<List<MyEmergencyReport>> = _myEmergencyReports

    private val _heartRateData = MutableStateFlow<List<HeartRateData>>(emptyList())
    val heartRateData: StateFlow<List<HeartRateData>> = _heartRateData

    fun loadEmergencyReports(start: String, end: String) {
        viewModelScope.launch {
            val reports = getMyEmergencyReportsUseCase(start, end)
            _myEmergencyReports.value = reports
        }
    }

    fun getHeartRateData(start: String, end: String) {
        viewModelScope.launch {
            val response = getMyHeartRateDataUseCase(start, end)
            if (response.success) {
                _heartRateData.value = response.data
            } else {
                // Handle error
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
}