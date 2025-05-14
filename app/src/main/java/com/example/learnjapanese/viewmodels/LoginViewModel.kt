package com.example.learnjapanese.viewmodels

import com.example.learnjapanese.data.repository.AuthRepository
import com.example.learnjapanese.data.model.LoginState
import com.example.learnjapanese.data.local.UserPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import java.security.MessageDigest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
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
        Log.d(TAG, "Bắt đầu quá trình đăng nhập - Kiểm tra dữ liệu đầu vào")
        if (validateInputs()) {
            Log.d(TAG, "Dữ liệu đầu vào hợp lệ, bắt đầu xử lý đăng nhập cho người dùng: $username")
            viewModelScope.launch {
                try {
                    Log.d(TAG, "Trước khi đặt Loading - Trạng thái hiện tại: ${_loginState.value}")
                    _loginState.value = LoginState.Loading
                    Log.d(TAG, "Sau khi đặt Loading - Trạng thái hiện tại: ${_loginState.value}")
                    Log.d(TAG, "Đặt trạng thái đăng nhập sang Loading")

                    val hashedPassword = hashPassword(password)
                    Log.d(TAG, "Đã mã hóa mật khẩu thành công, đang gửi yêu cầu đến server")
                    val response = authRepository.login(username, hashedPassword)
                    Log.d(TAG, "Đã nhận phản hồi từ API với mã trạng thái: ${response.code()}")

                    if (response.isSuccessful) {
                        Log.d(TAG, "Yêu cầu API thành công với mã: ${response.code()}")
                        response.body()?.let { loginResponse ->
                            Log.d(TAG, "Dữ liệu phản hồi: success=${loginResponse.success}, data=${loginResponse.data != null}")
                            if (loginResponse.success && loginResponse.data != null) {
                                Log.d(TAG, "Đăng nhập thành công, lưu dữ liệu người dùng vào DataStore")
                                try {
                                    // Lưu dữ liệu vào DataStore trước
                                    userPreferences.saveUserData(
                                        userId = loginResponse.data.user_id,
                                        email = loginResponse.data.email
                                    )
                                    Log.d(TAG, "Đã lưu dữ liệu người dùng thành công")
                                } catch (e: Exception) {
                                    Log.e(TAG, "Lỗi khi lưu dữ liệu vào DataStore: ${e.message}", e)
                                }
                                
                                Log.d(TAG, "Tiến hành đặt trạng thái Success")
                                try {
                                    // Kiểm tra trạng thái trước khi cập nhật
                                    Log.d(TAG, "Trước khi đặt Success - Trạng thái hiện tại: ${_loginState.value}")
                                    
                                    // Tạo đối tượng Success trước
                                    val successState = LoginState.Success(userData = loginResponse.data)
                                    Log.d(TAG, "Đã tạo đối tượng Success: $successState")
                                    
                                    // Sau đó mới chuyển trạng thái thành công
                                    _loginState.value = successState
                                    
                                    // Kiểm tra trạng thái sau khi cập nhật
                                    Log.d(TAG, "Sau khi đặt Success - Trạng thái hiện tại: ${_loginState.value}")
                                    Log.d(TAG, "Đã đặt trạng thái thành Success thành công")
                                    
                                    // Đảm bảo recomposition xảy ra
                                    delay(100)
                                    Log.d(TAG, "Sau delay - Trạng thái hiện tại: ${_loginState.value}")
                                } catch (e: Exception) {
                                    Log.e(TAG, "Lỗi khi đặt trạng thái Success: ${e.message}", e) 
                                }
                                
                                Log.d(TAG, "Đăng nhập thành công hoàn toàn, trạng thái hiện tại: ${_loginState.value}")
                            } else {
                                Log.e(TAG, "Đăng nhập thất bại với thông báo từ server: ${loginResponse.message}")
                                _loginState.value = LoginState.Error("Đăng nhập thất bại: ${loginResponse.message}")
                                Log.d(TAG, "Đã đặt trạng thái Error với message từ server")
                            }
                        } ?: run {
                            Log.e(TAG, "Đăng nhập thất bại: Không nhận được dữ liệu từ server")
                            _loginState.value = LoginState.Error("Đăng nhập thất bại: Không nhận được dữ liệu từ server")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Không xác định"
                        Log.e(TAG, "Yêu cầu API thất bại với mã ${response.code()}: $errorBody")
                        _loginState.value = LoginState.Error("Đăng nhập thất bại: $errorBody")
                    }
                } catch (e: Exception) {
                    val errorMessage = "Không thể kết nối đến server: ${e.message}"
                    Log.e(TAG, "Ngoại lệ xảy ra khi đăng nhập: $errorMessage", e)
                    _loginState.value = LoginState.Error(errorMessage)
                }
            }
        } else {
            Log.w(TAG, "Xác thực đầu vào thất bại - Lỗi email: $emailError, Lỗi mật khẩu: $passwordError")
            val errorMessage = when {
                emailError != null -> emailError
                passwordError != null -> passwordError
                else -> "Vui lòng kiểm tra lại thông tin đăng nhập"
            }
            Log.w(TAG, "Đặt trạng thái lỗi với thông báo: $errorMessage")
            _loginState.value = LoginState.Error(errorMessage!!)
        }
    }

    fun resetAllNotifications() {
        emailError = null
        passwordError = null
        _loginState.value = LoginState.Initial
    }
}