package com.example.learnjapanese.ui.screens.register

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
import com.example.learnjapanese.ui.viewmodels.RegisterViewModel

// Add these imports at the top
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.TextStyle

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = viewModel.fullName,
            onValueChange = { viewModel.updateFullName(it.replace("\n", "")) },
            label = { Text("Full Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.updateEmail(it.replace("\n", "")) },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = viewModel.phone,
            onValueChange = { viewModel.updatePhone(it.replace("\n", "")) },
            label = { Text("Phone Number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.updatePassword(it.replace("\n", "")) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.updateConfirmPassword(it.replace("\n", "")) },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
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
                viewModel.register()
                onRegisterSuccess() 
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Register")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onBackClick) {
            Text("Already have an account? Login")
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