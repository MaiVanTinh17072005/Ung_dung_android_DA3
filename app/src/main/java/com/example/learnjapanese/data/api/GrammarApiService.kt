package com.example.learnjapanese.data.api

import com.example.learnjapanese.data.model.GrammarBatchRequest
import com.example.learnjapanese.data.model.GrammarListResponse
import com.example.learnjapanese.data.model.GrammarResponse
import com.example.learnjapanese.data.model.QuizGenerationRequest
import com.example.learnjapanese.data.model.QuizResponseWrapper
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface định nghĩa các API liên quan đến ngữ pháp
 */
interface GrammarApiService {
    
    /**
     * Lấy danh sách tất cả ngữ pháp với phân trang và lọc
     * 
     * @param limit Số lượng mục trên một trang
     * @param page Số trang
     * @param level Cấp độ ngữ pháp (N5, N4, N3, N2, N1)
     * @param category Danh mục ngữ pháp
     * @return Danh sách ngữ pháp
     */
    @GET("/api/grammar")
    suspend fun getGrammarList(
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("level") level: String? = null,
        @Query("category") category: String? = null
    ): Response<GrammarListResponse>
    
    /**
     * Lấy thông tin chi tiết của một ngữ pháp theo ID
     * 
     * @param id ID của ngữ pháp
     * @return Chi tiết ngữ pháp
     */
    @GET("/api/grammar/{id}")
    suspend fun getGrammarDetail(
        @Path("id") id: String
    ): Response<GrammarResponse>
    
    /**
     * Lấy thông tin của nhiều ngữ pháp theo danh sách ID
     * 
     * @param request Request body chứa danh sách ID
     * @return Danh sách chi tiết ngữ pháp
     */
    @POST("/api/grammar/batch")
    suspend fun getGrammarBatch(
        @Body request: GrammarBatchRequest
    ): Response<GrammarListResponse>
    
    /**
     * Tìm kiếm ngữ pháp theo từ khóa
     * 
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách ngữ pháp phù hợp
     */
    @GET("/api/grammar/search")
    suspend fun searchGrammar(
        @Query("keyword") keyword: String
    ): Response<GrammarListResponse>
    
    /**
     * Tạo câu hỏi ngữ pháp bằng AI dựa trên danh sách ID ngữ pháp
     *
     * @param request Body chứa các thông số để tạo câu hỏi
     * @return Danh sách câu hỏi được tạo
     */
    @POST("/api/grammar/generate-quiz")
    suspend fun generateQuiz(
        @Body request: QuizGenerationRequest
    ): Response<QuizResponseWrapper>
} 