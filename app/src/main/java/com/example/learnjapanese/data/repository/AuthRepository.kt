package com.example.learnjapanese.data.repository

import retrofit2.Response
import android.util.Log
import com.example.learnjapanese.data.api.AuthApi
import com.example.learnjapanese.data.model.ChangePasswordRequest
import com.example.learnjapanese.data.model.ChangePasswordResponse
import com.example.learnjapanese.data.model.LoginRequest
import com.example.learnjapanese.data.model.LoginResponse
import com.example.learnjapanese.data.model.RegisterRequest
import com.example.learnjapanese.data.model.RegisterResponse
import com.example.learnjapanese.data.model.OtpRequest
import com.example.learnjapanese.data.model.OtpResponse
import com.example.learnjapanese.data.model.VerifyOtpRequest
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {
    companion object {
        private const val TAG = "AuthRepository"
    }

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        Log.d(TAG, "AuthRepository: Starting login process for email: $email")
        try {
            val response = authApi.login(LoginRequest(email, password))
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

    suspend fun register(fullName: String, email: String, phone: String, password: String): Response<RegisterResponse> {
        Log.d(TAG, "AuthRepository: Starting registration process for email: $email")
        try {
            // Mã hóa mật khẩu bằng SHA-256
            val hashedPassword = hashPassword(password)
            
            val registerRequest = RegisterRequest(
                fullName = fullName,
                email = email,
                phone = phone,
                password = hashedPassword
            )
            
            val response = authApi.register(registerRequest)
            Log.d(TAG, "AuthRepository: API Response - Status: ${response.code()}")
            
            if (response.isSuccessful) {
                Log.d(TAG, "AuthRepository: Registration successful")
            } else {
                Log.e(TAG, "AuthRepository: Registration failed with error: ${response.errorBody()?.string()}")
            }
            return response
        } catch (e: Exception) {
            Log.e(TAG, "AuthRepository: Exception during registration", e)
            throw e
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun sendOtp(email: String): Response<OtpResponse> {
        Log.d(TAG, "AuthRepository: Starting OTP sending process for email: $email")
        try {
            val response = authApi.sendOtp(OtpRequest(email))
            Log.d(TAG, "AuthRepository: API Response - Status: ${response.code()}")
            if (response.isSuccessful) {
                Log.d(TAG, "AuthRepository: OTP sent successfully")
            } else {
                Log.e(TAG, "AuthRepository: OTP sending failed with error: ${response.errorBody()?.string()}")
            }
            return response
        } catch (e: Exception) {
            Log.e(TAG, "AuthRepository: Exception during OTP sending", e)
            throw e
        }
    }

    suspend fun verifyOtp(email: String, otp: String): Response<OtpResponse> {
        Log.d(TAG, "AuthRepository: Starting OTP verification process for email: $email")
        try {
            val response = authApi.verifyOtp(VerifyOtpRequest(email, otp))
            Log.d(TAG, "AuthRepository: API Response - Status: ${response.code()}")
            if (response.isSuccessful) {
                Log.d(TAG, "AuthRepository: OTP verified successfully")
            } else {
                Log.e(TAG, "AuthRepository: OTP verification failed with error: ${response.errorBody()?.string()}")
            }
            return response
        } catch (e: Exception) {
            Log.e(TAG, "AuthRepository: Exception during OTP verification", e)
            throw e
        }
    }

    suspend fun changePassword(newPassword: String): Response<ChangePasswordResponse> {
        Log.d(TAG, "AuthRepository: Starting change password process")
        try {
            // Hash the new password
            val hashedPassword = hashPassword(newPassword)
            
            val response = authApi.changePassword(
                ChangePasswordRequest(hashedPassword)
            )
            
            Log.d(TAG, "AuthRepository: API Response - Status: ${response.code()}")
            if (response.isSuccessful) {
                Log.d(TAG, "AuthRepository: Password changed successfully")
            } else {
                Log.e(TAG, "AuthRepository: Password change failed with error: ${response.errorBody()?.string()}")
            }
            return response
        } catch (e: Exception) {
            Log.e(TAG, "AuthRepository: Exception during password change", e)
            throw e
        }
    }
}