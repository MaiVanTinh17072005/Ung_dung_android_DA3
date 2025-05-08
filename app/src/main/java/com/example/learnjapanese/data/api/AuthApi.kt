import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import android.util.Log
import com.example.learnjapanese.data.model.LoginRequest
import com.example.learnjapanese.data.model.LoginResponse

interface AuthApi {
    companion object {
        private const val TAG = "AuthApi"
    }

    @POST("api/auth/login") // Đường dẫn API của bạn
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse> {
        Log.d(TAG, "Calling login API with email: ${loginRequest.email}")
        return this.login(loginRequest)
    }
}