import retrofit2.Response

class AuthRepository {
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        println("AuthRepository: Gọi API login với email: $email")
        val response = RetrofitClient.authApi.login(LoginRequest(email, password))
        println("AuthRepository: Nhận response - Status: ${response.code()}")
        return response
    }
}