package com.devjsg.cj_logistics_future_technology.presentation.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MemberViewModel
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(
    viewModel: MemberViewModel = hiltViewModel<MemberViewModel>(),
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var employeeName by remember { mutableStateOf("") }
    val isLoginIdValid by viewModel.isLoginIdValid.collectAsState()
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var gender by remember { mutableStateOf("Male") }
    val loginIdMessage by viewModel.loginIdMessage.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
    val isPasswordValid by derivedStateOf { Pattern.matches(passwordRegex, password) }
    val doPasswordsMatch by derivedStateOf { password == confirmPassword }
    val isFormValid by derivedStateOf {
        username.isNotEmpty() && employeeName.isNotEmpty() && password.isNotEmpty() &&
                confirmPassword.isNotEmpty() && email.isNotEmpty() && birthDate.isNotEmpty() &&
                phoneNumber.text.isNotEmpty() && isPasswordValid &&
                doPasswordsMatch && isLoginIdValid
    }
    val context = LocalContext.current

    val window = (context as? Activity)?.window
    window?.statusBarColor = Color(0xFFF7F7F7).toArgb()
    val insetsController = window?.let { WindowCompat.getInsetsController(it, window.decorView) }
    insetsController?.isAppearanceLightStatusBars = true

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "회원가입",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF242424)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFF7F7F7))
            )
        },
        containerColor = Color(0xFFF7F7F7),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "계정 정보",
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight(700),
                    color = Color.Black,
                ),
                modifier = Modifier.padding(bottom = 16.dp),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("아이디") },
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color(0xFF6F6F6F)
                    )
                )
                Button(
                    onClick = { viewModel.checkLoginId(username) },
                    modifier = Modifier
                        .width(90.dp)
                        .defaultMinSize(minHeight = 56.dp)
                        .offset(y = 3.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF242424)),
                    shape = RoundedCornerShape(size = 8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = "중복 확인",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(400),
                        )
                    )
                }
            }
            if (loginIdMessage.isNotEmpty()) {
                Text(
                    text = loginIdMessage,
                    color = if (isLoginIdValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = employeeName,
                onValueChange = { employeeName = it },
                label = { Text("이름") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color(0xFF6F6F6F)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color(0xFF6F6F6F)
                )
            )
            if (password.isNotEmpty() && !isPasswordValid) {
                Text(
                    text = "비밀번호에는 8자리 이상, 영문, 숫자, 특수 문자를 모두 포함해야 합니다",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("비밀번호 확인") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color(0xFF6F6F6F)
                )
            )
            if (confirmPassword.isNotEmpty() && !doPasswordsMatch) {
                Text(
                    text = "비밀번호와 다릅니다",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일 주소") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color(0xFF6F6F6F)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            DatePickerIcon(
                date = birthDate,
                onDateSelected = { birthDate = it },
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { newValue ->
                    val filteredValue = newValue.text.filter { it.isDigit() }
                    val formattedValue = formatPhoneNumber(filteredValue)
                    phoneNumber = newValue.copy(
                        text = formattedValue,
                        selection = TextRange(formattedValue.length)
                    )
                    viewModel.onPhoneChange(filteredValue)
                },
                label = { Text("전화번호") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color(0xFF6F6F6F)
                )
            )
            Text(
                text = "성별",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GenderOption(
                    text = "남성",
                    isSelected = gender == "Male",
                    onClick = { gender = "Male" }
                )
                GenderOption(
                    text = "여성",
                    isSelected = gender == "Female",
                    onClick = { gender = "Female" }
                )
            }

            HorizontalDivider(
                color = Color(0xFFDFDFDF),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Button(
                onClick = {
                    if (isFormValid) {
                        val dateParts = birthDate.split("-")
                        val year = dateParts[0].toInt()
                        val month = dateParts[1].toInt()
                        val day = dateParts[2].toInt()

                        viewModel.signUp(
                            loginId = username,
                            password = password,
                            phone = phoneNumber.text,
                            gender = if (gender == "Male") "MALE" else "FEMALE",
                            email = email,
                            employeeName = employeeName,
                            year = year,
                            month = month,
                            day = day,
                            onSuccess = {
                                Toast.makeText(context, "회원가입이 성공적으로 완료되었습니다", Toast.LENGTH_LONG)
                                    .show()
                                navController.navigate("login")
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, "회원가입 실패: $errorMessage", Toast.LENGTH_LONG)
                                    .show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "모든 필드를 올바르게 작성해주세요.", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color.Red else Color.Black
                ),
                shape = RoundedCornerShape(size = 8.dp),
                enabled = isFormValid
            ) {
                Text(
                    text = "회원가입",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun GenderOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF242424) else Color(0xFFDFDFDF),
                shape = RoundedCornerShape(size = 8.dp)
            )
            .width(156.dp)
            .height(48.dp)
            .background(
                color = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(size = 8.dp)
            )
            .clickable { onClick() }
            .padding(top = 11.dp, bottom = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color(0xFFDFDFDF),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

fun formatPhoneNumber(phoneNumber: String): String {
    return when {
        phoneNumber.length <= 3 -> phoneNumber
        phoneNumber.length <= 7 -> "${phoneNumber.substring(0, 3)}-${phoneNumber.substring(3)}"
        phoneNumber.length <= 11 -> "${phoneNumber.substring(0, 3)}-${
            phoneNumber.substring(
                3,
                7
            )
        }-${phoneNumber.substring(7)}"

        else -> "${phoneNumber.substring(0, 3)}-${
            phoneNumber.substring(
                3,
                7
            )
        }-${phoneNumber.substring(7, 11)}"
    }
}