// Request model gửi lên server
data class LoginRequest(
    val email: String,
    val password: String
)

// Response model nhận từ server
data class LoginResponse(
    val token: String,
    val user: User
)

data class User(
    val id: String,
    val email: String,
    val name: String
    // Thêm các trường khác nếu cần
)