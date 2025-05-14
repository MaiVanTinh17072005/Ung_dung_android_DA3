package com.example.learnjapanese.data.model

/**
 * Model cho request API trò chuyện với AI
 */
data class AIChatRequest(
    val question: String,
    val userId: String? = null
)

/**
 * Model cho response API trò chuyện với AI
 */
data class AIChatResponse(
    val success: Boolean,
    val data: AIChatData,
    val message: String
)

/**
 * Model cho dữ liệu phản hồi từ AI
 */
data class AIChatData(
    val question: String,
    val answer: String,
    val caseType: String? = null
) 