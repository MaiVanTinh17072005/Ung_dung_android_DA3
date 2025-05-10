import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import android.util.Log
import com.example.learnjapanese.data.model.LoginRequest
import com.example.learnjapanese.data.model.LoginResponse
import com.example.learnjapanese.data.model.RegisterRequest
import com.example.learnjapanese.data.model.RegisterResponse
import com.example.learnjapanese.data.model.OtpRequest
import com.example.learnjapanese.data.model.OtpResponse
import com.example.learnjapanese.data.model.VerifyOtpRequest

interface AuthApi {
    companion object {
        private const val TAG = "AuthApi"
    }

    @POST("api/auth/login") // Đường dẫn API của bạn
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse> {
        Log.d(TAG, "Calling login API with email: ${loginRequest.email}")
        return this.login(loginRequest)
    }

    @POST("api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/send-otp")
    suspend fun sendOtp(@Body otpRequest: OtpRequest): Response<OtpResponse>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body otpRequest: VerifyOtpRequest): Response<OtpResponse>
}