package com.example.learnjapanese.data.repository

import com.example.learnjapanese.data.api.AIChatApiService
import com.example.learnjapanese.data.model.AIChatRequest
import com.example.learnjapanese.data.model.AIChatResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository xử lý các thao tác liên quan đến trò chuyện với AI
 */
@Singleton
class AIChatRepository @Inject constructor(
    private val aiChatApiService: AIChatApiService
) {
    /**
     * Gửi tin nhắn tới AI và nhận phản hồi
     * @param request Thông tin tin nhắn và người dùng
     * @return Kết quả bao gồm trạng thái thành công/thất bại và dữ liệu
     */
    suspend fun sendMessageToAI(request: AIChatRequest): Result<AIChatResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = aiChatApiService.sendMessageToAI(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(IOException("Phản hồi trống từ API"))
                } else {
                    Result.failure(IOException("Không thể gửi tin nhắn tới AI: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
} 