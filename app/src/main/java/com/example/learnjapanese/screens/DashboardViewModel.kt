package com.example.learnjapanese.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.FeaturedBanner
import com.example.learnjapanese.data.model.LearningFeature
import com.example.learnjapanese.data.model.ReadingResponse
import com.example.learnjapanese.data.model.VocabularySearchItem
import com.example.learnjapanese.data.repository.ReadingRepository
import com.example.learnjapanese.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val readingRepository: ReadingRepository,
    private val vocabularyRepository: VocabularyRepository
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
    
    // State cho tìm kiếm từ vựng
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<VocabularySearchItem>>(emptyList())
    val searchResults: StateFlow<List<VocabularySearchItem>> = _searchResults.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    private var searchJob: Job? = null
    
    init {
        Log.d("DashboardViewModel", "Starting to fetch data")
        // Lấy dữ liệu từ Repository
        fetchLatestReadings()
        fetchFeaturedBanner()
        fetchLearningFeatures()
    }
    
    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            _searchQuery.value = ""
            _searchResults.value = emptyList()
        }
    }
    
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        
        // Hủy bỏ job tìm kiếm cũ nếu có
        searchJob?.cancel()
        
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _isSearching.value = false
            return
        }
        
        // Tạo job tìm kiếm mới với debounce 500ms
        searchJob = viewModelScope.launch {
            _isSearching.value = true
            delay(500) // Đợi 500ms để tránh gọi API quá nhiều
            searchVocabulary(query)
        }
    }
    
    private fun searchVocabulary(keyword: String) {
        viewModelScope.launch {
            try {
                Log.d("DashboardViewModel", "Searching vocabulary with keyword: $keyword")
                val result = vocabularyRepository.searchVocabulary(keyword)
                result.onSuccess { items ->
                    _searchResults.value = items
                    Log.d("DashboardViewModel", "Search results: ${items.size} items")
                }.onFailure { error ->
                    Log.e("DashboardViewModel", "Error searching vocabulary", error)
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Exception when searching vocabulary", e)
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
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