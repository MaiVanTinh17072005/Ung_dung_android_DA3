package com.example.learnjapanese.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.repository.AuthRepository
import kotlinx.coroutines.launch
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    companion object {
        private const val TAG = "ForgotPasswordViewModel"
    }

    var email by mutableStateOf("")
        private set
    
    var otp by mutableStateOf("")
        private set
    
    var emailError by mutableStateOf<String?>(null)
        private set
    
    var otpSent by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
    
    fun updateEmail(newEmail: String) {
        email = newEmail
        emailError = if (newEmail.isEmpty()) {
            "Vui lòng nhập địa chỉ email"
        } else if (!isValidEmail(newEmail)) {
            "Vui lòng nhập địa chỉ email hợp lệ. Ví dụ: tinh@gmail.com"
        } else null
    }
    
    fun updateOtp(newOtp: String) {
        otp = newOtp
    }

    fun sendOtp() {
        updateEmail(email)
        if (emailError == null) {
            viewModelScope.launch {
                try {
                    isLoading = true
                    errorMessage = null
                    
                    val response = authRepository.sendOtp(email)
                    
                    if (response.isSuccessful) {
                        response.body()?.let { otpResponse ->
                            if (otpResponse.success) {
                                otpSent = true
                                errorMessage = "Gửi OTP thành công" // Add success message
                            } else {
                                errorMessage = "Gửi OTP thất bại, do ${otpResponse.message}"
                            }
                        }
                    } else {
                        when (response.code()) {
                            404 -> errorMessage = "Email không tồn tại trong hệ thống"
                            400 -> errorMessage = "Email không hợp lệ"
                            500 -> errorMessage = "Lỗi máy chủ, vui lòng thử lại sau"
                            else -> errorMessage = "Gửi mã OTP thất bại: ${response.errorBody()?.string() ?: "Lỗi không xác định"}"
                        }
                    }
                } catch (e: Exception) {
                    errorMessage = "Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối mạng và thử lại."
                    Log.e(TAG, "Error sending OTP", e)
                } finally {
                    isLoading = false
                }
            }
        }
    }

    var otpVerified by mutableStateOf(false)
        private set

    fun verifyOtp() {
        if (otp.isBlank()) {
            errorMessage = "Vui lòng nhập mã OTP"
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                val response = authRepository.verifyOtp(email, otp)
                
                if (response.isSuccessful) {
                    response.body()?.let { otpResponse ->
                        if (otpResponse.success) {
                            errorMessage = "Xác thực OTP thành công"
                            otpVerified = true  // Add this flag
                        } else {
                            errorMessage = "Xác thực OTP thất bại: ${otpResponse.message}"
                            otpVerified = false
                        }
                    }
                } else {
                    when (response.code()) {
                        400 -> errorMessage = "Mã OTP không hợp lệ"
                        404 -> errorMessage = "Mã OTP không tồn tại"
                        410 -> errorMessage = "Mã OTP đã hết hạn"
                        500 -> errorMessage = "Lỗi máy chủ, vui lòng thử lại sau"
                        else -> errorMessage = "Xác thực OTP thất bại: ${response.errorBody()?.string() ?: "Lỗi không xác định"}"
                    }
                    otpVerified = false
                }
            } catch (e: Exception) {
                errorMessage = "Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối mạng và thử lại."
                Log.e(TAG, "Error verifying OTP", e)
                otpVerified = false
            } finally {
                isLoading = false
            }
        }
    }

    fun resetState() {
        errorMessage = null
        isLoading = false
    }
}