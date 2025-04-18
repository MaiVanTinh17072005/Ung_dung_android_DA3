package com.example.learnjapanese.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ChangePasswordViewModel : ViewModel() {
    var newPassword by mutableStateOf("")
        private set
    
    var confirmPassword by mutableStateOf("")
        private set

    var newPasswordError by mutableStateOf<String?>(null)
        private set
        
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    private fun isPasswordStrong(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val isLongEnough = password.length >= 8
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar && isLongEnough
    }
    
    fun updateNewPassword(password: String) {
        newPassword = password
        newPasswordError = if (password.isEmpty()) {
            "Vui lòng nhập mật khẩu mới"
        } else if (!isPasswordStrong(password)) {
            "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
        } else null
    }
    
    fun updateConfirmPassword(password: String) {
        confirmPassword = password
        confirmPasswordError = if (password.isEmpty()) {
            "Vui lòng xác nhận mật khẩu"
        } else if (password != newPassword) {
            "Mật khẩu xác nhận không khớp"
        } else null
    }

    fun validateInputs(): Boolean {
        updateNewPassword(newPassword)
        updateConfirmPassword(confirmPassword)
        return newPasswordError == null && confirmPasswordError == null
    }
    
    fun changePassword() {
        if (validateInputs()) {
            // Thực hiện logic thay đổi mật khẩu ở đây
            // Thông thường sẽ gọi đến repository
        }
    }
}