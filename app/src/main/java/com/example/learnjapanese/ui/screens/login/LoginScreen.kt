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
import com.example.learnjapanese.ui.viewmodels.LoginViewModelFactory
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.tween
import com.example.learnjapanese.R
import kotlinx.coroutines.delay
import com.example.learnjapanese.ui.theme.MauChinh
import com.example.learnjapanese.ui.theme.MauChinhDam
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import com.example.learnjapanese.ui.theme.Xanh_la_qmk
import com.example.learnjapanese.ui.theme.mau_chu_o_text
import com.example.learnjapanese.ui.theme.xanhnhat
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.learnjapanese.data.model.LoginState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Snackbar
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.shadow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import android.util.Log

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(LocalContext.current)),
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    var isVisible by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.loginState) {
        when (viewModel.loginState) {
            is LoginState.Success -> {
                scope.launch {
                    try {
                        snackbarHostState.showSnackbar(
                            message = "Đăng nhập thành công",
                            duration = SnackbarDuration.Short,
                            actionLabel = null
                        )
                        delay(1000)
                        onLoginSuccess()
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Error showing success snackbar", e)
                    }
                }
            }
            is LoginState.Error -> {
                scope.launch {
                    try {
                        snackbarHostState.showSnackbar(
                            message = (viewModel.loginState as LoginState.Error).message,
                            duration = SnackbarDuration.Short,
                            actionLabel = null
                        )
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Error showing error snackbar", e)
                    }
                }
            }
            else -> {}
        }
    }

    // Add this LaunchedEffect to reset notifications when screen is shown
    LaunchedEffect(Unit) {
        viewModel.resetAllNotifications()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.updateUsername("")
            viewModel.updatePassword("")
            viewModel.resetAllNotifications()
        }
    }

    LaunchedEffect(Unit) {
        delay(20)
        isVisible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                // Background Image and Overlay remain unchanged
                Image(
                    painter = painterResource(id = R.drawable.hinh_nen_1),
                    contentDescription = "Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0f))
                )

                // Outer container remains unchanged
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(xanhnhat.copy(alpha = 0.6f))
                    )

                    // Extract inner content to separate composable
                    LoginContent(
                        viewModel = viewModel,
                        onRegisterClick = onRegisterClick,
                        onForgotPasswordClick = onForgotPasswordClick,
                        onLoginSuccess = onLoginSuccess
                    )
                }
            }
        }

        // Custom Snackbar ở giữa màn hình
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) { data ->
            val isSuccess = data.visuals.message.contains("thành công")
            val backgroundColor = if (isSuccess) Color(0xFF4CAF50) else Color.Red.copy(alpha = 0.9f)
            val icon = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error
            
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(12.dp)),
                containerColor = backgroundColor,
                contentColor = Color.White,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginContent(
    viewModel: LoginViewModel,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var isContentVisible by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Reset content visibility when navigating away
    DisposableEffect(Unit) {
        onDispose {
            isContentVisible = false
            viewModel.updateUsername("")
            viewModel.updatePassword("")
            viewModel.resetAllNotifications()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "LearnJapanese",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MauChinhDam,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                    fontSize = 27.sp,
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
                    .padding(bottom = 10.dp)
            )

            Text(
                text = "Đăng nhập để tiếp tục",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Black.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp

                ),
                modifier = Modifier.padding(bottom = 25.dp)
            )

            // For Email TextField
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.updateUsername(it.replace("\n", "")) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black.copy(alpha = 0.7f),
                    fontWeight = FontWeight.W500,
                    fontSize = 14.sp
                ),
                label = {
                    Text(
                        "Email",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.W600,
                            color = mau_chu_o_text.copy(alpha = 0.7f),
                            fontStyle = FontStyle.Italic
                        )
                    )
                },
                singleLine = true,
                isError = viewModel.emailError != null,
                supportingText = {
                    viewModel.emailError?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.W300,
                                color = Color.Red.copy(alpha = 0.6f)
                            )
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
                    unfocusedLeadingIconColor = MauChinhDam,
                    unfocusedLabelColor = MauChinhDam.copy(alpha = 0.7f),
                    unfocusedBorderColor = MauChinhDam.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            // For Password TextField
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.updatePassword(it) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black.copy(alpha = 0.7f),
                    fontWeight = FontWeight.W500,
                    fontSize = 14.sp
                ),
                label = {
                    Text(
                        "Mật khẩu",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.W600,
                            color = mau_chu_o_text.copy(alpha = 0.7f),
                            fontStyle = FontStyle.Italic
                        )
                    )
                },
                singleLine = true,
                isError = viewModel.passwordError != null,
                supportingText = {
                    viewModel.passwordError?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.W300,
                                color = Color.Red.copy(alpha = 0.6f)
                            )
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
                    unfocusedLeadingIconColor = MauChinhDam,
                    unfocusedTrailingIconColor = MauChinhDam,
                    unfocusedLabelColor = MauChinhDam.copy(alpha = 0.7f),
                    unfocusedBorderColor = MauChinhDam.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(onClick = onForgotPasswordClick) {
                    Text(
                        text = "Quên mật khẩu?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Xanh_la_qmk,
                            fontWeight = FontWeight.W500,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp,
                        )
                    )
                }
            }

            // Update the Button onClick
            Button(
                onClick = {
                    viewModel.login()
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
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        fontSize = 13.sp
                    )
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "Đăng ký",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MauChinhDam,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}