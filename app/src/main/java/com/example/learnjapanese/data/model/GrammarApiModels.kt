package com.example.learnjapanese.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model phản hồi chi tiết ngữ pháp từ API
 */
data class GrammarResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: GrammarDetailResponse,
    @SerializedName("message") val message: String
)

/**
 * Model chi tiết thông tin ngữ pháp
 */
data class GrammarDetailResponse(
    @SerializedName("grammar_id") val grammarId: String,
    @SerializedName("pattern") val pattern: String,
    @SerializedName("meaning") val meaning: String,
    @SerializedName("usage_note") val usageNote: String,
    @SerializedName("example_sentences") val exampleSentences: List<String>,
    @SerializedName("example_meanings") val exampleMeanings: List<String>,
    @SerializedName("level") val level: String,
    @SerializedName("category") val category: String,
    @SerializedName("video_url") val videoUrl: String? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("status") val status: String
)

/**
 * Model request body cho API lấy nhiều ngữ pháp theo ID
 */
data class GrammarBatchRequest(
    @SerializedName("ids") val ids: List<String>
)

/**
 * Model phản hồi danh sách ngữ pháp từ API
 */
data class GrammarListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<GrammarDetailResponse>,
    @SerializedName("message") val message: String
) 