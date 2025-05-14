package com.example.learnjapanese.data.repository

import com.example.learnjapanese.data.api.ReadingApiService
import com.example.learnjapanese.data.model.ReadingDetailData
import com.example.learnjapanese.data.model.ReadingListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository xử lý các thao tác liên quan đến bài đọc
 */
@Singleton
class ReadingRepository @Inject constructor(
    private val readingApiService: ReadingApiService
) {
    /**
     * Lấy danh sách bài đọc
     * @param limit Số lượng bài đọc trên mỗi trang
     * @param page Số trang
     * @param level Cấp độ bài đọc (N5, N4, N3, N2, N1)
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun getReadingList(
        limit: Int = 10,
        page: Int = 1,
        level: String? = null
    ): Result<ReadingListData> {
        return withContext(Dispatchers.IO) {
            try {
                val response = readingApiService.getReadingList(limit, page, level)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it.data)
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể lấy danh sách bài đọc: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Lấy chi tiết bài đọc
     * @param id ID của bài đọc
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun getReadingDetail(id: String): Result<ReadingDetailData> {
        return withContext(Dispatchers.IO) {
            try {
                val response = readingApiService.getReadingDetail(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it.data)
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể lấy chi tiết bài đọc: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
} 