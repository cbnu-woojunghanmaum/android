package com.devjsg.cj_logistics_future_technology.presentation.login

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.data.biometric.BiometricPromptHelper
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MemberViewModel = hiltViewModel(),
    biometricPromptHelper: BiometricPromptHelper
) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    var isBiometricPromptShown by remember { mutableStateOf(false) }

    val window = (context as? Activity)?.window
    window?.statusBarColor = Color.White.toArgb()
    val insetsController = window?.let { WindowCompat.getInsetsController(it, window.decorView) }
    insetsController?.isAppearanceLightStatusBars = true

    fun showBiometricPrompt() {
        isBiometricPromptShown = true
        biometricPromptHelper.authenticate(
            onSuccess = {
                isBiometricPromptShown = false
                viewModel.autoLogin { sub, auth ->
                    coroutineScope.launch {
                        Toast.makeText(context, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    if (sub == "1" || auth == "ROLE_ADMIN") {
                        navController.navigate("admin_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("worker_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            },
            onError = { errorMessage ->
                isBiometricPromptShown = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("인증이 취소되었습니다.")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 88.dp, bottom = 16.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_smartworker_main_logo),
                    contentDescription = "CJ Logo",
                    modifier = Modifier
                        .width(180.dp)
                        .height(180.dp)
                )
            }
        },
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 16.dp, bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                IdOutlinedTextField(
                    id = id,
                    onIdChange = { id = it },
                    onShowBiometricPrompt = { showBiometricPrompt() },
                    isBiometricPromptShown = isBiometricPromptShown
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordOutlinedTextField(
                    password = password,
                    onPasswordChange = { password = it },
                    passwordVisible = passwordVisible,
                    onPasswordVisibleChange = { passwordVisible = !passwordVisible },
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "회원이 아니신가요?", style = TextStyle(fontSize = 16.sp))

                    TextButton(
                        onClick = { navController.navigate("terms") },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF006ECD))
                    ) {
                        Text("회원가입")
                    }
                }

                if (uiState is MemberViewModel.UiState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(94.dp)
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    shape = RoundedCornerShape(size = 8.dp),
                    onClick = {
                        viewModel.login(id, password, onSuccess = { sub, auth ->
                            coroutineScope.launch {
                                Toast.makeText(context, "로그인에 성공했습니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            if (sub == "1" || auth == "ROLE_ADMIN") {
                                navController.navigate("admin_home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                navController.navigate("worker_home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }, onError = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("로그인에 실패했습니다. 아이디나 비밀번호를 다시 확인해주세요.")
                            }
                        })
                    }
                ) {
                    Text(
                        "로그인",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(700),
                            color = Color.White,
                        )
                    )
                }
            }
        }
    )

    LaunchedEffect(uiState) {
        when (uiState) {
            is MemberViewModel.UiState.Error -> {
                val errorMessage = (uiState as MemberViewModel.UiState.Error).message
                snackbarHostState.showSnackbar(errorMessage)
            }

            else -> {}
        }
    }
}