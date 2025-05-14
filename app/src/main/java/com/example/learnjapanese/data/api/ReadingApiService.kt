package com.example.learnjapanese.data.api

import com.example.learnjapanese.data.model.ReadingDetailResponse
import com.example.learnjapanese.data.model.ReadingListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface định nghĩa các API liên quan đến bài đọc
 */
interface ReadingApiService {
    
    /**
     * Lấy danh sách bài đọc với các tùy chọn lọc
     * 
     * @param limit Số lượng bài đọc trên mỗi trang
     * @param page Số trang
     * @param level Cấp độ bài đọc (N5, N4, N3, N2, N1)
     * @return Danh sách bài đọc và thông tin phân trang
     */
    @GET("/api/reading")
    suspend fun getReadingList(
        @Query("limit") limit: Int? = 10,
        @Query("page") page: Int? = 1,
        @Query("level") level: String? = null
    ): Response<ReadingListResponse>
    
    /**
     * Lấy chi tiết của một bài đọc theo ID
     * 
     * @param id ID của bài đọc
     * @return Chi tiết bài đọc
     */
    @GET("/api/reading/{id}")
    suspend fun getReadingDetail(
        @Path("id") id: String
    ): Response<ReadingDetailResponse>
} 