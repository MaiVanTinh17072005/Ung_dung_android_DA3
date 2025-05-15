package com.example.learnjapanese.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

// Tạo một extension function cho Context để tạo DataStore một lần duy nhất
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val TAG = "UserPreferences"
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
    }

    // Lưu dữ liệu người dùng
    suspend fun saveUserData(userId: String, email: String, token: String = "") {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[EMAIL_KEY] = email
            if (token.isNotEmpty()) {
                preferences[TOKEN_KEY] = token
            }
        }
        // In ra dữ liệu sau khi lưu
        debugPrintDataStore()
    }

    // Lấy ID người dùng
    val userId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    // Lấy email người dùng
    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[EMAIL_KEY]
    }

    // Lấy token xác thực
    val authToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    // Xóa tất cả dữ liệu người dùng (đăng xuất)
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
        // In ra dữ liệu sau khi xóa
        debugPrintDataStore()
    }

    // Hàm debug để in ra dữ liệu trong DataStore
    suspend fun debugPrintDataStore() {
        try {
        // Lấy đường dẫn của DataStore
        val dataStoreFile = File(context.filesDir, "datastore/user_preferences.preferences_pb")
        val dataStorePath = dataStoreFile.absolutePath
        
            // Sử dụng first() thay vì collect để chỉ lấy giá trị hiện tại
            val preferences = context.dataStore.data.first()
            val userId = preferences[USER_ID_KEY]
            val userEmail = preferences[EMAIL_KEY]
            val token = preferences[TOKEN_KEY]
            
            Log.d(TAG, "DataStore Information:")
            Log.d(TAG, "File Path: $dataStorePath")
            Log.d(TAG, "File Exists: ${dataStoreFile.exists()}")
            Log.d(TAG, "File Size: ${if (dataStoreFile.exists()) dataStoreFile.length() else 0} bytes")
            Log.d(TAG, "DataStore Contents:")
            Log.d(TAG, "User ID: $userId")
            Log.d(TAG, "User Email: $userEmail")
            Log.d(TAG, "Token: $token")
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi đọc DataStore: ${e.message}", e)
        }
    }
}