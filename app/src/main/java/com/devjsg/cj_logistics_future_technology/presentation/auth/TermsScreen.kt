package com.devjsg.cj_logistics_future_technology.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(navController: NavController) {
    var isTermsChecked by remember { mutableStateOf(false) }
    var isPrivacyChecked by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (showToast) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "모든 약관에 동의해야 합니다.", Toast.LENGTH_LONG).show()
            showToast = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "이용 약관 동의",
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "필수 이용 약관 동의",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "제1조 (목적) 이 약관은 주식회사 스마트워커(이하 “회사”라 합니다)가 운영하는 스마트워커 “홈페이지”와 스마트워커 “애플리케이션”(이하 “홈페이지”와 “애플리케이션”을 “APP”이라고 합니다)의 서비스 이용 및 제공에 관한 제반 사항의 규정을 목적으로 합니다.\n" +
                                "\n" +
                                "제2조 (용어의 정의) ① 이 약관에서 사용하는 용어의 정의는 다음과 같습니다.\n" +
                                "\n" +
                                "“서비스”라 함은 구현되는 PC, 모바일 기기를 통하여 “이용자”가 이용할 수 있는 심박 측정 서비스 등 회사가 제공하는 제반 서비스를 의미합니다.\n" +
                                "\n" +
                                "“이용자”란 “APP”에 접속하여 이 약관에 따라 “APP”이 제공하는 서비스를 받는 회원 및 비회원을 말합니다.\n" +
                                "\n" +
                                "“회원”이란 “APP”에 개인정보를 제공하여 회원등록을 한 자로서, “APP”이 제공하는 서비스를 이용하는 자를 말합니다.\n" +
                                "\n" +
                                "“모바일 기기”란 콘텐츠를 다운로드 받거나 설치하여 사용할 수 있는 기기로서, 휴대폰, 스마트폰, 휴대정보단말기(PDA), 태블릿 등을 의미합니다.\n" +
                                "\n" +
                                "“계정정보”란 회원의 회원번호와 내보험다보여 등 외부계정정보, 기기정보 등 회원이 회사에 제공한 정보를 의미합니다.\n" +
                                "\n" +
                                "“애플리케이션”이란 회사가 제공하는 서비스를 이용하기 위하여 모바일 기기를 통해 다운로드 받거나 설치하여 사용하는 프로그램 일체를 의미합니다.\n" +
                                "\n" +
                                "② 이 약관에서 사용하는 용어의 정의는 본 조 제1항에서 정하는 것을 제외하고는 관계법령 및 서비스별 정책에서 정하는 바에 의하며, 이에 정하지 아니한 것은 일반적인 상 관례에 따릅니다.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Checkbox(
                        checked = isTermsChecked,
                        onCheckedChange = { isChecked -> isTermsChecked = isChecked },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Red,
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                    Text(text = "필수 이용 약관에 동의합니다.")
                }

                Text(
                    text = "개인 정보 수집 동의",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color.Gray)
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "제1조 (목적) 이 약관은 주식회사 스마트워커(이하 “회사”라 합니다)가 운영하는 스마트워커 “홈페이지”와 스마트워커 “애플리케이션”(이하 “홈페이지”와 “애플리케이션”을 “APP”이라고 합니다)의 서비스 이용 및 제공에 관한 제반 사항의 규정을 목적으로 합니다.\n" +
                                "\n" +
                                "제2조 (용어의 정의) ① 이 약관에서 사용하는 용어의 정의는 다음과 같습니다.\n" +
                                "\n" +
                                "“서비스”라 함은 구현되는 PC, 모바일 기기를 통하여 “이용자”가 이용할 수 있는 심박 측정 서비스 등 회사가 제공하는 제반 서비스를 의미합니다.\n" +
                                "\n" +
                                "“이용자”란 “APP”에 접속하여 이 약관에 따라 “APP”이 제공하는 서비스를 받는 회원 및 비회원을 말합니다.\n" +
                                "\n" +
                                "“회원”이란 “APP”에 개인정보를 제공하여 회원등록을 한 자로서, “APP”이 제공하는 서비스를 이용하는 자를 말합니다.\n" +
                                "\n" +
                                "“모바일 기기”란 콘텐츠를 다운로드 받거나 설치하여 사용할 수 있는 기기로서, 휴대폰, 스마트폰, 휴대정보단말기(PDA), 태블릿 등을 의미합니다.\n" +
                                "\n" +
                                "“계정정보”란 회원의 회원번호와 내보험다보여 등 외부계정정보, 기기정보 등 회원이 회사에 제공한 정보를 의미합니다.\n" +
                                "\n" +
                                "“애플리케이션”이란 회사가 제공하는 서비스를 이용하기 위하여 모바일 기기를 통해 다운로드 받거나 설치하여 사용하는 프로그램 일체를 의미합니다.\n" +
                                "\n" +
                                "② 이 약관에서 사용하는 용어의 정의는 본 조 제1항에서 정하는 것을 제외하고는 관계법령 및 서비스별 정책에서 정하는 바에 의하며, 이에 정하지 아니한 것은 일반적인 상 관례에 따릅니다.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Checkbox(
                        checked = isPrivacyChecked,
                        onCheckedChange = { isChecked -> isPrivacyChecked = isChecked },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Red,
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                    Text(text = "개인 정보 수집에 동의합니다.")
                }

                Button(
                    onClick = {
                        if (isTermsChecked && isPrivacyChecked) {
                            navController.navigate("sign_up")
                        } else {
                            showToast = true
                        }
                    },
                    enabled = isTermsChecked && isPrivacyChecked,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTermsChecked && isPrivacyChecked) Color.Red else Color.Gray
                    )
                ) {
                    Text(
                        text = "다음 진행",
                        color = Color.White,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    )
}