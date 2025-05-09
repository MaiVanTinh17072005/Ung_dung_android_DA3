package com.example.learnjapanese.utils

import com.example.learnjapanese.data.model.VocabularyTopic
import com.example.learnjapanese.data.model.VocabularyTopicResponse
import com.example.learnjapanese.data.model.VocabularyWord
import com.example.learnjapanese.data.model.VocabularyWordResponse

/**
 * Chuyển đổi từ danh sách chủ đề từ API sang dạng dùng cho UI
 */
fun List<VocabularyTopicResponse>.toUiTopicList(): List<VocabularyTopic> {
    val result = mutableListOf<VocabularyTopic>()
    
    // Chuyển đổi các chủ đề root
    forEach { topicResponse ->
        val topic = topicResponse.toUiTopic()
        result.add(topic)
        
        // Thêm các chủ đề con (nếu có)
        topicResponse.children?.forEach { childResponse ->
            val childTopic = childResponse.toUiTopic()
            result.add(childTopic)
        }
    }
    
    return result
}

/**
 * Chuyển đổi từ chủ đề từ API sang dạng dùng cho UI
 */
fun VocabularyTopicResponse.toUiTopic(): VocabularyTopic {
    return VocabularyTopic(
        id = this.topicId,
        name = this.name,
        category = this.level,
        totalWords = 0, // Sẽ được cập nhật khi lấy danh sách từ vựng
        progress = 0f, // Sẽ được cập nhật từ thông tin tiến độ
        isFavorite = false, // Mặc định chưa yêu thích
        words = emptyList() // Sẽ được cập nhật khi lấy danh sách từ vựng
    )
}

/**
 * Chuyển đổi từ danh sách từ vựng từ API sang dạng dùng cho UI
 */
fun List<VocabularyWordResponse>.toUiWordList(): List<VocabularyWord> {
    return map { it.toUiWord() }
}

/**
 * Chuyển đổi từ từ vựng từ API sang dạng dùng cho UI
 */
fun VocabularyWordResponse.toUiWord(): VocabularyWord {
    return VocabularyWord(
        id = this.id,
        word = this.word,
        reading = this.reading,
        meaning = this.meaning,
        exampleSentence = this.exampleSentence,
        exampleSentenceTranslation = this.exampleMeaning,
        isLearned = false // Mặc định chưa học
    )
} 