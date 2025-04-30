package com.example.learnjapanese.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String? = null,
    val joinDate: Long = System.currentTimeMillis(),
    val dailyStreak: Int = 0,
    val lastActiveDate: Long = System.currentTimeMillis()
) 