package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.component.EmergencyReportItem
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.AdminViewModel
import java.time.LocalDate

@SuppressLint("DefaultLocale")
@Composable
fun IncidentHistoryScreen(viewModel: AdminViewModel, navController: NavController) {
    var loginId by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    val context = LocalContext.current
    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().monthValue - 1
    val currentDay = LocalDate.now().dayOfMonth

    val startDatePickerDialog = remember {
        val dialogContext = ContextThemeWrapper(context, R.style.CustomDatePickerDialog)
        DatePickerDialog(dialogContext, { _, year, month, dayOfMonth ->
            startDate =
                "$year-${String.format("%02d", month + 1)}-${String.format("%02d", dayOfMonth)}"
        }, currentYear, currentMonth, currentDay)
    }

    val endDatePickerDialog = remember {
        val dialogContext = ContextThemeWrapper(context, R.style.CustomDatePickerDialog)
        DatePickerDialog(dialogContext, { _, year, month, dayOfMonth ->
            endDate =
                "$year-${String.format("%02d", month + 1)}-${String.format("%02d", dayOfMonth)}"
        }, currentYear, currentMonth, currentDay)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = {
                    Text(
                        "시작 날짜",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFFA5A5A5),
                        )
                    )
                },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { startDatePickerDialog.show() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Start Date",
                            tint = Color(0xFF191919)
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { startDatePickerDialog.show() },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF191919),
                    unfocusedBorderColor = Color(0xFFEDEDED),
                    focusedLabelColor = Color.Black,
                )
            )

            OutlinedTextField(
                value = endDate,
                onValueChange = { endDate = it },
                label = {
                    Text(
                        "종료 날짜",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFFA5A5A5),
                        )
                    )
                },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { endDatePickerDialog.show() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "End Date",
                            tint = Color(0xFF191919)
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { startDatePickerDialog.show() },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF191919),
                    unfocusedBorderColor = Color(0xFFEDEDED),
                    focusedLabelColor = Color.Black,
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = loginId,
                onValueChange = { loginId = it },
                label = {
                    Text(
                        "회원 아이디로 검색",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFFA5A5A5),
                        )
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFEF151E),
                    unfocusedBorderColor = Color(0xFFEF151E),
                    focusedLabelColor = Color.Black,
                )
            )

            IconButton(
                onClick = {
                    when {
                        loginId.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty() -> {
                            viewModel.searchEmergencyReports(loginId, startDate, endDate)
                        }

                        loginId.isEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty() -> {
                            viewModel.getEmergencyReportsWithDate(startDate, endDate)
                        }

                        loginId.isNotEmpty() && startDate.isEmpty() && endDate.isEmpty() -> {
                            viewModel.searchEmergencyReports(loginId)
                        }
                    }
                },
                modifier = Modifier
                    .offset(y = 2.dp)
                    .width(57.dp)
                    .height(57.dp)
                    .background(color = Color(0xFFEF151E), shape = RoundedCornerShape(size = 12.dp))
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val emergencyReports by viewModel.emergencyReports.collectAsState()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            emergencyReports?.let { reports ->
                items(reports) { report ->
                    EmergencyReportItem(
                        report = report,
                        onClick = {
                            navController.navigate("detail_member/${report.reporterId}")
                        }
                    )
                }
            }
        }
    }
}