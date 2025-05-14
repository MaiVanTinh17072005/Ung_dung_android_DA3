package com.example.learnjapanese.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import com.example.learnjapanese.data.api.AuthApi
import com.example.learnjapanese.data.local.UserPreferences
import com.example.learnjapanese.data.model.GetProfileRequest
import com.example.learnjapanese.data.model.SetProfileRequest
import com.example.learnjapanese.data.model.UpdateAvatarRequest
import com.example.learnjapanese.data.model.UserProfileData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val authApi: AuthApi,
    private val userPreferences: UserPreferences,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "ProfileRepository"
        private const val MAX_IMAGE_DIMENSION = 500 // Kích thước tối đa cho avatar
    }

    // Lấy thông tin profile từ API
    suspend fun getProfile(): Flow<Result<UserProfileData>> = flow {
        try {
            val userId = userPreferences.userId.first()
            if (userId.isNullOrEmpty()) {
                emit(Result.failure(Exception("User ID không hợp lệ")))
                return@flow
            }
            
            val response = authApi.getProfile(GetProfileRequest(userId))
            
            if (response.isSuccessful && response.body()?.success == true) {
                val profileData = response.body()?.data
                if (profileData != null) {
                    emit(Result.success(profileData))
                } else {
                    emit(Result.failure(Exception("Không tìm thấy dữ liệu người dùng")))
                }
            } else {
                emit(Result.failure(Exception(response.body()?.message ?: "Lỗi khi lấy thông tin profile")))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi lấy thông tin profile: ${e.message}", e)
            emit(Result.failure(e))
        }
    }

    // Cập nhật thông tin profile
    suspend fun updateProfile(
        displayName: String,
        email: String,
        phone: String
    ): Flow<Result<String>> = flow {
        try {
            // Kiểm tra các thông tin trước khi gửi API
            if (displayName.isBlank()) {
                emit(Result.failure(Exception("Họ và tên không được để trống")))
                return@flow
            }
            
            if (!isValidEmail(email)) {
                emit(Result.failure(Exception("Email không hợp lệ")))
                return@flow
            }
            
            if (!isValidPhoneNumber(phone)) {
                emit(Result.failure(Exception("Số điện thoại không hợp lệ")))
                return@flow
            }
            
            val userId = userPreferences.userId.first()
            if (userId.isNullOrEmpty()) {
                emit(Result.failure(Exception("User ID không hợp lệ")))
                return@flow
            }
            
            val request = SetProfileRequest(
                userId = userId,
                email = email,
                number_phone = phone,
                display_name = displayName
            )
            
            val response = authApi.setProfile(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                emit(Result.success(response.body()?.message ?: "Cập nhật thông tin thành công"))
            } else {
                emit(Result.failure(Exception(response.body()?.message ?: "Lỗi khi cập nhật thông tin")))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi cập nhật thông tin profile: ${e.message}", e)
            emit(Result.failure(e))
        }
    }
    
    // Cập nhật avatar
    suspend fun updateAvatar(imageUri: Uri): Flow<Result<String>> = flow {
        try {
            val userId = userPreferences.userId.first()
            if (userId.isNullOrEmpty()) {
                emit(Result.failure(Exception("User ID không hợp lệ")))
                return@flow
            }
            
            // Chuyển đổi URI thành Base64 string
            val base64Image = convertImageUriToBase64(imageUri)
            if (base64Image == null) {
                emit(Result.failure(Exception("Không thể xử lý hình ảnh")))
                return@flow
            }
            
            // TODO: Khi API được triển khai, bỏ comment đoạn code bên dưới
            /*
            val request = UpdateAvatarRequest(
                user_id = userId,
                avatar_data = base64Image
            )
            
            val response = authApi.updateAvatar(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                emit(Result.success(response.body()?.message ?: "Cập nhật avatar thành công"))
            } else {
                emit(Result.failure(Exception(response.body()?.message ?: "Lỗi khi cập nhật avatar")))
            }
            */
            
            // Tạm thời trả về thành công để demo UI
            emit(Result.success("Cập nhật avatar thành công (API chưa được triển khai)"))
            
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi cập nhật avatar: ${e.message}", e)
            emit(Result.failure(e))
        }
    }
    
    // Chuyển đổi URI thành chuỗi Base64
    private fun convertImageUriToBase64(imageUri: Uri): String? {
        return try {
            // Đọc bitmap từ URI
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            
            // Resize bitmap để giảm kích thước
            val resizedBitmap = resizeBitmap(bitmap)
            
            // Chuyển đổi bitmap thành Base64
            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi chuyển đổi ảnh sang Base64: ${e.message}", e)
            null
        }
    }
    
    // Resize bitmap để giảm kích thước
    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        // Tính toán tỷ lệ co
        val ratio: Float = when {
            width > height -> MAX_IMAGE_DIMENSION.toFloat() / width.toFloat()
            else -> MAX_IMAGE_DIMENSION.toFloat() / height.toFloat()
        }
        
        // Nếu kích thước đã nhỏ hơn giới hạn, giữ nguyên
        if (ratio >= 1) {
            return bitmap
        }
        
        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
    
    // Kiểm tra email hợp lệ
    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    // Kiểm tra số điện thoại hợp lệ
    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.isNotBlank() && phone.length >= 9 && phone.all { it.isDigit() }
    }
} 