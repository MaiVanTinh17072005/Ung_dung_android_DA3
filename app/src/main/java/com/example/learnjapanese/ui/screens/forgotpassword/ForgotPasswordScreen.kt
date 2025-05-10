package com.example.learnjapanese.ui.screens.forgotpassword

// Add these imports
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.TextStyle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.ui.viewmodels.ForgotPasswordViewModel

// Add these imports at the top
import com.example.learnjapanese.ui.theme.MauChinh
import com.example.learnjapanese.ui.theme.MauChinhDam
import com.example.learnjapanese.ui.theme.MauNen
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit,
    onSubmitOtp: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error message in Snackbar
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
                withDismissAction = false // Remove dismiss action
            )
            delay(2000) // Force dismiss after 2 seconds
            if (!viewModel.otpSent) {
                viewModel.resetState()
            }
        }
    }

    Scaffold(
        snackbarHost = { 
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                ) { data ->
                    Snackbar(
                        containerColor = if (viewModel.otpSent) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                        contentColor = Color.White,
                        snackbarData = data,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
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
                    text = "Quên mật khẩu",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MauChinhDam,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                if (!viewModel.otpSent) {
                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.updateEmail(it.replace("\n", "")) },
                        label = { Text("Email tạo tài khoản") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        textStyle = TextStyle(color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MauChinh,
                            focusedLabelColor = MauChinh,
                            cursorColor = MauChinh
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        isError = viewModel.emailError != null,
                        supportingText = {
                            viewModel.emailError?.let { error ->
                                Text(
                                    text = error,
                                    color = Color.Red.copy(alpha = 0.8f)
                                )
                            }
                        }
                    )

                    Button(
                        onClick = { viewModel.sendOtp() },
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
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(
                            "Gửi mã OTP",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                } else {
                    Text(
                        text = "Đã gửi mã OTP đến ${viewModel.email}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.otp,
                        onValueChange = { viewModel.updateOtp(it.replace("\n", "")) },
                        label = { Text("Nhập mã OTP") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MauChinh,
                            focusedLabelColor = MauChinh,
                            unfocusedLabelColor = Color.Black,
                            cursorColor = MauChinh,
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    // Add this LaunchedEffect to handle navigation
                    LaunchedEffect(viewModel.otpVerified) {
                        if (viewModel.otpVerified) {
                            delay(1500) // Wait for success message to show
                            onSubmitOtp()
                        }
                    }

                    // Modify the Button onClick to only call verifyOtp
                    Button(
                        onClick = { 
                            viewModel.verifyOtp()
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
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(
                            "Xác nhận mã OTP",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(onClick = onBackClick) {
                    Text(
                        "Quay lại đăng nhập",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MauChinhDam,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    LearnJapaneseTheme {
        ForgotPasswordScreen(
            onBackClick = {},
            onSubmitOtp = {}
        )
    }
}