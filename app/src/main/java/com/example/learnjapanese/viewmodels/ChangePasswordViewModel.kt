package com.example.learnjapanese.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.learnjapanese.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var newPassword by mutableStateOf("")
        private set
    
    var confirmPassword by mutableStateOf("")
        private set

    var newPasswordError by mutableStateOf<String?>(null)
        private set
        
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    private fun validatePasswordStrength(password: String): String? {
        if (password.isEmpty()) {
            return "Vui lòng nhập mật khẩu mới"
        }
        
        val errors = mutableListOf<String>()
        
        if (password.length < 8) {
            errors.add("ít nhất 8 ký tự")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.add("chữ hoa")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.add("chữ thường")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("số")
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            errors.add("ký tự đặc biệt")
        }
        
        return if (errors.isEmpty()) null else "Mật khẩu phải có ${errors.joinToString(", ")}"
    }
    
    fun updateNewPassword(password: String) {
        newPassword = password
        newPasswordError = validatePasswordStrength(password)
    }
    
    fun updateConfirmPassword(password: String) {
        confirmPassword = password
        confirmPasswordError = if (password.isEmpty()) {
            "Vui lòng xác nhận mật khẩu"
        } else if (password != newPassword) {
            "Mật khẩu xác nhận không khớp"
        } else null
    }

    fun validateInputs(): Boolean {
        updateNewPassword(newPassword)
        updateConfirmPassword(confirmPassword)
        return newPasswordError == null && confirmPasswordError == null
    }
    
    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading

    private var _error by mutableStateOf<String?>(null)
    val error: String? get() = _error

    private var _isSuccess by mutableStateOf(false)
    val isSuccess: Boolean get() = _isSuccess

    private var _showSuccessMessage by mutableStateOf(false)
    val showSuccessMessage: Boolean get() = _showSuccessMessage

    fun changePassword() {
        if (validateInputs()) {
            viewModelScope.launch {
                _isLoading = true
                _error = null
                _isSuccess = false
                _showSuccessMessage = false
                try {
                    val response = authRepository.changePassword(newPassword)
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!
                        if (responseBody.success) {
                            _isSuccess = true
                            _showSuccessMessage = true
                            _error = null
                        } else {
                            _error = responseBody.message
                        }
                    } else {
                        when (response.code()) {
                            400 -> _error = "Mật khẩu không hợp lệ"
                            401 -> _error = "Phiên đăng nhập đã hết hạn"
                            500 -> _error = "Lỗi máy chủ, vui lòng thử lại sau"
                            else -> _error = "Không thể đổi mật khẩu. Vui lòng thử lại sau."
                        }
                    }
                } catch (e: Exception) {
                    _error = "Đã xảy ra lỗi: ${e.message}"
                } finally {
                    _isLoading = false
                }
            }
        }
    }

    fun hideSuccessMessage() {
        _showSuccessMessage = false
    }
}