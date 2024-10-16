package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.biometric.KeystoreHelper
import com.devjsg.cj_logistics_future_technology.data.model.SignUpRequest
import com.devjsg.cj_logistics_future_technology.di.util.decodeJwt
import com.devjsg.cj_logistics_future_technology.domain.usecase.CheckLoginIdUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.LoginUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.SignUpUseCase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val checkLoginIdUseCase: CheckLoginIdUseCase,
    private val loginUseCase: LoginUseCase,
    private val keystoreHelper: KeystoreHelper
) : ViewModel() {

    private val _phoneState = MutableStateFlow("")
    val phoneState: StateFlow<String> = _phoneState

    private val _isLoginIdValid = MutableStateFlow(false)
    val isLoginIdValid: StateFlow<Boolean> = _isLoginIdValid

    private val _loginIdMessage = MutableStateFlow("")
    val loginIdMessage: StateFlow<String> = _loginIdMessage

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun onPhoneChange(newPhone: String) {
        _phoneState.value = newPhone
    }

    fun checkLoginId(loginId: String) {
        if (loginId.length !in 4..8 || !loginId.matches("^[a-zA-Z0-9]*$".toRegex())) {
            _loginIdMessage.value = "아이디는 4~8자리의 영문자와 숫자로만 구성되어야 합니다."
            _isLoginIdValid.value = false
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = checkLoginIdUseCase(loginId)
                if (response.success) {
                    if (response.data) {
                        _loginIdMessage.value = "사용 가능한 아이디 입니다."
                        _isLoginIdValid.value = true
                    } else {
                        _loginIdMessage.value = "이미 존재하는 아이디 입니다."
                        _isLoginIdValid.value = false
                    }
                } else {
                    _loginIdMessage.value = "아이디 중복 확인에 실패했습니다."
                    _isLoginIdValid.value = false
                }
            } catch (e: Exception) {
                _loginIdMessage.value = "아이디는 4~8자리의 영문자와 숫자로만 구성되어야 합니다."
                _isLoginIdValid.value = false
            }
        }
    }

    fun signUp(
        loginId: String,
        password: String,
        phone: String,
        gender: String,
        email: String,
        employeeName: String,
        year: Int,
        month: Int,
        day: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val signUpRequest = SignUpRequest(
                    loginId = loginId,
                    password = password,
                    phone = phone,
                    gender = gender,
                    email = email,
                    employeeName = employeeName,
                    year = year,
                    month = month,
                    day = day
                )
                val response = signUpUseCase(signUpRequest)
                if (response.status.isSuccess()) {
                    onSuccess()
                } else {
                    _uiState.value = UiState.Error("Failed to sign up")
                    onError("Failed to sign up")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun login(
        id: String,
        password: String,
        onSuccess: (String?, String?) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val fcmToken = getFcmToken()
                Log.d("MemberViewModel", "FCM Token: $fcmToken")
                val response = loginUseCase(id, password, fcmToken)
                if (response.success) {
                    val loginData = "$id:$password"
                    keystoreHelper.saveLoginData(loginData)
                    val (sub, auth) = decodeJwt(response.data.token)
                    onSuccess(sub, auth)
                } else {
                    _uiState.value = UiState.Error("로그인에 실패했습니다. 아이디나 비밀번호를 다시 확인해주세요.")
                    onError("로그인에 실패했습니다. 아이디나 비밀번호를 다시 확인해주세요.")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
                onError(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun getFcmToken(): String {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("FCM 토큰을 가져오는 데 실패했습니다."))
                }
            }
        }
    }

    fun autoLogin(onSuccess: (String?, String?) -> Unit) {
        val loginData = keystoreHelper.getLoginData() ?: return
        val (id, password) = loginData.split(":")
        login(id, password, onSuccess, onError = { })
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object LoginIdValid : UiState()
        data class Error(val message: String) : UiState()
    }
}