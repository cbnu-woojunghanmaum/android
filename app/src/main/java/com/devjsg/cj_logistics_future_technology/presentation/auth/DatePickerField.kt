package com.devjsg.cj_logistics_future_technology.presentation.auth

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.view.ContextThemeWrapper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.devjsg.cj_logistics_future_technology.R

@Composable
fun DatePickerIcon(
    date: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val dialogContext = ContextThemeWrapper(context, R.style.CustomDatePickerDialog)
        DatePickerDialog(
            dialogContext,
            { _, selectedYear, selectedMonth, selectedDay ->
                onDateSelected("$selectedYear-${selectedMonth + 1}-$selectedDay")
                showDialog = false
            },
            year, month, day
        ).apply {
            setOnDismissListener { showDialog = false }
            show()
        }
    }

    OutlinedTextField(
        value = date,
        onValueChange = {},
        readOnly = true,
        label = { Text("생년월일") },
        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = "달력 아이콘",
                    tint = Color.Black
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Black,
            focusedLabelColor = Color(0xFF6F6F6F)
        )
    )
}