package com.example.learnjapanese.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjapanese.ui.viewmodels.LoginViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.tween
import com.example.learnjapanese.R
import kotlinx.coroutines.delay
import com.example.learnjapanese.ui.theme.MauChinh
import com.example.learnjapanese.ui.theme.MauChinhDam
import com.example.learnjapanese.ui.theme.MauNen
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(20)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image and Overlay
            Image(
                painter = painterResource(id = R.drawable.hinh_nen_1),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.12f))
            )

            // Content Box
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(10.dp)
                    .height(580.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .border(
                        width = 1.2.dp,
                        color = MauChinh.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(40.dp)
                    )
                    .align(Alignment.Center)
            ) {
                // Inner background
                Image(
                    painter = painterResource(id = R.drawable.hinh_nen_1),
                    contentDescription = "Inner Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.5f))
                )

                // Main Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center  // Changed to Center
                ) {
                    Text(
                        text = "LearnJapanese",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MauChinhDam,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp,
                            shadow = Shadow(
                                color = MauChinh.copy(alpha = 0.3f),
                                offset = Offset(1f, 1f),
                                blurRadius = 2f
                            )
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)  // Reduced padding
                    )

                    Text(
                        text = "Đăng nhập để tiếp tục",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Black.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium

                        ),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.username,
                        onValueChange = { viewModel.updateUsername(it.replace("\n", "")) },
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.updatePassword(it) },
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
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
                            .padding(bottom = 4.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(onClick = onForgotPasswordClick) {
                            Text(
                                text = "Quên mật khẩu?",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MauChinh,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.login()
                            onLoginSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
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
                            "Đăng nhập",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chưa có tài khoản? ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF212121)
                            )
                        )
                        TextButton(onClick = onRegisterClick) {
                            Text(
                                text = "Đăng ký",
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
    }
}