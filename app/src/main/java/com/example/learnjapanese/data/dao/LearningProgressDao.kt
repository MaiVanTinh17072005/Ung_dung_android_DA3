package com.example.learnjapanese.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.learnjapanese.data.model.LearningProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: LearningProgress)
    
    @Update
    suspend fun updateProgress(progress: LearningProgress)
    
    @Query("SELECT * FROM learning_progress WHERE userId = :userId")
    fun getAllProgressByUserId(userId: String): Flow<List<LearningProgress>>
    
    @Query("SELECT * FROM learning_progress WHERE userId = :userId AND category = :category")
    fun getProgressByCategory(userId: String, category: String): Flow<List<LearningProgress>>
    
    @Query("SELECT * FROM learning_progress WHERE userId = :userId AND category = :category AND subcategory = :subcategory")
    fun getProgressBySubcategory(userId: String, category: String, subcategory: String): Flow<LearningProgress?>
    
    @Query("UPDATE learning_progress SET completedItems = :completedItems, lastStudyDate = :lastStudyDate WHERE id = :progressId")
    suspend fun updateCompletedItems(progressId: Long, completedItems: Int, lastStudyDate: Long = System.currentTimeMillis())
    
    @Query("SELECT SUM(completedItems) FROM learning_progress WHERE userId = :userId AND lastStudyDate >= :startOfDay")
    fun getCompletedItemsToday(userId: String, startOfDay: Long): Flow<Int?>
} 