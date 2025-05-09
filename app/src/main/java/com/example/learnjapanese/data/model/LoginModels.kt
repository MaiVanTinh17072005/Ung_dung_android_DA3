package com.example.learnjapanese.data.model

// Request model gửi lên server
data class LoginRequest(
    val email: String,
    val password: String
)

// Response model nhận từ server
data class LoginResponse(
    val success: Boolean,
    val data: UserDataLogin,
    val message: String
)

data class UserDataLogin(
    val user_id: String,
    val email: String
)