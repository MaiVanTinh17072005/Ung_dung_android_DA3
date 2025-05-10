package com.example.learnjapanese.ui.screens.changepassword

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.learnjapanese.ui.viewmodels.ChangePasswordViewModel

// Add these imports at the top
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField

// Add these imports
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
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit,
    onPasswordChanged: () -> Unit
) {
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = MauNen,
            darkIcons = true
        )
    }

    // Add this at the top level of the composable
    LaunchedEffect(viewModel.showSuccessMessage) {
        if (viewModel.showSuccessMessage) {
            delay(2000) // Wait for 2 seconds
            viewModel.hideSuccessMessage()
            onPasswordChanged() // Navigate to login screen
        }
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
                text = "Đổi mật khẩu",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MauChinhDam,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = viewModel.newPassword,
                onValueChange = { viewModel.updateNewPassword(it.replace("\n", "")) },
                label = { Text("Mật khẩu mới") },
                singleLine = true,
                isError = viewModel.newPasswordError != null,
                supportingText = {
                    viewModel.newPasswordError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (newPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                            tint = MauChinhDam
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MauChinh,
                    focusedLabelColor = MauChinh,
                    cursorColor = MauChinh,
                    focusedTrailingIconColor = MauChinhDam,
                    unfocusedTrailingIconColor = MauChinhDam.copy(alpha = 1f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it.replace("\n", "")) },
                label = { Text("Xác nhận mật khẩu mới") },
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                    cursorColor = MauChinh,
                    focusedTrailingIconColor = MauChinhDam,
                    unfocusedTrailingIconColor = MauChinhDam.copy(alpha = 1f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = MauChinh
                )
            }

            if (viewModel.showSuccessMessage) {
                Text(
                    text = "Đổi mật khẩu thành công!",
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            viewModel.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.changePassword()
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
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        "Xác nhận",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            // Replace the existing TextButton with this one
            TextButton(
                onClick = onPasswordChanged, // Changed from onBackClick to onPasswordChanged
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    "Quay lại đăng nhập", // Updated text to be more specific
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MauChinhDam,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}