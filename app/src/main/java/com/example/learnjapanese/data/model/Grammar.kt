package com.example.learnjapanese.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grammar")
data class Grammar(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pattern: String, // Mẫu ngữ pháp
    val formation: String, // Cách thành lập
    val meaning: String, // Nghĩa tiếng Việt
    val exampleSentences: List<String>, // Danh sách câu ví dụ
    val exampleSentenceTranslations: List<String>, // Danh sách dịch nghĩa của câu ví dụ
    val level: String, // Cấp độ: N5, N4, N3, N2, N1
    val usageNotes: String? = null, // Ghi chú sử dụng
    val similarPatterns: List<String>? = null // Các mẫu tương tự
) 