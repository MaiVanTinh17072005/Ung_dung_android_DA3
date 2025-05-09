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