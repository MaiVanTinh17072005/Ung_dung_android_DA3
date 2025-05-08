package com.example.learnjapanese.data.repository

import retrofit2.Response
import android.util.Log
import com.example.learnjapanese.data.model.LoginRequest
import com.example.learnjapanese.data.model.LoginResponse

class AuthRepository {
    companion object {
        private const val TAG = "AuthRepository"
    }

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        Log.d(TAG, "AuthRepository: Starting login process for email: $email")
        try {
            val response = RetrofitClient.authApi.login(LoginRequest(email, password))
            Log.d(TAG, "AuthRepository: API Response - Status: ${response.code()}")
            if (response.isSuccessful) {
                Log.d(TAG, "AuthRepository: Login successful")
            } else {
                Log.e(TAG, "AuthRepository: Login failed with error: ${response.errorBody()?.string()}")
            }
            return response
        } catch (e: Exception) {
            Log.e(TAG, "AuthRepository: Exception during login", e)
            throw e
        }
    }
}