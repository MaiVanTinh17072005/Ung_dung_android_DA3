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
    
    fun updateUsername(newUsername: String) {
        username = newUsername
    }
    
    fun updatePassword(newPassword: String) {
        password = newPassword
    }
    
    fun login() {
        // Implement login logic here
        // This would typically involve calling a repository
    }
}