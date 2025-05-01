package com.example.learnjapanese.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Tạm thời xóa annotation HiltViewModel để có thể khởi tạo trực tiếp
class DashboardViewModel : ViewModel() {
    
    // State cho dữ liệu thống kê học tập
    private val _learningStats = MutableStateFlow(LearningStats())
    val learningStats: StateFlow<LearningStats> = _learningStats.asStateFlow()
    
    // State cho tính năng nổi bật
    private val _featuredBanner = MutableStateFlow<FeaturedBanner?>(null)
    val featuredBanner: StateFlow<FeaturedBanner?> = _featuredBanner.asStateFlow()
    
    // State cho danh sách tính năng học tập
    private val _learningFeatures = MutableStateFlow<List<LearningFeature>>(emptyList())
    val learningFeatures: StateFlow<List<LearningFeature>> = _learningFeatures.asStateFlow()
    
    init {
        // Giả lập lấy dữ liệu từ Repository
        fetchLearningStats()
        fetchFeaturedBanner()
        fetchLearningFeatures()
    }
    
    private fun fetchLearningStats() {
        viewModelScope.launch {
            // Trong thực tế, dữ liệu này sẽ được lấy từ repository
            // Chẳng hạn: userRepository.getCurrentUserStats()
            _learningStats.value = LearningStats(
                wordsLearntToday = 30,
                currentStreak = 5,
                accuracy = 80
            )
        }
    }
    
    private fun fetchFeaturedBanner() {
        viewModelScope.launch {
            // Trong thực tế, dữ liệu này sẽ được lấy từ repository
            _featuredBanner.value = FeaturedBanner(
                title = "Tính năng mới: Trò chuyện với AI",
                description = "Luyện tập hội thoại với trợ lý AI thông minh",
                imageUrl = null
            )
        }
    }
    
    private fun fetchLearningFeatures() {
        viewModelScope.launch {
            // Trong thực tế, dữ liệu này sẽ được lấy từ repository
            // Chẳng hạn: learningProgressRepository.getLearningFeatures()
            _learningFeatures.value = listOf(
                LearningFeature("Từ vựng N5", 0.3f),
                LearningFeature("Ngữ pháp", 0.5f),
                LearningFeature("Chữ Kanji", 0.2f),
                LearningFeature("Bài tập", 0.6f)
            )
        }
    }
}

// Data classes cho các state
data class LearningStats(
    val wordsLearntToday: Int = 0,
    val currentStreak: Int = 0,
    val accuracy: Int = 0
)

data class FeaturedBanner(
    val title: String,
    val description: String,
    val imageUrl: String?
)

data class LearningFeature(
    val title: String,
    val progress: Float
) 