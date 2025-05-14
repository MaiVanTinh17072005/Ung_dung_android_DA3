package com.example.learnjapanese.data.repository

import com.example.learnjapanese.data.api.VocabularyApiService
import com.example.learnjapanese.data.model.CompleteTopicRequest
import com.example.learnjapanese.data.model.CompleteTopicResponse
import com.example.learnjapanese.data.model.VocabularyCountResponse
import com.example.learnjapanese.data.model.VocabularyTopicResponse
import com.example.learnjapanese.data.model.VocabularyWordResponse
import com.example.learnjapanese.data.model.VocabularySearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository xử lý các thao tác liên quan đến từ vựng
 */
@Singleton
class VocabularyRepository @Inject constructor(
    private val vocabularyApiService: VocabularyApiService
) {
    /**
     * Lấy danh sách tất cả các chủ đề từ vựng
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun getVocabularyTopics(): Result<List<VocabularyTopicResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = vocabularyApiService.getVocabularyTopics()
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(IOException("Không thể tải danh sách chủ đề từ vựng: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Lấy danh sách từ vựng của một chủ đề cụ thể
     * @param topicId ID của chủ đề
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun getVocabularyWords(topicId: String): Result<List<VocabularyWordResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = vocabularyApiService.getVocabularyWords(topicId)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(IOException("Không thể tải danh sách từ vựng: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Lấy số lượng từ vựng theo từng chủ đề
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun getVocabularyCountByTopics(): Result<List<VocabularyCountResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = vocabularyApiService.getVocabularyCountByTopics()
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            Result.success(it.data)
                        } else {
                            Result.failure(IOException(it.message))
                        }
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể lấy số lượng từ vựng theo chủ đề: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Đánh dấu hoàn thành một chủ đề từ vựng
     * @param topicId ID của chủ đề
     * @param userId ID của người dùng
     * @param score Điểm số đạt được (optional)
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun completeVocabularyTopic(topicId: String, userId: String, score: Int? = null): Result<CompleteTopicResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = CompleteTopicRequest(userId = userId, score = score)
                val response = vocabularyApiService.completeVocabularyTopic(topicId, request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể đánh dấu hoàn thành chủ đề: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Tìm kiếm từ vựng theo từ khóa
     * @param keyword Từ khóa tìm kiếm (tiếng Nhật, cách đọc hoặc nghĩa tiếng Việt)
     * @param fuzzy Tìm kiếm gần đúng (true/false, mặc định là true)
     * @return Kết quả tìm kiếm
     */
    suspend fun searchVocabulary(keyword: String, fuzzy: Boolean = true): Result<List<VocabularySearchItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = vocabularyApiService.searchVocabulary(keyword, fuzzy)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.success) {
                            Result.success(it.data)
                        } else {
                            Result.failure(IOException(it.message))
                        }
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể tìm kiếm từ vựng: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
} 