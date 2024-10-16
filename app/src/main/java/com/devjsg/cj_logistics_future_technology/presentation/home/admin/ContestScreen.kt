package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.component.PaginationRow
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.component.ShowDropdown
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.component.StaffCard
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.ContestHomeViewModel
import java.text.DecimalFormat

@Composable
fun ContestScreen(
    navController: NavController,
    viewModel: ContestHomeViewModel = hiltViewModel()
) {
    var warningText by remember { mutableStateOf("필터 없음") }
    var listSize by remember { mutableStateOf(10) }

    Column {
        var searchQuery by remember { mutableStateOf("") }
        var showFilters by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newQuery ->
                    searchQuery = newQuery
                    viewModel.searchQuery.value = newQuery
                },
                label = null,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchStaffByName(searchQuery)
                    }
                ),
                placeholder = {
                    Text(
                        "근무자 이름 검색", style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFFA5A5A5),
                        )
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFEF151E),
                    unfocusedBorderColor = Color(0xFFEF151E),
                    focusedLabelColor = Color.Black,
                )
            )

            val isActive = remember { mutableStateOf(false) }

            Button(
                onClick = {
                    isActive.value = !isActive.value
                    showFilters = !showFilters
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive.value) Color.White else Color(0xFFEF151E)
                ),
                border = BorderStroke(
                    1.dp,
                    if (isActive.value) Color(0xFFEF151E) else Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = "Settings",
                    tint = if (isActive.value) Color(0xFFEF151E) else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (showFilters) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    "정렬 조건",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight(700),
                        color = Color.Black,
                    )
                )
                Row {
                    Button(
                        onClick = { viewModel.toggleMoveSortOrder() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Color(0xFFEF151E),
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "걸음수",
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(
                                    id = when (viewModel.moveSortOrder.value) {
                                        "MOVE_DESC" -> R.drawable.ic_desc
                                        "MOVE_ASC" -> R.drawable.ic_asc
                                        else -> R.drawable.ic_none_sort
                                    }
                                ),
                                contentDescription = null,
                                tint = when (viewModel.moveSortOrder.value) {
                                    "MOVE_DESC" -> Color(0xFF006ECD)
                                    "MOVE_ASC" -> Color(0xFFEF151E)
                                    else -> Color.Gray
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { viewModel.toggleKmSortOrder() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Color(0xFFEF151E),
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "이동거리",
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(
                                    id = when (viewModel.kmSortOrder.value) {
                                        "KM_DESC" -> R.drawable.ic_desc
                                        "KM_ASC" -> R.drawable.ic_asc
                                        else -> R.drawable.ic_none_sort
                                    }
                                ),
                                contentDescription = null,
                                tint = when (viewModel.kmSortOrder.value) {
                                    "KM_DESC" -> Color(0xFF006ECD)
                                    "KM_ASC" -> Color(0xFFEF151E)
                                    else -> Color.Gray
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { viewModel.toggleHeartRateSortOrder() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Color(0xFFEF151E),
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "심박수",
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(
                                    id = when (viewModel.heartRateSortOrder.value) {
                                        "HEART_RATE_DESC" -> R.drawable.ic_desc
                                        "HEART_RATE_ASC" -> R.drawable.ic_asc
                                        else -> R.drawable.ic_none_sort
                                    }
                                ),
                                contentDescription = null,
                                tint = when (viewModel.heartRateSortOrder.value) {
                                    "HEART_RATE_DESC" -> Color(0xFF006ECD)
                                    "HEART_RATE_ASC" -> Color(0xFFEF151E)
                                    else -> Color.Gray
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "필터 조건",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight(700),
                        color = Color.Black,
                    ),
                )
                Row {
                    Button(
                        onClick = {
                            warningText = when (warningText) {
                                "필터 없음" -> {
                                    viewModel.updateReportCondition("HEART_RATE_FILTER")
                                    "심박수 주의"
                                }

                                "심박수 주의" -> {
                                    viewModel.updateReportCondition("MOVE_FILTER")
                                    "걸음수 주의"
                                }

                                "걸음수 주의" -> {
                                    viewModel.updateReportCondition("KM_FILTER")
                                    "이동거리 주의"
                                }

                                else -> {
                                    viewModel.updateReportCondition("NONE")
                                    "필터 없음"
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .height(48.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFFEF151E),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = warningText,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(
                                    id = when (warningText) {
                                        "심박수 주의" -> R.drawable.ic_filter_heart
                                        "걸음수 주의" -> R.drawable.ic_filter_walk
                                        "이동거리 주의" -> R.drawable.ic_filter_distance
                                        else -> R.drawable.ic_none_sort
                                    }
                                ),
                                contentDescription = null,
                                tint = when (warningText) {
                                    "심박수 주의" -> Color(0xFFEF151E)
                                    "걸음수 주의" -> Color(0xFFFF9700)
                                    "이동거리 주의" -> Color(0xFF006ECD)
                                    else -> Color.Gray
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            viewModel.applySortingAndFiltering()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF151E)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            text = "필터 및 정렬 적용",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            viewModel.resetSortingAndFiltering()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF151E)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .size(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_reset),
                            contentDescription = "초기화",
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }

        ShowDropdown(listSize = listSize, onListSizeChange = { newSize ->
            listSize = newSize
            viewModel.updateListSize(newSize)
            viewModel.applySortingAndFiltering()
        })

        val staffList by viewModel.staffList.collectAsState()
        val totalPages = viewModel.totalPages.value

        val formatter = DecimalFormat("#,###")

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            items(staffList) { staff ->
                StaffCard(staff = staff, formatter = formatter)
            }
        }

        PaginationRow(
            viewModel = viewModel,
            totalPages = totalPages
        )
    }

    LaunchedEffect(viewModel.currentPage.value, listSize) {
        viewModel.applySortingAndFiltering()
    }
}