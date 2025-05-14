package com.example.learnjapanese.data.model

data class ReadingListResponse(
    val success: Boolean,
    val data: ReadingListData,
    val message: String
)

data class ReadingListData(
    val readings: List<ReadingResponse>,
    val pagination: PaginationData
)

data class ReadingResponse(
    val reading_id: String,
    val title: String,
    val short_intro: String,
    val level: String,
    val background_image_url: String?,
    val difficulty: Int,
    val estimated_time: Int,
    val created_at: String,
    val status: String
)

data class ReadingDetailResponse(
    val success: Boolean,
    val data: ReadingDetailData,
    val message: String
)

data class ReadingDetailData(
    val reading_id: String,
    val title: String,
    val level: String,
    val short_intro: String,
    val japanese_content: String,
    val vietnamese_content: String,
    val background_image_url: String?,
    val tags: String,
    val difficulty: Int,
    val estimated_time: Int,
    val vocabulary_focus: String,
    val grammar_focus: String,
    val created_at: String,
    val updated_at: String,
    val status: String,
    val sentences: List<SentenceData>
)

data class SentenceData(
    val text: String,
    val translation: String
)

data class PaginationData(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
) 