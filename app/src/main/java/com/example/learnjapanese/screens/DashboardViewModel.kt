package com.example.learnjapanese.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.FeaturedBanner
import com.example.learnjapanese.data.model.LearningFeature
import com.example.learnjapanese.data.model.ReadingResponse
import com.example.learnjapanese.data.repository.ReadingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val readingRepository: ReadingRepository
) : ViewModel() {
    
    init {
        Log.d("DashboardViewModel", "DashboardViewModel initialized")
    }
    
    // State cho các bài đọc mới nhất
    private val _latestReadings = MutableStateFlow<List<ReadingResponse>>(emptyList())
    val latestReadings: StateFlow<List<ReadingResponse>> = _latestReadings.asStateFlow()
    
    // State cho tính năng nổi bật
    private val _featuredBanner = MutableStateFlow<FeaturedBanner?>(null)
    val featuredBanner: StateFlow<FeaturedBanner?> = _featuredBanner.asStateFlow()
    
    // State cho danh sách tính năng học tập
    private val _learningFeatures = MutableStateFlow<List<LearningFeature>>(emptyList())
    val learningFeatures: StateFlow<List<LearningFeature>> = _learningFeatures.asStateFlow()
    
    init {
        Log.d("DashboardViewModel", "Starting to fetch data")
        // Lấy dữ liệu từ Repository
        fetchLatestReadings()
        fetchFeaturedBanner()
        fetchLearningFeatures()
    }
    
    private fun fetchLatestReadings() {
        viewModelScope.launch {
            Log.d("DashboardViewModel", "Fetching latest readings")
            try {
                // Lấy 2 bài đọc mới nhất
                val result = readingRepository.getReadingList(limit = 2, page = 1)
                result.onSuccess { readingListData ->
                    _latestReadings.value = readingListData.readings
                    Log.d("DashboardViewModel", "Latest readings fetched successfully: ${readingListData.readings.size} items")
                }.onFailure { error ->
                    Log.e("DashboardViewModel", "Error fetching latest readings", error)
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Exception when fetching latest readings", e)
            }
        }
    }
    
    private fun fetchFeaturedBanner() {
        viewModelScope.launch {
            Log.d("DashboardViewModel", "Fetching featured banner")
            // Trong thực tế, dữ liệu này sẽ được lấy từ repository
            _featuredBanner.value = FeaturedBanner(
                title = "Tính năng mới: Trò chuyện với AI",
                description = "Luyện tập hội thoại với trợ lý AI thông minh",
                imageUrl = null
            )
            Log.d("DashboardViewModel", "Featured banner fetched successfully")
        }
    }
    
    private fun fetchLearningFeatures() {
        viewModelScope.launch {
            Log.d("DashboardViewModel", "Fetching learning features")
            // Trong thực tế, dữ liệu này sẽ được lấy từ repository
            // Chẳng hạn: learningProgressRepository.getLearningFeatures()
            _learningFeatures.value = listOf(
                LearningFeature("Từ vựng N5", 0.3f),
                LearningFeature("Ngữ pháp", 0.5f),
                LearningFeature("Chữ Kanji", 0.2f),
                LearningFeature("Bài tập", 0.6f)
            )
            Log.d("DashboardViewModel", "Learning features fetched successfully")
        }
    }
}

// Data classes cho các state
data class LearningStats(
    val wordsLearntToday: Int = 0,
    val currentStreak: Int = 0,
    val accuracy: Int = 0
) 