sealed class LoginState {
    object Initial : LoginState()           // Trạng thái ban đầu
    object Loading : LoginState()           // Đang loading
    data class Success(                     // Login thành công
        val token: String,
        val user: User
    ) : LoginState()
    data class Error(val message: String) : LoginState() // Có lỗi xảy ra
}