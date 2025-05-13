package com.example.learnjapanese.data.model

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: UserDataRegiter? = null
)

data class UserDataRegiter(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String
)