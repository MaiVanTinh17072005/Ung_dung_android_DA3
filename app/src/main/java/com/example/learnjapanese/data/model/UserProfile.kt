package com.example.learnjapanese.data.model

data class UserProfile(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val bio: String = "",
    val level: String = "",
    val avatarUrl: String? = null
) 