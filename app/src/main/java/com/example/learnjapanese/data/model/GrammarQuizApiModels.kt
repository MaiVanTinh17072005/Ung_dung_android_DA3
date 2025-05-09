package com.example.learnjapanese.data.model

import com.google.gson.annotations.SerializedName

/**
 * Request body cho API tạo câu hỏi ngữ pháp bằng AI
 */
data class QuizGenerationRequest(
    @SerializedName("grammar_ids") val grammarIds: List<String>,
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("level") val level: String? = null,
    @SerializedName("question_count") val questionCount: Int = 10,
    @SerializedName("question_types") val questionTypes: List<String> = listOf("MEANING_TO_PATTERN", "PATTERN_TO_MEANING"),
    @SerializedName("difficulty") val difficulty: String = "MEDIUM"
)

/**
 * Response wrapper cho API tạo câu hỏi
 */
data class QuizResponseWrapper(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: QuizResponse,
    @SerializedName("message") val message: String
)

/**
 * Model dữ liệu quiz từ API
 */
data class QuizResponse(
    @SerializedName("quiz_id") val quizId: String,
    @SerializedName("grammar_ids") val grammarIds: List<String>,
    @SerializedName("questions") val questions: List<QuizQuestionResponse>
)

/**
 * Model câu hỏi quiz từ API
 */
data class QuizQuestionResponse(
    @SerializedName("question_id") val questionId: String,
    @SerializedName("question_type") val questionType: String,
    @SerializedName("grammar_id") val grammarId: String,
    @SerializedName("grammar_pattern") val grammarPattern: String,
    @SerializedName("question_text") val questionText: String,
    @SerializedName("options") val options: List<String>,
    @SerializedName("correct_answer_index") val correctAnswerIndex: Int,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("example") val example: QuizExampleResponse? = null,
    @SerializedName("difficulty") val difficulty: String
)

/**
 * Model ví dụ trong câu hỏi quiz
 */
data class QuizExampleResponse(
    @SerializedName("japanese") val japanese: String,
    @SerializedName("reading") val reading: String? = null,
    @SerializedName("meaning") val meaning: String
) 