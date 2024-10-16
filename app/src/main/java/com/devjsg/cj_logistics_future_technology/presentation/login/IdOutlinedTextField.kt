package com.devjsg.cj_logistics_future_technology.presentation.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IdOutlinedTextField(
    id: String,
    onIdChange: (String) -> Unit,
    onShowBiometricPrompt: () -> Unit,
    isBiometricPromptShown: Boolean
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = id,
        onValueChange = onIdChange,
        label = { Text(if (isFocused) "아이디" else "아이디를 입력해주세요.") },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (isFocused && !isBiometricPromptShown) {
                    onShowBiometricPrompt()
                }
            }
            .padding(horizontal = 16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            focusedLabelColor = Color(0xFF6F6F6F)
        )
    )
}