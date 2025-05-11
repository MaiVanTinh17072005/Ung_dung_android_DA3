package com.example.learnjapanese.data.api

import com.example.learnjapanese.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Body
import retrofit2.http.Query

/**
 * Interface định nghĩa các API endpoints liên quan đến thông tin người dùng
 * Sử dụng Retrofit để thực hiện các HTTP requests
 */
interface UserApiService {
    /**
     * Lấy thông tin profile của người dùng theo email
     * 
     * Request:
     * - Method: GET
     * - Endpoint: /api/profile
     * - Headers: 
     *   + Authorization: Bearer {token}
     * - Query Parameters:
     *   + email: string (email của người dùng)
     * 
     * Response:
     * - Success (200):
     *   {
     *     "fullName": "string",
     *     "email": "string",
     *     "phone": "string",
     *     "bio": "string",
     *     "level": "string",
     *     "avatarUrl": "string | null"
     *   }
     * - Error:
     *   + 401: Unauthorized - Chưa đăng nhập
     *   + 404: Not Found - Không tìm thấy profile
     *   + 500: Server Error
     * 
     * Usage:
     * - Gọi khi cần hiển thị thông tin profile
     * - Gọi khi mở màn hình EditProfile
     */
    @GET("/api/profile")
    suspend fun getUserProfile(@Query("email") email: String): Response<UserProfile>

    /**
     * Cập nhật thông tin profile của người dùng
     * 
     * Request:
     * - Method: PUT
     * - Endpoint: /api/profile
     * - Headers:
     *   + Authorization: Bearer {token}
     *   + Content-Type: application/json
     * - Body:
     *   {
     *     "fullName": "string",
     *     "email": "string",
     *     "phone": "string",
     *     "bio": "string",
     *     "level": "string",
     *     "avatarUrl": "string | null"
     *   }
     * 
     * Response:
     * - Success (200):
     *   {
     *     "fullName": "string",
     *     "email": "string",
     *     "phone": "string",
     *     "bio": "string",
     *     "level": "string",
     *     "avatarUrl": "string | null"
     *   }
     * - Error:
     *   + 400: Bad Request - Dữ liệu không hợp lệ
     *   + 401: Unauthorized - Chưa đăng nhập
     *   + 404: Not Found - Không tìm thấy profile
     *   + 500: Server Error
     * 
     * Usage:
     * - Gọi khi người dùng cập nhật thông tin profile
     * - Gọi khi nhấn nút "Lưu thay đổi" trong EditProfile
     * 
     * Validation:
     * - fullName: không được trống
     * - email: phải đúng định dạng email
     * - phone: phải đúng định dạng số điện thoại
     * - level: phải là một trong các giá trị: N5, N4, N3, N2, N1
     */
    @PUT("/api/profile")
    suspend fun updateUserProfile(@Body profile: UserProfile): Response<UserProfile>
} 