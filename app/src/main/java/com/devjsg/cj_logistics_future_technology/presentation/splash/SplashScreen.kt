package com.devjsg.cj_logistics_future_technology.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.di.util.decodeJwt
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    var isLoading by remember { mutableStateOf(true) }
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = null)

    LaunchedEffect(isLoggedIn) {
        delay(1000)
        if (isLoggedIn != null) {
            val (decodedSub, auth) = decodeJwt(isLoggedIn!!)
            if (decodedSub == "1" || auth == "ROLE_ADMIN") {
                //대회 평가를 위해 관리자 홈이 아닌, 로그인 화면으로 초기 세팅
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                //대회 평가를 위해 근로자 홈이 아닌, 로그인 화면으로 초기 세팅
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            isLoading = false
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
            isLoading = false
        }
    }

    var loadingText by remember { mutableStateOf("서버에 접속 중입니다") }

    LaunchedEffect(Unit) {
        while (isLoading) {
            loadingText = when (loadingText) {
                "서버에 접속 중입니다" -> "서버에 접속 중입니다."
                "서버에 접속 중입니다." -> "서버에 접속 중입니다.."
                "서버에 접속 중입니다.." -> "서버에 접속 중입니다..."
                else -> "서버에 접속 중입니다"
            }
            delay(300)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 32.dp, horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_cj_logistics),
            contentDescription = "CJ Logistics Icon",
            modifier = Modifier
                .width(128.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "스마트워커와 함께하는 \n안전한 건강 관리 생활",
            style = TextStyle(
                fontSize = 32.sp,
                lineHeight = 42.sp,
                fontWeight = FontWeight(700)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "CJ대한통운에서 제공하는 물류 현장 근로 \n보조 서비스에 오신 것을 진심으로 환영합니다.",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF6F6F6F)
            )
        )
        Spacer(modifier = Modifier.height(54.dp))
        Text(
            text = loadingText,
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFFA5A5A5)
            )
        )
    }
}