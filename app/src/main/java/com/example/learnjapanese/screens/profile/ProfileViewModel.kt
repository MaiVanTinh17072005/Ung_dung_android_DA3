package com.example.learnjapanese.screens.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.local.UserPreferences
import com.example.learnjapanese.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    // Trạng thái UI
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    // Tải dữ liệu profile từ API
    fun loadProfileData() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                profileRepository.getProfile().collect { result ->
                    result.fold(
                        onSuccess = { profileData ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    displayName = profileData.display_name,
                                    email = profileData.email,
                                    phone = profileData.number_phone,
                                    avatarUrl = profileData.profile_image_url,
                                    error = null
                                )
                            }
                        },
                        onFailure = { error ->
                            Log.e(TAG, "Failed to load profile data: ${error.message}")
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = error.message ?: "Lỗi không xác định khi tải dữ liệu"
                                )
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi khi tải dữ liệu profile: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Lỗi không xác định khi tải dữ liệu"
                    )
                }
            }
        }
    }

    // Cập nhật giá trị displayName trong state
    fun updateDisplayName(name: String) {
        _uiState.update { it.copy(displayName = name) }
    }

    // Cập nhật giá trị email trong state
    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    // Cập nhật giá trị phone trong state
    fun updatePhone(phone: String) {
        _uiState.update { it.copy(phone = phone) }
    }

    // Cập nhật avatar tạm thời trong UI
    fun updateTempAvatar(uri: Uri?) {
        _uiState.update { it.copy(tempAvatarUri = uri) }
    }

    // Gửi dữ liệu profile lên API để cập nhật
    fun saveProfile() {
        val currentState = _uiState.value
        
        _uiState.update { it.copy(isLoading = true, error = null, successMessage = null) }
        
        viewModelScope.launch {
            try {
                profileRepository.updateProfile(
                    displayName = currentState.displayName,
                    email = currentState.email,
                    phone = currentState.phone
                ).collect { result ->
                    result.fold(
                        onSuccess = { message ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    successMessage = message
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = error.message ?: "Lỗi không xác định khi cập nhật thông tin",
                                    successMessage = null
                                )
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi khi cập nhật profile: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Lỗi không xác định khi cập nhật thông tin",
                        successMessage = null
                    )
                }
            }
        }
    }

    // Tải lên avatar mới
    fun uploadAvatar(uri: Uri) {
        _uiState.update { it.copy(
            isUploadingAvatar = true,
            avatarError = null,
            successMessage = null
        ) }
        
        viewModelScope.launch {
            try {
                profileRepository.updateAvatar(uri).collect { result ->
                    result.fold(
                        onSuccess = { message ->
                            _uiState.update {
                                it.copy(
                                    isUploadingAvatar = false,
                                    avatarError = null,
                                    successMessage = message,
                                    // Khi API thật được triển khai, avatarUrl sẽ được cập nhật từ response
                                    // Tạm thời sử dụng tempAvatarUri để hiển thị trước
                                    avatarUrl = null, // Đặt thành null để UI sử dụng tempAvatarUri
                                    tempAvatarUri = uri
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    isUploadingAvatar = false,
                                    avatarError = error.message ?: "Lỗi không xác định khi cập nhật avatar",
                                    successMessage = null
                                )
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi khi cập nhật avatar: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        isUploadingAvatar = false,
                        avatarError = e.message ?: "Lỗi không xác định khi cập nhật avatar",
                        successMessage = null
                    )
                }
            }
        }
    }

    // Đăng xuất
    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            userPreferences.clearUserData()
            onLogoutSuccess()
        }
    }
}

// State của màn hình Profile
data class ProfileUiState(
    val isLoading: Boolean = false,
    val displayName: String = "",
    val email: String = "",
    val phone: String = "",
    val avatarUrl: String? = null,
    val tempAvatarUri: Uri? = null,
    val isUploadingAvatar: Boolean = false,
    val error: String? = null,
    val avatarError: String? = null,
    val successMessage: String? = null
)