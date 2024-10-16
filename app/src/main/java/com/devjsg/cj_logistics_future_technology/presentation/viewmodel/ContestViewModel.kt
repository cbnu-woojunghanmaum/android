package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.Staff
import com.devjsg.cj_logistics_future_technology.data.repository.AdminMemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContestHomeViewModel @Inject constructor(
    private val adminMemberRepository: AdminMemberRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _staffList = MutableStateFlow<List<Staff>>(emptyList())
    val staffList: StateFlow<List<Staff>> = _staffList

    var currentPage = mutableStateOf(1)
    var listSize = mutableStateOf(10)
    var totalPages = mutableStateOf(0)
    var moveSortOrder = mutableStateOf("NONE")
    var kmSortOrder = mutableStateOf("NONE")
    var heartRateSortOrder = mutableStateOf("NONE")
    var reportCondition = mutableStateOf("NONE")

    var searchQuery = mutableStateOf("")

    fun applySortingAndFiltering() {
        val sortings = mutableListOf<String>()
        if (moveSortOrder.value != "NONE") sortings.add(moveSortOrder.value)
        if (kmSortOrder.value != "NONE") sortings.add(kmSortOrder.value)
        if (heartRateSortOrder.value != "NONE") sortings.add(heartRateSortOrder.value)

        viewModelScope.launch {
            val token = dataStoreManager.token.first()
            if (token != null) {
                val response = adminMemberRepository.getStaff(
                    token = token,
                    page = currentPage.value,
                    offset = listSize.value,
                    sortings = sortings.ifEmpty { listOf("NONE") },
                    reportCondition = reportCondition.value
                )
                _staffList.value = response.data.value

                totalPages.value = response.data.allPageCount

                if (currentPage.value > totalPages.value) {
                    currentPage.value = totalPages.value
                }
            }
        }
    }

    fun searchStaffByName(name: String) {
        viewModelScope.launch {
            val token = dataStoreManager.token.first()
            if (token != null) {
                val response = adminMemberRepository.searchStaff(token, name)
                _staffList.value = response.data.map { searchStaff ->
                    Staff(
                        id = searchStaff.id,
                        memberName = searchStaff.memberName,
                        moveWork = searchStaff.moveWork,
                        heartRate = searchStaff.heartRate,
                        km = searchStaff.km,
                        createdAt = searchStaff.createdAt,
                        isOverHeartRate = searchStaff.isOverHeartRate
                    )
                }
                totalPages.value = 1
                currentPage.value = 1
            }
        }
    }

    fun updateListSize(newSize: Int) {
        listSize.value = newSize
        applySortingAndFiltering()
    }

    fun toggleMoveSortOrder() {
        moveSortOrder.value = when (moveSortOrder.value) {
            "NONE" -> "MOVE_DESC"
            "MOVE_DESC" -> "MOVE_ASC"
            else -> "NONE"
        }
    }

    fun toggleKmSortOrder() {
        kmSortOrder.value = when (kmSortOrder.value) {
            "NONE" -> "KM_DESC"
            "KM_DESC" -> "KM_ASC"
            else -> "NONE"
        }
    }

    fun toggleHeartRateSortOrder() {
        heartRateSortOrder.value = when (heartRateSortOrder.value) {
            "NONE" -> "HEART_RATE_DESC"
            "HEART_RATE_DESC" -> "HEART_RATE_ASC"
            else -> "NONE"
        }
    }

    fun updateReportCondition(condition: String) {
        reportCondition.value = condition
    }

    fun resetSortingAndFiltering() {
        moveSortOrder.value = "NONE"
        kmSortOrder.value = "NONE"
        heartRateSortOrder.value = "NONE"
        reportCondition.value = "NONE"
        applySortingAndFiltering()
    }
}