package com.example.learnjapanese.ui.viewmodels

// Sửa lại các import
import com.example.learnjapanese.data.repository.AuthRepository
import com.example.learnjapanese.data.model.LoginState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import java.security.MessageDigest
import java.nio.charset.StandardCharsets

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    companion object {
        private const val TAG = "LoginViewModel"
    }

    var username by mutableStateOf("")
        private set
    
    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set
        
    var passwordError by mutableStateOf<String?>(null)
        private set

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isPasswordStrong(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val isLongEnough = password.length >= 8
        
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar && isLongEnough
    }
    
    private fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> 
                "Email không được để trống"
            !email.contains("@") -> 
                "Email phải chứa 1 ký tự @ và 1 ký tự ."
            email.contains(" ") -> 
                "Email không được chứa khoảng trắng"
            email.matches(".*[!#\$%^&*()+=\\[\\]{}|;:\"'<>?~`].*".toRegex()) ->
                "Email không được chứa ký tự đặc biệt (chỉ cho phép @ và .)"
            !email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+".toRegex()) ->
                "Ví dụ email hợp lệ: example@gmail.com"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> 
                "Mật khẩu không được để trống"
            !password.any { it.isUpperCase() } -> 
                "Mật khẩu phải chứa ít nhất 1 chữ hoa"
            !password.any { it.isLowerCase() } -> 
                "Mật khẩu phải chứa ít nhất 1 chữ thường"
            !password.any { it.isDigit() } -> 
                "Mật khẩu phải chứa ít nhất 1 chữ số"
            !password.any { !it.isLetterOrDigit() } -> 
                "Mật khẩu phải chứa ít nhất 1 ký tự đặc biệt (!@#$%^&*...)"
            password.length < 8 ->
                "Mật khẩu phải có ít nhất 8 ký tự"
            else -> null
        }
    }

    fun updateUsername(newUsername: String) {
        username = newUsername
        emailError = validateEmail(newUsername)
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        passwordError = validatePassword(newPassword)
    }

    fun validateInputs(): Boolean {
        emailError = validateEmail(username)
        passwordError = validatePassword(password)
        return emailError == null && passwordError == null
    }

    // Thêm state để theo dõi trạng thái login
    private val _loginState = mutableStateOf<LoginState>(LoginState.Initial)
    val loginState: LoginState by _loginState

    private fun hashPassword(password: String): String {
        Log.d(TAG, "Original password: $password")
        val bytes = password.encodeToByteArray()
        Log.d(TAG, "Password bytes: ${bytes.joinToString(", ") { it.toString() }}")
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hash = digest.joinToString("") { "%02x".format(it) }
        Log.d(TAG, "Generated hash: $hash")
        return hash
    }

    fun login() {
        if (validateInputs()) {
            Log.d(TAG, "Starting login process for user: $username")
            viewModelScope.launch {
                try {
                    _loginState.value = LoginState.Loading
                    Log.d(TAG, "Login state changed to Loading")

                    val hashedPassword = hashPassword(password)
                    val response = authRepository.login(username, hashedPassword)
                    Log.d(TAG, "Received API response with status: ${response.code()}")

                    if (response.isSuccessful) {
                        response.body()?.let { loginResponse ->
                            if (loginResponse.success && loginResponse.data != null) {
                                _loginState.value = LoginState.Success(
                                    userData = loginResponse.data
                                )
                            } else {
                                _loginState.value = LoginState.Error("đăng nhập thất bại")
                            }
                        }
                    } else {
                        _loginState.value = LoginState.Error("đăng nhập thất bại") 
                    }
                } catch (e: Exception) {
                    val errorMessage = "không thể kết nối đến server" // Changed and simplified
                    Log.e(TAG, errorMessage, e)
                    _loginState.value = LoginState.Error(errorMessage)
                }
            }
        } else {
            Log.w(TAG, "Login validation failed - Email error: $emailError, Password error: $passwordError")
        }
    }

    fun resetAllNotifications() {
        emailError = null
        passwordError = null
        _loginState.value = LoginState.Initial
    }
}