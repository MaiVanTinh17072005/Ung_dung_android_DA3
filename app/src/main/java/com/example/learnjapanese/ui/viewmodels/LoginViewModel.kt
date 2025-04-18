package com.example.learnjapanese.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set
    
    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set
        
    var passwordError by mutableStateOf<String?>(null)
        private set

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isPasswordStrong(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val isLongEnough = password.length >= 8
        
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar && isLongEnough
    }
    
    fun updateUsername(newUsername: String) {
        username = newUsername
        emailError = if (newUsername.isNotEmpty() && !isValidEmail(newUsername)) {
            "Vui lòng nhập địa chỉ email hợp lệ"
        } else null
    }
    
    fun updatePassword(newPassword: String) {
        password = newPassword
        passwordError = if (newPassword.isNotEmpty() && !isPasswordStrong(newPassword)) {
            "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
        } else null
    }
    
    fun validateInputs(): Boolean {
        emailError = if (username.isEmpty()) {
            "Email không được để trống"
        } else if (!isValidEmail(username)) {
            "Vui lòng nhập địa chỉ email hợp lệ"
        } else null

        passwordError = if (password.isEmpty()) {
            "Mật khẩu không được để trống"
        } else if (!isPasswordStrong(password)) {
            "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
        } else null

        return emailError == null && passwordError == null
    }
    
    fun login() {
        if (validateInputs()) {
            // Thực hiện logic đăng nhập ở đây
            // Thông thường sẽ gọi đến repository
        }
    }
}