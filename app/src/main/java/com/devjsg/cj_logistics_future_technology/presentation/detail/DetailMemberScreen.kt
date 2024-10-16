package com.devjsg.cj_logistics_future_technology.presentation.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
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
import com.devjsg.cj_logistics_future_technology.presentation.detail.component.HeartRateChart
import com.devjsg.cj_logistics_future_technology.presentation.detail.component.MemberEmergencyReportItem
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MemberDetailViewModel
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMemberScreen(
    navController: NavController,
    memberId: Int,
    viewModel: MemberDetailViewModel = hiltViewModel()
) {
    val heartRateData by viewModel.heartRateData.collectAsState()
    var selectedOption by remember { mutableStateOf("전체") }

    val options = listOf("전체", "하루", "7일", "30일", "60일", "90일")
    val context = LocalContext.current

    LaunchedEffect(memberId) {
        viewModel.getMemberInfo(memberId.toString())
    }

    LaunchedEffect(selectedOption) {
        val (start, end) = getDateTimeForOption(selectedOption)
        viewModel.getHeartRateData(memberId, start, end)
    }

    LaunchedEffect(selectedOption) {
        val (start, end) = getDateForOption(selectedOption)
        viewModel.loadEmergencyReports(memberId, start, end)
    }

    val memberEmergencyReports by viewModel.memberEmergencyReports.collectAsState()
    val selectedMember by viewModel.selectedMember.collectAsState()

    val iconRes = when (selectedMember?.gender) {
        "MALE" -> R.drawable.ic_man_face
        "FEMALE" -> R.drawable.ic_woman_face
        else -> R.drawable.ic_man_face
    }

    val window = (context as? Activity)?.window
    window?.statusBarColor = Color(0xFFF7F7F7).toArgb()
    val insetsController = window?.let { WindowCompat.getInsetsController(it, window.decorView) }
    insetsController?.isAppearanceLightStatusBars = true

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("회원 상세 정보") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF7F7F7)
                ),
            )
        },
        containerColor = Color(0xFFF7F7F7),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(options.size) { index ->
                        val option = options[index]
                        Box(
                            modifier = Modifier
                                .width(68.dp)
                                .height(40.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (selectedOption == option) Color.Black else Color(
                                        0xFFDFDFDF
                                    ),
                                    shape = RoundedCornerShape(size = 8.dp)
                                )
                                .background(
                                    color = if (selectedOption == option) Color.Black else Color(
                                        0xFFF7F7F7
                                    ),
                                    shape = RoundedCornerShape(size = 8.dp)
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures(onTap = {
                                        selectedOption = option
                                    })
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                color = if (selectedOption == option) Color.White else Color.Black
                            )
                        }
                    }
                }

                selectedMember?.let { member ->
                    val roleText = when (member.authorities[0]) {
                        "ADMIN" -> "관리자"
                        "EMPLOYEE" -> "근로자"
                        else -> "알 수 없음"
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Member Icon",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = member.employeeName,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .width(1.dp)
                                    .height(8.dp)
                                    .background(Color(0xFFDFDFDF))
                            )

                            Text(
                                text = member.loginId,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = member.phone)
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .width(1.dp)
                                    .height(8.dp)
                                    .background(Color(0xFFDFDFDF))
                            )
                            Text(text = "${member.year}년 ${member.month}월 ${member.day}일")
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .width(1.dp)
                                    .height(8.dp)
                                    .background(Color(0xFFDFDFDF))
                            )
                            Text(text = roleText)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(168.dp)
                        .padding(13.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(16.dp))
                ) {
                    HeartRateChart(heartRateData)
                }

                Text(
                    text = "신고 이력 조회",
                    modifier = Modifier.padding(
                        top = 32.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF242424),
                    )
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    items(memberEmergencyReports) { report ->
                        MemberEmergencyReportItem(report)
                    }
                }
            }
        }
    )
}

private fun getDateForOption(option: String): Pair<String, String> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val endDate = Date()
    val startDate = Calendar.getInstance()

    when (option) {
        "하루" -> startDate.add(Calendar.DAY_OF_YEAR, -1)
        "7일" -> startDate.add(Calendar.DAY_OF_YEAR, -7)
        "30일" -> startDate.add(Calendar.DAY_OF_YEAR, -30)
        "60일" -> startDate.add(Calendar.DAY_OF_YEAR, -60)
        "90일" -> startDate.add(Calendar.DAY_OF_YEAR, -90)
        "전체" -> {
            startDate.set(1900, Calendar.JANUARY, 1)
        }
    }

    return dateFormat.format(startDate.time) to dateFormat.format(endDate)
}

private fun getDateTimeForOption(option: String): Pair<String, String> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:00:ss", Locale.getDefault())
    val endDate = Date()
    val startDate = Calendar.getInstance()

    when (option) {
        "하루" -> startDate.add(Calendar.DAY_OF_YEAR, -1)
        "7일" -> startDate.add(Calendar.DAY_OF_YEAR, -7)
        "30일" -> startDate.add(Calendar.DAY_OF_YEAR, -30)
        "60일" -> startDate.add(Calendar.DAY_OF_YEAR, -60)
        "90일" -> startDate.add(Calendar.DAY_OF_YEAR, -90)
        "전체" -> {
            startDate.set(1900, Calendar.JANUARY, 1)
        }
    }

    return dateFormat.format(startDate.time) to dateFormat.format(endDate)
}