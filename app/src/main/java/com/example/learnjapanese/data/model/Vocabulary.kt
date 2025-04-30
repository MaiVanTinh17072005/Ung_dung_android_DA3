package com.example.learnjapanese.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary")
data class Vocabulary(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String, // Từ tiếng Nhật
    val reading: String, // Cách đọc trong hiragana
    val meaning: String, // Nghĩa tiếng Việt
    val exampleSentence: String? = null, // Câu ví dụ
    val exampleSentenceTranslation: String? = null, // Dịch nghĩa câu ví dụ
    val level: String, // Cấp độ: N5, N4, N3, N2, N1
    val category: String, // Danh mục: động từ, tính từ, danh từ, v.v.
    val imageUrl: String? = null
) 