import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApi {
    @POST("api/auth/login") // Đường dẫn API của bạn
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}