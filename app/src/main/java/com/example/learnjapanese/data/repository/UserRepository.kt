package com.example.learnjapanese.data.repository

import android.util.Log
import com.example.learnjapanese.data.api.UserApiService
import com.example.learnjapanese.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UserRepository"

@Singleton
class UserRepository @Inject constructor(
    private val userApiService: UserApiService
) {

    suspend fun getUserProfile(email: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        Log.d(TAG, "getUserProfile: Starting to fetch user profile for email: $email")
        try {
            Log.d(TAG, "getUserProfile: Making API call to getUserProfile")
            val response = userApiService.getUserProfile(email)
            
            if (response.isSuccessful) {
                Log.d(TAG, "getUserProfile: API call successful with code ${response.code()}")
                response.body()?.let {
                    Log.d(TAG, "getUserProfile: Successfully parsed response body: $it")
                    Result.success(it)
                } ?: run {
                    Log.e(TAG, "getUserProfile: Empty response body")
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Log.e(TAG, "getUserProfile: API call failed with code ${response.code()}")
                Log.e(TAG, "getUserProfile: Error body: ${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to get user profile: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getUserProfile: Exception occurred", e)
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(profile: UserProfile): Result<UserProfile> = withContext(Dispatchers.IO) {
        Log.d(TAG, "updateUserProfile: Starting to update user profile with data: $profile")
        try {
            Log.d(TAG, "updateUserProfile: Making API call to updateUserProfile")
            val response = userApiService.updateUserProfile(profile)
            
            if (response.isSuccessful) {
                Log.d(TAG, "updateUserProfile: API call successful with code ${response.code()}")
                response.body()?.let {
                    Log.d(TAG, "updateUserProfile: Successfully parsed response body: $it")
                    Result.success(it)
                } ?: run {
                    Log.e(TAG, "updateUserProfile: Empty response body")
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Log.e(TAG, "updateUserProfile: API call failed with code ${response.code()}")
                Log.e(TAG, "updateUserProfile: Error body: ${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to update user profile: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateUserProfile: Exception occurred", e)
            Result.failure(e)
        }
    }
} 