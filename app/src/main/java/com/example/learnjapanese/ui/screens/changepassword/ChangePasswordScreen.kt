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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.ui.viewmodels.ChangePasswordViewModel

// Add these imports at the top
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.TextStyle

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit,
    onPasswordChanged: () -> Unit
) {
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Change Password",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = viewModel.newPassword,
            onValueChange = { viewModel.updateNewPassword(it.replace("\n", "")) },
            label = { Text("New Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                    Icon(
                        imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (newPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.updateConfirmPassword(it.replace("\n", "")) },
            label = { Text("Confirm New Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = { 
                viewModel.changePassword()
                onPasswordChanged() 
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Change Password")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onBackClick) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    LearnJapaneseTheme {
        ChangePasswordScreen(
            onBackClick = {},
            onPasswordChanged = {}
        )
    }
}