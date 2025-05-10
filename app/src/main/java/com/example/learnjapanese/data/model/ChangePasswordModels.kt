package com.example.learnjapanese.data.model

data class ChangePasswordRequest(
    val newPassword: String
)

data class ChangePasswordResponse(
    val success: Boolean,
    val message: String
)