package com.example.learnjapanese.ui.screens.login

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val iconColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = primaryColor,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Sign in to continue",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.updateUsername(it.replace("\n", "")) },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = iconColor
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor,
                focusedLeadingIconColor = iconColor,
                cursorColor = primaryColor,
                unfocusedLeadingIconColor = iconColor.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = iconColor
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = iconColor
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor,
                focusedLeadingIconColor = iconColor,
                focusedTrailingIconColor = iconColor,
                cursorColor = primaryColor,
                unfocusedLeadingIconColor = iconColor.copy(alpha = 0.7f),
                unfocusedTrailingIconColor = iconColor.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = onForgotPasswordClick) {
                Text(
                    text = "Forgot Password?",
                    color = primaryColor
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
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                "Login",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onRegisterClick) {
                Text(
                    text = "Register",
                    color = primaryColor
                )
            }
        }
    }
}