package com.example.learnjapanese.data.repository

import com.example.learnjapanese.data.dao.LearningProgressDao
import com.example.learnjapanese.data.model.LearningProgress
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningProgressRepository @Inject constructor(
    private val learningProgressDao: LearningProgressDao
) {
    suspend fun insertProgress(progress: LearningProgress) {
        learningProgressDao.insertProgress(progress)
    }
    
    suspend fun updateProgress(progress: LearningProgress) {
        learningProgressDao.updateProgress(progress)
    }
    
    fun getAllProgressByUserId(userId: String): Flow<List<LearningProgress>> {
        return learningProgressDao.getAllProgressByUserId(userId)
    }
    
    fun getProgressByCategory(userId: String, category: String): Flow<List<LearningProgress>> {
        return learningProgressDao.getProgressByCategory(userId, category)
    }
    
    fun getProgressBySubcategory(userId: String, category: String, subcategory: String): Flow<LearningProgress?> {
        return learningProgressDao.getProgressBySubcategory(userId, category, subcategory)
    }
    
    suspend fun updateCompletedItems(progressId: Long, completedItems: Int, lastStudyDate: Long = System.currentTimeMillis()) {
        learningProgressDao.updateCompletedItems(progressId, completedItems, lastStudyDate)
    }
    
    fun getCompletedItemsToday(userId: String, startOfDay: Long): Flow<Int?> {
        return learningProgressDao.getCompletedItemsToday(userId, startOfDay)
    }
} 