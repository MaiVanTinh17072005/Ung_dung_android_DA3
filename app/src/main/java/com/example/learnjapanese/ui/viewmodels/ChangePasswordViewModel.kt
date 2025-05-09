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

    private fun validatePasswordStrength(password: String): String? {
        if (password.isEmpty()) {
            return "Vui lòng nhập mật khẩu mới"
        }
        
        val errors = mutableListOf<String>()
        
        if (password.length < 8) {
            errors.add("ít nhất 8 ký tự")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.add("chữ hoa")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.add("chữ thường")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("số")
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            errors.add("ký tự đặc biệt")
        }
        
        return if (errors.isEmpty()) null else "Mật khẩu phải có ${errors.joinToString(", ")}"
    }
    
    fun updateNewPassword(password: String) {
        newPassword = password
        newPasswordError = validatePasswordStrength(password)
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