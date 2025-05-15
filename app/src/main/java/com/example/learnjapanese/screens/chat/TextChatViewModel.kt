package com.example.learnjapanese.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.AIChatRequest
import com.example.learnjapanese.data.model.ChatMessage
import com.example.learnjapanese.data.repository.AIChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextChatViewModel @Inject constructor(
    private val aiChatRepository: AIChatRepository
) : ViewModel() {
    
    // Danh sách tin nhắn
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    // Trạng thái loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Trạng thái lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // Khởi tạo tin nhắn cho cuộc trò chuyện cũ nếu có
    fun initializeChat(chatId: String?) {
        // Nếu chatId không null thì tải dữ liệu mẫu cho cuộc trò chuyện đó
        // Trong triển khai thực tế, có thể gọi repository để lấy dữ liệu
        _messages.value = if (chatId != null) {
            getSampleMessages(chatId)
        } else {
            emptyList() // Trường hợp cuộc trò chuyện mới
        }
    }
    
    // Gửi tin nhắn và nhận phản hồi từ AI
    fun sendMessage(content: String, userId: String? = null) {
        if (content.isBlank()) return
        
        // Thêm tin nhắn của người dùng
        val userMessage = ChatMessage(
            id = "u${_messages.value.size + 1}",
            content = content,
            isFromUser = true,
            timestamp = System.currentTimeMillis()
        )
        _messages.value = _messages.value + userMessage
        
        // Bắt đầu loading
        _isLoading.value = true
        _error.value = null
        
        // Gọi API
        viewModelScope.launch {
            try {
                val request = AIChatRequest(
                    question = content,
                    userId = userId
                )
                
                val result = aiChatRepository.sendMessageToAI(request)
                
                result.fold(
                    onSuccess = { response ->
                        // Thêm phản hồi từ AI
                        val aiMessage = ChatMessage(
                            id = "a${_messages.value.size + 1}",
                            content = response.data.answer,
                            isFromUser = false,
                            timestamp = System.currentTimeMillis()
                        )
                        _messages.value = _messages.value + aiMessage
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Đã xảy ra lỗi khi gửi tin nhắn"
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Đã xảy ra lỗi không xác định"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Xóa thông báo lỗi
    fun clearError() {
        _error.value = null
    }
    
    /**
     * Trả về dữ liệu mẫu cho cuộc trò chuyện đã có
     * Trong triển khai thực tế, dữ liệu này sẽ được lấy từ DB hoặc API
     */
    private fun getSampleMessages(chatId: String): List<ChatMessage> {
        return when (chatId) {
            "c1" -> listOf(
                ChatMessage(
                    id = "u1",
                    content = "Tôi muốn học về mẫu câu với て-form",
                    isFromUser = true,
                    timestamp = System.currentTimeMillis() - 3700000
                ),
                ChatMessage(
                    id = "a1",
                    content = "て-form là một hình thức quan trọng trong tiếng Nhật. Nó được sử dụng trong nhiều tình huống khác nhau như:\n\n1. Kết nối nhiều hành động\n2. Yêu cầu/Đề nghị\n3. Xin phép\n4. Diễn tả trạng thái\n\nBạn muốn tìm hiểu về cách nào?",
                    isFromUser = false,
                    timestamp = System.currentTimeMillis() - 3680000
                ),
                ChatMessage(
                    id = "u2",
                    content = "Tôi muốn học cách kết nối nhiều hành động",
                    isFromUser = true,
                    timestamp = System.currentTimeMillis() - 3600000
                ),
                ChatMessage(
                    id = "a2",
                    content = "Để kết nối nhiều hành động, chúng ta sử dụng động từ て-form rồi thêm động từ tiếp theo. Ví dụ:\n\n朝ごはんを食べて、学校に行きます。\nAsagohan o tabete, gakkou ni ikimasu.\n(Tôi ăn sáng, rồi đi học.)\n\nテレビを見て、寝ました。\nTerebi o mite, nemashita.\n(Tôi xem TV, rồi đi ngủ.)",
                    isFromUser = false,
                    timestamp = System.currentTimeMillis() - 3580000
                )
            )
            "c2" -> listOf(
                ChatMessage(
                    id = "u1",
                    content = "Tôi gặp khó khăn khi phát âm âm R và L trong tiếng Nhật",
                    isFromUser = true,
                    timestamp = System.currentTimeMillis() - 90000000
                ),
                ChatMessage(
                    id = "a1",
                    content = "Trong tiếng Nhật, âm \"R\" (ら、り、る、れ、ろ) được phát âm khác với cả âm R và L trong tiếng Anh. Đó là âm đánh lưỡi, gần giống với âm \"d\" trong từ \"ready\" của tiếng Anh.\n\nĐể luyện tập, hãy thử lặp lại: らりるれろ (ra-ri-ru-re-ro) nhiều lần.",
                    isFromUser = false,
                    timestamp = System.currentTimeMillis() - 89900000
                )
            )
            else -> emptyList()
        }
    }
} 