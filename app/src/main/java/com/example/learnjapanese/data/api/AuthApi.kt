package com.example.learnjapanese.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import android.util.Log
import com.example.learnjapanese.data.model.ChangePasswordRequest
import com.example.learnjapanese.data.model.ChangePasswordResponse
import com.example.learnjapanese.data.model.LoginRequest
import com.example.learnjapanese.data.model.LoginResponse
import com.example.learnjapanese.data.model.RegisterRequest
import com.example.learnjapanese.data.model.RegisterResponse
import com.example.learnjapanese.data.model.OtpRequest
import com.example.learnjapanese.data.model.OtpResponse
import com.example.learnjapanese.data.model.VerifyOtpRequest
import com.example.learnjapanese.data.model.GetProfileRequest
import com.example.learnjapanese.data.model.GetProfileResponse
import com.example.learnjapanese.data.model.SetProfileRequest
import com.example.learnjapanese.data.model.SetProfileResponse
import com.example.learnjapanese.data.model.UpdateAvatarRequest
import com.example.learnjapanese.data.model.UpdateAvatarResponse

interface AuthApi {
    companion object {
        private const val TAG = "AuthApi"
    }

    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/send-otp")
    suspend fun sendOtp(@Body otpRequest: OtpRequest): Response<OtpResponse>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body otpRequest: VerifyOtpRequest): Response<OtpResponse>

    @POST("api/auth/change-password")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse>

    @POST("api/auth/getprofile")
    suspend fun getProfile(@Body request: GetProfileRequest): Response<GetProfileResponse>

    @POST("api/auth/setprofile")
    suspend fun setProfile(@Body request: SetProfileRequest): Response<SetProfileResponse>

    @POST("api/auth/update-avatar")
    suspend fun updateAvatar(@Body request: UpdateAvatarRequest): Response<UpdateAvatarResponse>
}