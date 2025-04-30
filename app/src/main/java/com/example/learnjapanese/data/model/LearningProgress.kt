package com.example.learnjapanese.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "learning_progress",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LearningProgress(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val category: String, // Từ vựng, Ngữ pháp, Kanji, v.v.
    val subcategory: String, // N5, N4, v.v. hoặc các loại ngữ pháp
    val totalItems: Int,
    val completedItems: Int,
    val lastStudyDate: Long = System.currentTimeMillis()
) {
    fun getProgressPercentage(): Float {
        return if (totalItems > 0) {
            completedItems.toFloat() / totalItems.toFloat()
        } else {
            0f
        }
    }
} 