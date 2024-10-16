package com.devjsg.cj_logistics_future_technology.presentation.home.admin.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devjsg.cj_logistics_future_technology.R

@Composable
fun ShowDropdown(listSize: Int, onListSizeChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(10, 20, 50)
    var selectedItem by remember { mutableStateOf(listSize) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp, end = 16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, Color(0xFFDFDFDF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$selectedItem", color = Color.Black, style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Black,
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_dropdown),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .width(IntrinsicSize.Min),
            offset = DpOffset(x = (-4).dp, y = 0.dp)
        ) {
            items.forEach { size ->
                DropdownMenuItem(
                    text = { Text("$size") },
                    onClick = {
                        selectedItem = size
                        onListSizeChange(size)
                        expanded = false
                    },
                )
            }
        }
    }
}