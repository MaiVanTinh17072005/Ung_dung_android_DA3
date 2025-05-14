package com.example.learnjapanese.data.api

import com.example.learnjapanese.data.model.AIChatRequest
import com.example.learnjapanese.data.model.AIChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interface định nghĩa các API liên quan đến trò chuyện với AI
 */
interface AIChatApiService {
    
    /**
     * Gửi câu hỏi tới AI và nhận phản hồi
     * @param request Thông tin câu hỏi và ID người dùng (không bắt buộc)
     * @return Phản hồi từ AI
     */
    @POST("/api/ai-chat")
    suspend fun sendMessageToAI(@Body request: AIChatRequest): Response<AIChatResponse>
} 