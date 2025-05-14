package com.example.learnjapanese.data.model

import com.google.gson.annotations.SerializedName

/**
 * Đối tượng chủ đề từ vựng nhận từ API
 */
data class VocabularyTopicResponse(
    @SerializedName("topic_id") val topicId: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("level") val level: String,
    @SerializedName("children") val children: List<VocabularyTopicResponse>? = null
)

/**
 * Đối tượng từ vựng nhận từ API
 */
data class VocabularyWordResponse(
    @SerializedName("vocab_id") val id: String,
    @SerializedName("word") val word: String,
    @SerializedName("reading") val reading: String,
    @SerializedName("meaning") val meaning: String,
    @SerializedName("example_sentence") val exampleSentence: String? = null,
    @SerializedName("example_meaning") val exampleMeaning: String? = null
)

/**
 * Đối tượng đếm từ vựng theo chủ đề nhận từ API
 */
data class VocabularyCountResponse(
    @SerializedName("topic_id") val topicId: String,
    @SerializedName("topic_name") val topicName: String,
    @SerializedName("topic_description") val topicDescription: String,
    @SerializedName("level") val level: String,
    @SerializedName("vocabulary_count") val vocabularyCount: Int
)

/**
 * Wrapper cho phản hồi API đếm từ vựng theo chủ đề
 */
data class VocabularyCountListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<VocabularyCountResponse>,
    @SerializedName("message") val message: String
)

/**
 * Request body cho API đánh dấu hoàn thành chủ đề
 */
data class CompleteTopicRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("score") val score: Int? = null
)

/**
 * Response khi đánh dấu hoàn thành
 */
data class CompleteTopicResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null
)

/**
 * Model cho dữ liệu trả về từ API tìm kiếm từ vựng
 */
data class VocabularySearchResponse(
    val success: Boolean,
    val data: List<VocabularySearchItem>,
    val message: String
)

/**
 * Mỗi mục từ vựng trong kết quả tìm kiếm
 */
data class VocabularySearchItem(
    val vocab_id: String,
    val word: String,
    val reading: String,
    val meaning: String,
    val example_sentence: String? = null,
    val example_meaning: String? = null
) 