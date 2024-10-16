package com.devjsg.cj_logistics_future_technology.presentation.home.admin.component

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.ContestHomeViewModel

@Composable
fun PaginationRow(
    viewModel: ContestHomeViewModel,
    totalPages: Int
) {
    val pagesPerGroup = 5
    var currentGroupStart by remember { mutableIntStateOf(1) }

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "<<",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    currentGroupStart = 1
                    viewModel.currentPage.value = 1
                    viewModel.applySortingAndFiltering()
                },
            fontSize = 18.sp
        )

        Text(
            text = "<",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    if (currentGroupStart > 1) {
                        currentGroupStart -= pagesPerGroup
                        viewModel.currentPage.value = currentGroupStart
                        viewModel.applySortingAndFiltering()
                    }
                },
            fontSize = 18.sp,
        )

        val endPage = minOf(currentGroupStart + pagesPerGroup - 1, totalPages)
        Log.d("endPage", endPage.toString())
        for (page in currentGroupStart..endPage) {
            Text(
                text = page.toString(),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        viewModel.currentPage.value = page
                        viewModel.applySortingAndFiltering()
                    },
                fontSize = 18.sp,
                color = if (page == viewModel.currentPage.value) Color.Black else Color.Gray
            )
        }

        Text(
            text = ">",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    if (currentGroupStart + pagesPerGroup <= totalPages) {
                        currentGroupStart += pagesPerGroup
                        viewModel.currentPage.value = currentGroupStart
                        viewModel.applySortingAndFiltering()
                    }
                },
            fontSize = 18.sp,
        )

        Text(
            text = ">>",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    currentGroupStart = (totalPages - 1) / pagesPerGroup * pagesPerGroup + 1
                    viewModel.currentPage.value = totalPages
                    viewModel.applySortingAndFiltering()
                },
            fontSize = 18.sp,
        )
    }
}