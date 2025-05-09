package com.example.learnjapanese.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.ui.theme.MauChinh
import com.example.learnjapanese.ui.theme.MauChinhDam
import com.example.learnjapanese.ui.theme.MauNen
import com.example.learnjapanese.ui.viewmodels.RegisterViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = MauNen,
            darkIcons = true
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MauNen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Đăng ký tài khoản",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MauChinhDam,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = viewModel.fullName,
                onValueChange = { viewModel.updateFullName(it.replace("\n", "")) },
                label = { Text("Họ và tên") },
                singleLine = true,
                isError = viewModel.fullNameError != null,
                supportingText = {
                    viewModel.fullNameError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name Icon",
                        tint = MauChinhDam
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MauChinh,
                    focusedLabelColor = MauChinh,
                    focusedLeadingIconColor = MauChinhDam,
                    cursorColor = MauChinh,
                    unfocusedLeadingIconColor = MauChinhDam.copy(alpha = 1f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.updateEmail(it.replace("\n", "")) },
                label = { Text("Email") },
                singleLine = true,
                isError = viewModel.emailError != null,
                supportingText = {
                    viewModel.emailError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = MauChinhDam
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MauChinh,
                    focusedLabelColor = MauChinh,
                    focusedLeadingIconColor = MauChinhDam,
                    cursorColor = MauChinh,
                    unfocusedLeadingIconColor = MauChinhDam.copy(alpha = 1f)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )

            OutlinedTextField(
                value = viewModel.phone,
                onValueChange = { viewModel.updatePhone(it.replace("\n", "")) },
                label = { Text("Số điện thoại") },
                singleLine = true,
                isError = viewModel.phoneError != null,
                supportingText = {
                    viewModel.phoneError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon",
                        tint = MauChinhDam
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MauChinh,
                    focusedLabelColor = MauChinh,
                    focusedLeadingIconColor = MauChinhDam,
                    cursorColor = MauChinh,
                    unfocusedLeadingIconColor = MauChinhDam.copy(alpha = 1f)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.updatePassword(it.replace("\n", "")) },
                label = { Text("Mật khẩu") },
                singleLine = true,
                isError = viewModel.passwordError != null,
                supportingText = {
                    viewModel.passwordError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon",
                        tint = MauChinhDam
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                            tint = MauChinhDam
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MauChinh,
                    focusedLabelColor = MauChinh,
                    focusedLeadingIconColor = MauChinhDam,
                    focusedTrailingIconColor = MauChinhDam,
                    cursorColor = MauChinh,
                    unfocusedLeadingIconColor = MauChinhDam.copy(alpha = 1f),
                    unfocusedTrailingIconColor = MauChinhDam.copy(alpha = 1f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )

            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it.replace("\n", "")) },
                label = { Text("Xác nhận mật khẩu") },
                singleLine = true,
                isError = viewModel.confirmPasswordError != null,
                supportingText = {
                    viewModel.confirmPasswordError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Confirm Password Icon",
                        tint = MauChinhDam
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                            tint = MauChinhDam
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MauChinh,
                    focusedLabelColor = MauChinh,
                    focusedLeadingIconColor = MauChinhDam,
                    focusedTrailingIconColor = MauChinhDam,
                    cursorColor = MauChinh,
                    unfocusedLeadingIconColor = MauChinhDam.copy(alpha = 1f),
                    unfocusedTrailingIconColor = MauChinhDam.copy(alpha = 1f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )

            Button(
                onClick = {
                    if (viewModel.validateInputs()) {
                        viewModel.register()
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MauChinh,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    hoveredElevation = 8.dp,
                    focusedElevation = 8.dp
                )
            ) {
                Text(
                    "Đăng ký",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Đã có tài khoản? ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                )
                TextButton(onClick = onBackClick) {
                    Text(
                        text = "Đăng nhập",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MauChinhDam,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    LearnJapaneseTheme {
        RegisterScreen(
            onBackClick = {},
            onRegisterSuccess = {}
        )
    }
}