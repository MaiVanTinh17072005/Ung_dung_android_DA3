package com.example.learnjapanese.data.repository

import com.example.learnjapanese.data.dao.UserDao
import com.example.learnjapanese.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
    
    fun getUserById(userId: String): Flow<User?> {
        return userDao.getUserById(userId)
    }
    
    fun getUserByEmail(email: String): Flow<User?> {
        return userDao.getUserByEmail(email)
    }
    
    suspend fun updateDailyStreak(userId: String, streak: Int, lastActiveDate: Long) {
        userDao.updateDailyStreak(userId, streak, lastActiveDate)
    }
} 