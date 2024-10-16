package com.devjsg.cj_logistics_future_technology.presentation.home.admin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.devjsg.cj_logistics_future_technology.data.model.Member

@Composable
fun MemberItem(member: Member, onEditClick: () -> Unit, onItemClick: () -> Unit) {
    val iconRes = when (member.gender) {
        "MALE" -> R.drawable.ic_man_face
        "FEMALE" -> R.drawable.ic_woman_face
        else -> R.drawable.ic_man_face
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF7F7F7), shape = RoundedCornerShape(size = 16.dp))
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .clickable { onItemClick() }
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = "Member Icon",
            tint = Color.Unspecified
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = member.loginId,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF242424),
                )
            )
            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = member.employeeName,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF6F6F6F),
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
                    text = member.phone,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF6F6F6F),
                    )
                )
            }
        }

        TextButton(
            onClick = onEditClick,
            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF006ECD))
        ) {
            Text("편집하기")
        }
    }
}