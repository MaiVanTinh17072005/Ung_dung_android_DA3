package com.example.learnjapanese.utils

import com.example.learnjapanese.data.model.GrammarDetailResponse
import com.example.learnjapanese.data.model.GrammarExample
import com.example.learnjapanese.data.model.GrammarItem

/**
 * Chuyển đổi từ GrammarDetailResponse (model API) sang GrammarItem (model UI)
 */
fun GrammarDetailResponse.toUiModel(): GrammarItem {
    // Tạo danh sách ví dụ từ hai danh sách riêng biệt, xử lý trường hợp danh sách null hoặc rỗng
    val examples = if (exampleSentences.isNullOrEmpty() || exampleMeanings.isNullOrEmpty()) {
        // Trả về danh sách trống nếu một trong hai danh sách là null hoặc rỗng
        emptyList()
    } else {
        // Xác định kích thước tối thiểu để tránh IndexOutOfBoundsException
        val minSize = minOf(exampleSentences.size, exampleMeanings.size)
        val safeSentences = exampleSentences.take(minSize)
        val safeMeanings = exampleMeanings.take(minSize)
        
        // Ghép cặp các phần tử từ hai danh sách
        safeSentences.zip(safeMeanings)
            .mapIndexed { index, (sentence, meaning) ->
                GrammarExample(
                    japanese = sentence,
                    meaning = meaning
                )
            }
    }
    
    return GrammarItem(
        id = this.grammarId,
        title = this.pattern,
        level = this.level,
        summary = this.meaning,
        explanation = this.usageNote,
        examples = examples,
        videoUrl = this.videoUrl,
        // Các trường thêm trước đây không có trong API
        isLearned = false // Mặc định chưa học
    )
}

/**
 * Chuyển đổi danh sách GrammarDetailResponse thành danh sách GrammarItem
 */
fun List<GrammarDetailResponse>.toUiModelList(): List<GrammarItem> {
    return this.map { it.toUiModel() }
} 