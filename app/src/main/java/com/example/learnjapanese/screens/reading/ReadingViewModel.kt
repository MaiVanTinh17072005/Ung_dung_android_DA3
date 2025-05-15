package com.example.learnjapanese.screens.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.ReadingDetailData
import com.example.learnjapanese.data.model.ReadingResponse
import com.example.learnjapanese.data.repository.ReadingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val readingRepository: ReadingRepository
) : ViewModel() {
    // Danh sách bài đọc
    private val _readingList = MutableStateFlow<List<Reading>>(emptyList())
    val readingList: StateFlow<List<Reading>> = _readingList.asStateFlow()
    
    // Chi tiết bài đọc hiện tại
    private val _currentReading = MutableStateFlow<ReadingDetail?>(null)
    val currentReading: StateFlow<ReadingDetail?> = _currentReading.asStateFlow()
    
    // Trạng thái lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // Trạng thái loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        // Tải danh sách bài đọc khi khởi tạo
        fetchReadingList()
    }
    
    // Lấy danh sách bài đọc từ API
    fun fetchReadingList(level: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                readingRepository.getReadingList(level = level).fold(
                    onSuccess = { data ->
                        _readingList.value = data.readings.map { it.toReadingModel() }
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Đã xảy ra lỗi khi tải danh sách bài đọc"
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Đã xảy ra lỗi không xác định"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Tải chi tiết bài đọc từ API
    fun loadReadingDetail(readingId: String) {
        viewModelScope.launch {
            // Reset trạng thái hiện tại
            _currentReading.value = null
            _isLoading.value = true
            _error.value = null
            
            try {
                readingRepository.getReadingDetail(readingId).fold(
                    onSuccess = { data ->
                        _currentReading.value = data.toReadingDetailModel()
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Đã xảy ra lỗi khi tải chi tiết bài đọc"
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Đã xảy ra lỗi không xác định"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Chuyển đổi từ response model sang UI model để giữ nguyên giao diện
    private fun ReadingResponse.toReadingModel(): Reading {
        return Reading(
            id = reading_id,
            title = title,
            summary = short_intro,
            level = level,
            isBookmarked = false, // Trường này cần lấy từ trạng thái người dùng, tạm thời để false
            imageUrl = background_image_url
        )
    }
    
    // Chuyển đổi từ response model sang UI model chi tiết
    private fun ReadingDetailData.toReadingDetailModel(): ReadingDetail {
        return ReadingDetail(
            id = reading_id,
            title = title,
            summary = short_intro,
            level = level,
            wordCount = japanese_content.split("\\s+".toRegex()).size, // Ước tính số từ
            content = japanese_content,
            sentences = sentences.map { 
                Sentence(
                    id = "${reading_id}_${sentences.indexOf(it) + 1}",
                    text = it.text,
                    translation = it.translation
                )
            }
        )
    }
    
    // Xóa lỗi
    fun clearError() {
        _error.value = null
    }
}

// Model cho danh sách bài đọc
data class Reading(
    val id: String,
    val title: String,
    val summary: String,
    val level: String,
    val isBookmarked: Boolean = false,
    val imageUrl: String? = null
)

// Model cho chi tiết bài đọc
data class ReadingDetail(
    val id: String,
    val title: String,
    val summary: String,
    val level: String,
    val wordCount: Int,
    val content: String,
    val sentences: List<Sentence>
)

// Model cho từng câu trong bài đọc
data class Sentence(
    val id: String,
    val text: String,
    val translation: String
) 