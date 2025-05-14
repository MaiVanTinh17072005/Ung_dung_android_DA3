package com.example.learnjapanese.data.api

import com.example.learnjapanese.data.model.CompleteTopicRequest
import com.example.learnjapanese.data.model.CompleteTopicResponse
import com.example.learnjapanese.data.model.VocabularyCountListResponse
import com.example.learnjapanese.data.model.VocabularyTopicResponse
import com.example.learnjapanese.data.model.VocabularyWordResponse
import com.example.learnjapanese.data.model.VocabularySearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface định nghĩa các API liên quan đến từ vựng
 */
interface VocabularyApiService {
    
    /**
     * Lấy danh sách tất cả các chủ đề từ vựng
     * @return Danh sách chủ đề dạng cây (có parent-child relationship)
     */
    @GET("/api/vocabulary-topics")
    suspend fun getVocabularyTopics(): Response<List<VocabularyTopicResponse>>
    
    /**
     * Lấy danh sách từ vựng của một chủ đề cụ thể
     * @param topicId ID của chủ đề
     * @return Danh sách từ vựng thuộc chủ đề
     */
    @GET("/api/vocabulary-topics/{topicId}/vocabulary")
    suspend fun getVocabularyWords(@Path("topicId") topicId: String): Response<List<VocabularyWordResponse>>
    
    /**
     * Lấy số lượng từ vựng theo từng chủ đề
     * @return Danh sách chủ đề và số lượng từ vựng
     */
    @GET("/api/vocabulary/count-by-topics")
    suspend fun getVocabularyCountByTopics(): Response<VocabularyCountListResponse>
    
    /**
     * Đánh dấu hoàn thành một chủ đề từ vựng
     * @param topicId ID của chủ đề
     * @param completeTopicRequest Request body chứa thông tin người dùng và điểm số
     * @return Thông tin về trạng thái hoàn thành
     */
    @POST("/api/vocabulary-topics/{topicId}/complete")
    suspend fun completeVocabularyTopic(
        @Path("topicId") topicId: String,
        @Body completeTopicRequest: CompleteTopicRequest
    ): Response<CompleteTopicResponse>
    
    /**
     * Tìm kiếm từ vựng theo từ khóa
     * @param keyword Từ khóa tìm kiếm (tiếng Nhật, cách đọc hoặc nghĩa tiếng Việt)
     * @param fuzzy Tìm kiếm gần đúng (true/false, mặc định là false)
     * @return Danh sách từ vựng tìm thấy
     */
    @GET("/api/vocabulary/search")
    suspend fun searchVocabulary(
        @Query("keyword") keyword: String,
        @Query("fuzzy") fuzzy: Boolean = true
    ): Response<VocabularySearchResponse>
} 