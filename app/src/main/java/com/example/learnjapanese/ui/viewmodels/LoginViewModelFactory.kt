package com.example.learnjapanese.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnjapanese.data.local.UserPreferences
import com.example.learnjapanese.data.repository.AuthRepository

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val userPreferences = UserPreferences(context)
            val authRepository = AuthRepository()
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 