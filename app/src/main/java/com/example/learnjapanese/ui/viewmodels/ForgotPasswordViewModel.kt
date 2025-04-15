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
    
    var otpSent by mutableStateOf(false)
        private set
    
    fun updateEmail(newEmail: String) {
        email = newEmail
    }
    
    fun updateOtp(newOtp: String) {
        otp = newOtp
    }
    
    fun sendOtp() {
        // Implement OTP sending logic here
        // This would typically involve calling a repository
        otpSent = true
    }
    
    fun verifyOtp() {
        // Implement OTP verification logic here
        // This would typically involve calling a repository
    }
}