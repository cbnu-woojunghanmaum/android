package com.devjsg.cj_logistics_future_technology.presentation.home.admin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.data.model.EmergencyReport

@Composable
fun EmergencyReportItem(report: EmergencyReport, onClick: () -> Unit) {
    val emergencyText = when (report.emergency) {
        "REPORT" -> "직접 신고"
        "HEART_RATE" -> "심박수 경고"
        else -> "알 수 없음"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFDFDFDF),
                shape = RoundedCornerShape(size = 16.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .clickable { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_emergency),
                contentDescription = "Member Icon",
                tint = Color.Unspecified
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = emergencyText,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF242424),
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = report.createdAt,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF242424),
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(text = "신고자: ${report.reporter}", style = MaterialTheme.typography.bodyMedium)

                Text(
                    text = "위치: (${report.x}, ${report.y})",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "신고 시간: ${report.createdAt}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "회원 아이디: ${report.loginId}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(text = "전화번호: ${report.phone}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}