package com.example.learnjapanese.data.model

data class OtpRequest(
    val email: String
)

data class OtpResponse(
    val success: Boolean,
    val message: String,
    val data: OtpData? = null
)

data class OtpData(
    val otp: String,
    val expiresAt: String
) 