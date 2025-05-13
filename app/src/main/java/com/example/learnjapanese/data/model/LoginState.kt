package com.example.learnjapanese.data.model


sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    data class Success(val userData: UserDataLogin) : LoginState()
    data class Error(val message: String) : LoginState()
}