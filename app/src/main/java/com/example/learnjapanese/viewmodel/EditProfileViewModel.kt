package com.example.learnjapanese.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.UserProfile
import com.example.learnjapanese.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "EditProfileViewModel"

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Loading)
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _profile = MutableStateFlow(
        UserProfile(
            fullName = "",
            email = "",
            phone = "",
            bio = "",
            level = ""
        )
    )
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    fun loadUserProfile(email: String) {
        Log.d(TAG, "loadUserProfile: Starting to load profile for email: $email")
        viewModelScope.launch {
            _uiState.value = EditProfileUiState.Loading
            userRepository.getUserProfile(email)
                .onSuccess { userProfile ->
                    Log.d(TAG, "loadUserProfile: Successfully loaded profile: $userProfile")
                    _profile.value = userProfile
                    _uiState.value = EditProfileUiState.Success
                }
                .onFailure { error ->
                    Log.e(TAG, "loadUserProfile: Failed to load profile", error)
                    _uiState.value = EditProfileUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun updateProfile(
        fullName: String,
        email: String,
        phone: String,
        bio: String,
        level: String
    ) {
        Log.d(TAG, "updateProfile: Starting to update profile")
        viewModelScope.launch {
            _uiState.value = EditProfileUiState.Loading
            val updatedProfile = UserProfile(
                fullName = fullName,
                email = email,
                phone = phone,
                bio = bio,
                level = level,
                avatarUrl = _profile.value.avatarUrl
            )
            
            userRepository.updateUserProfile(updatedProfile)
                .onSuccess { userProfile ->
                    Log.d(TAG, "updateProfile: Successfully updated profile: $userProfile")
                    _profile.value = userProfile
                    _uiState.value = EditProfileUiState.Success
                }
                .onFailure { error ->
                    Log.e(TAG, "updateProfile: Failed to update profile", error)
                    _uiState.value = EditProfileUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}

sealed class EditProfileUiState {
    object Loading : EditProfileUiState()
    object Success : EditProfileUiState()
    data class Error(val message: String) : EditProfileUiState()
} 