package com.devjsg.cj_logistics_future_technology.presentation.home.worker

import android.app.Activity
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MyHeartRateViewModel
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.WorkerHomeViewModel
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerHomeScreen(
    navController: NavController,
    viewModel: WorkerHomeViewModel = hiltViewModel(),
    myHeartRateViewModel: MyHeartRateViewModel
) {
    val heartRateData by viewModel.heartRateData.collectAsState()
    var selectedOption by remember { mutableStateOf("전체") }

    val options = listOf("전체", "하루", "7일", "30일", "60일", "90일")
    val context = LocalContext.current

    LaunchedEffect(selectedOption) {
        val (heartRateStart, heartRateEnd) = getHeartRateDatesForOption(selectedOption)
        viewModel.getHeartRateData(heartRateStart, heartRateEnd)
    }

    LaunchedEffect(selectedOption) {
        val (start, end) = getDatesForOption(selectedOption)
        viewModel.loadEmergencyReports(start, end)
    }

    val heartRate by myHeartRateViewModel.heartRateAvg.collectAsState()
    val myEmergencyReports by viewModel.myEmergencyReports.collectAsState()

    val window = (context as? Activity)?.window
    window?.statusBarColor = Color(0xFFF7F7F7).toArgb()
    val insetsController = window?.let { WindowCompat.getInsetsController(it, window.decorView) }
    insetsController?.isAppearanceLightStatusBars = true

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "메인 화면",
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF242424)
                        )
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout {
                            navController.navigate("login") {
                                popUpTo("worker_home") { inclusive = true }
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = "로그아웃",
                            modifier = Modifier.padding(end = 16.dp),
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF7F7F7)
                )
            )
        },
        containerColor = Color(0xFFF7F7F7),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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
                                    color = if (selectedOption == option) Color.Black else Color(0xFFDFDFDF),
                                    shape = RoundedCornerShape(size = 8.dp)
                                )
                                .background(
                                    color = if (selectedOption == option) Color.Black else Color.White,
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

                Text(
                    text = "평균 심박수",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF242424)
                    )
                )
                Text(
                    text = if (heartRate == 0) "워치를 실행해 주세요" else "$heartRate BPM",
                    modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                    style = TextStyle(
                        fontSize = if(heartRate == 0) 24.sp else 60.sp,
                        lineHeight = 72.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF242424),
                    )
                )

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
                    items(myEmergencyReports) { report ->
                        MyEmergencyReportItem(report)
                    }
                }
            }
        }
    )
}

private fun getHeartRateDatesForOption(option: String): Pair<String, String> {
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

private fun getDatesForOption(option: String): Pair<String, String> {
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