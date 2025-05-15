package com.example.learnjapanese.screens.register

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
import com.example.learnjapanese.viewmodels.RegisterViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material3.CircularProgressIndicator
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
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

    // Thêm theo dõi trạng thái
    val registerSuccess by viewModel.registerSuccess
    val message by viewModel.showMessage
    
    // Thêm LaunchedEffect để theo dõi trạng thái đăng ký thành công
    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            delay(1500) // Chờ 1.5 giây để hiển thị thông báo thành công
            onRegisterSuccess()
        }
    }
    
    // Xử lý hiển thị thông báo
    Scaffold(

    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Move the Surface and its content here
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
                        text = "Đăng ký tài khoản",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = MauChinhDam,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Modify the OutlinedTextField common properties
                    val textFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MauChinh,
                        focusedLabelColor = MauChinh,
                        focusedLeadingIconColor = MauChinhDam,
                        cursorColor = MauChinh,
                        unfocusedLeadingIconColor = MauChinhDam.copy(alpha = 1f),
                        // Darker text color for input
                        focusedTextColor = Color.Black.copy(alpha = 0.9f),
                        unfocusedTextColor = Color.Black.copy(alpha = 0.9f),
                        // Error colors
                        errorBorderColor = Color.Red.copy(alpha = 0.8f),
                        errorLabelColor = Color.Red.copy(alpha = 0.8f),
                        errorLeadingIconColor = Color.Red.copy(alpha = 0.8f),
                        errorSupportingTextColor = Color.Red.copy(alpha = 0.8f)
                    )

                    // Update each OutlinedTextField to use textFieldColors
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
                                    color = Color.Red.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Medium
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
                        colors = textFieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        shape = RoundedCornerShape(12.dp) // Add rounded corners
                    )

                    // For email field
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
                                        color = Color.Red.copy(alpha = 0.7f),
                                        fontWeight = FontWeight.Medium
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
                        colors = textFieldColors, // Add this line and remove the existing colors property
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // For phone field
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
                                        color = Color.Red.copy(alpha = 0.7f),
                                        fontWeight = FontWeight.Medium
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
                        colors = textFieldColors, // Add this line and remove the existing colors property
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // For password field
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
                                        color = Color.Red.copy(alpha = 0.7f),
                                        fontWeight = FontWeight.Medium
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
                            .padding(bottom = 5.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // For confirm password field
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
                                    color = Color.Red.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Medium
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
                            .padding(bottom = 5.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            if (viewModel.validateInputs()) {
                                viewModel.register()
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
                        if (viewModel.isLoading.value) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Đăng ký",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
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
            // Replace existing Snackbar with centered message
            message?.let {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = if (registerSuccess) MauChinh else MaterialTheme.colorScheme.error,
                        shadowElevation = 8.dp
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(16.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
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