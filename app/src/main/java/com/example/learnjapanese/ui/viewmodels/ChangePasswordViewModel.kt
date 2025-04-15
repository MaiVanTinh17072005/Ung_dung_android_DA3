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
    
    fun updateNewPassword(password: String) {
        newPassword = password
    }
    
    fun updateConfirmPassword(password: String) {
        confirmPassword = password
    }
    
    fun changePassword() {
        // Implement password change logic here
        // This would typically involve calling a repository
    }
}