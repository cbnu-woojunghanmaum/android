package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.component.EditMemberBottomSheet
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun AdminHomeScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val members = viewModel.members.collectAsLazyPagingItems()
    val selectedMember by viewModel.selectedMember.collectAsState()
    Log.d("selectedMember", selectedMember.toString())
    val searchResult by viewModel.searchResult.collectAsState()
    val snackBarMessage by viewModel.snackBarMessage.collectAsState()
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let {
            snackBarHostState.showSnackbar(it)
        }
    }

    val context = LocalContext.current

    val window = (context as? Activity)?.window
    window?.statusBarColor = Color.White.toArgb()
    val insetsController = window?.let { WindowCompat.getInsetsController(it, window.decorView) }
    insetsController?.isAppearanceLightStatusBars = true

    val pagerState = rememberPagerState(pageCount = { 3 })

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetShadowElevation = 20.dp,
        sheetContainerColor = Color.White,
        sheetContent = {
            selectedMember?.let {
                EditMemberBottomSheet(
                    member = it,
                    onDismiss = { scope.launch { sheetState.bottomSheetState.hide() } },
                    onSave = { updatedMember ->
                        viewModel.updateMember(updatedMember, {
                            members.refresh()
                            scope.launch { sheetState.bottomSheetState.hide() }
                        }, {
                            // 에러 처리
                        })
                    }
                )
            }
        },
        containerColor = Color.White,
        sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "관리자 화면",
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF242424)
                        )
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout {
                            navController.navigate("login") {
                                popUpTo("admin_home") { inclusive = true }
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
                    containerColor = Color.White
                )
            )

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color(0xFFEF151E)
                    )
                }
            ) {
                Tab(
                    text = {
                        Text(
                            "레포트", style = if (pagerState.currentPage == 0) TextStyle(
                                fontWeight = FontWeight(
                                    700
                                )
                            ) else TextStyle(fontWeight = FontWeight(400))
                        )
                    },
                    selected = pagerState.currentPage == 0,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } }
                )
                Tab(
                    text = {
                        Text(
                            "회원 리스트",
                            style = if (pagerState.currentPage == 1) TextStyle(
                                fontWeight = FontWeight(
                                    700
                                )
                            ) else TextStyle(fontWeight = FontWeight(400))
                        )
                    },
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
                )
                Tab(
                    text = {
                        Text(
                            "신고 이력 조회", style = if (pagerState.currentPage == 2) TextStyle(
                                fontWeight = FontWeight(
                                    700
                                )
                            ) else TextStyle(fontWeight = FontWeight(400))
                        )
                    },
                    selected = pagerState.currentPage == 2,
                    onClick = { scope.launch { pagerState.animateScrollToPage(2) } }
                )
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> ContestScreen(navController = navController)

                    1 -> MemberListScreen(
                        navController = navController,
                        viewModel = viewModel,
                        members = members,
                        searchResult = searchResult,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        onSearch = { viewModel.searchMembers(searchQuery) },
                        onEditClick = {
                            viewModel.getMemberInfo(it)
                            scope.launch { sheetState.bottomSheetState.expand() }
                        }
                    )

                    2 -> IncidentHistoryScreen(viewModel = viewModel, navController = navController)
                }
            }
        }
    }
}