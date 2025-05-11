package com.example.learnjapanese.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.util.Log
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {

    companion object {
        private const val TAG = "UserPreferences"
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_EMAIL = stringPreferencesKey("user_email")
    }

    val userId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }

    suspend fun saveUserData(userId: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_EMAIL] = email
        }
        // In ra dữ liệu sau khi lưu
        debugPrintDataStore()
    }

    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(USER_EMAIL)
        }
        // In ra dữ liệu sau khi xóa
        debugPrintDataStore()
    }

    // Hàm debug để in ra dữ liệu trong DataStore
    suspend fun debugPrintDataStore() {
        // Lấy đường dẫn của DataStore
        val dataStoreFile = File(context.filesDir, "datastore/user_preferences.preferences_pb")
        val dataStorePath = dataStoreFile.absolutePath
        
        context.dataStore.data.collect { preferences ->
            val userId = preferences[USER_ID]
            val userEmail = preferences[USER_EMAIL]
            Log.d(TAG, "DataStore Information:")
            Log.d(TAG, "File Path: $dataStorePath")
            Log.d(TAG, "File Exists: ${dataStoreFile.exists()}")
            Log.d(TAG, "File Size: ${if (dataStoreFile.exists()) dataStoreFile.length() else 0} bytes")
            Log.d(TAG, "DataStore Contents:")
            Log.d(TAG, "User ID: $userId")
            Log.d(TAG, "User Email: $userEmail")
        }
    }
}