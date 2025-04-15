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
    
    fun updateFullName(newFullName: String) {
        fullName = newFullName
    }
    
    fun updateEmail(newEmail: String) {
        email = newEmail
    }
    
    fun updatePhone(newPhone: String) {
        phone = newPhone
    }
    
    fun updatePassword(newPassword: String) {
        password = newPassword
    }
    
    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
    }
    
    fun register() {
        // Implement registration logic here
        // This would typically involve calling a repository
    }
}