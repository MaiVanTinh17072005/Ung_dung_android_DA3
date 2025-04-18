package com.example.learnjapanese.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    var fullName by mutableStateOf("")
        private set
    
    var email by mutableStateOf("")
        private set
    
    var phone by mutableStateOf("")
        private set
    
    var password by mutableStateOf("")
        private set
    
    var confirmPassword by mutableStateOf("")
        private set

    var fullNameError by mutableStateOf<String?>(null)
        private set
    
    var emailError by mutableStateOf<String?>(null)
        private set
    
    var phoneError by mutableStateOf<String?>(null)
        private set
    
    var passwordError by mutableStateOf<String?>(null)
        private set
    
    var confirmPasswordError by mutableStateOf<String?>(null)
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
    
    fun updateFullName(newFullName: String) {
        fullName = newFullName
        fullNameError = if (newFullName.isEmpty()) {
            "Vui lòng nhập họ tên"
        } else null
    }
    
    fun updateEmail(newEmail: String) {
        email = newEmail
        emailError = if (newEmail.isNotEmpty() && !isValidEmail(newEmail)) {
            "Vui lòng nhập địa chỉ email hợp lệ"
        } else null
    }
    
    fun updatePhone(newPhone: String) {
        phone = newPhone
        phoneError = if (newPhone.isEmpty()) {
            "Vui lòng nhập số điện thoại"
        } else null
    }
    
    fun updatePassword(newPassword: String) {
        password = newPassword
        passwordError = if (newPassword.isNotEmpty() && !isPasswordStrong(newPassword)) {
            "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
        } else null
    }
    
    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        confirmPasswordError = if (newConfirmPassword != password) {
            "Mật khẩu xác nhận không khớp"
        } else null
    }

    fun validateInputs(): Boolean {
        updateFullName(fullName)
        updateEmail(email)
        updatePhone(phone)
        updatePassword(password)
        updateConfirmPassword(confirmPassword)
        
        return fullNameError == null && 
               emailError == null && 
               phoneError == null && 
               passwordError == null && 
               confirmPasswordError == null
    }
    
    fun register() {
        if (validateInputs()) {
            // Thực hiện logic đăng ký ở đây
            // Thông thường sẽ gọi đến repository
        }
    }
}