package com.example.learnjapanese.data.model

/**
 * Model cho tin nhắn trong cuộc trò chuyện
 * @param id ID của tin nhắn
 * @param content Nội dung tin nhắn
 * @param isFromUser true nếu tin nhắn được gửi bởi người dùng, false nếu từ AI hoặc hệ thống
 * @param timestamp Thời điểm tin nhắn được gửi (milliseconds)
 */
data class ChatMessage(
    val id: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long
)

/**
 * Model cho cuộc trò chuyện
 * @param id ID của cuộc trò chuyện
 * @param title Tiêu đề cuộc trò chuyện
 * @param lastMessage Tin nhắn cuối cùng
 * @param timestamp Thời điểm tin nhắn cuối cùng
 */
data class ChatConversation(
    val id: String,
    val title: String,
    val lastMessage: String?,
    val timestamp: Long
) 