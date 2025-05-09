package com.example.learnjapanese.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ForgotPasswordViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set
    
    var otp by mutableStateOf("")
        private set
    
    var emailError by mutableStateOf<String?>(null)
        private set
    
    var otpSent by mutableStateOf(false)
        private set

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
    
    fun updateEmail(newEmail: String) {
        email = newEmail
        emailError = if (newEmail.isEmpty()) {
            "Vui lòng nhập địa chỉ email"
        } else if (!isValidEmail(newEmail)) {
            "Vui lòng nhập địa chỉ email hợp lệ. Ví dụ: tinh@gmail.com"
        } else null
    }
    
    fun updateOtp(newOtp: String) {
        otp = newOtp
    }

    fun sendOtp() {
        updateEmail(email)
        if (emailError == null) {
            // TODO: Implement actual OTP sending logic here
            otpSent = true
        }
    }

    fun verifyOtp() {
        // TODO: Implement OTP verification logic here
    }
}