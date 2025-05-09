package com.example.learnjapanese.screens.vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.VocabularyTopic
import com.example.learnjapanese.data.repository.VocabularyRepository
import com.example.learnjapanese.utils.Resource
import com.example.learnjapanese.utils.removeAccent
import com.example.learnjapanese.utils.toUiTopicList
import com.example.learnjapanese.utils.updateWordCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho màn hình danh sách chủ đề từ vựng
 */
@HiltViewModel
class VocabularyViewModel @Inject constructor(
    private val vocabularyRepository: VocabularyRepository
) : ViewModel() {

    // State của danh sách chủ đề
    private val _topics = MutableStateFlow<Resource<List<VocabularyTopic>>>(Resource.Loading())
    val topics: StateFlow<Resource<List<VocabularyTopic>>> = _topics.asStateFlow()
    
    // State của chủ đề yêu thích
    private val _favoriteTopics = MutableStateFlow<List<VocabularyTopic>>(emptyList())
    val favoriteTopics: StateFlow<List<VocabularyTopic>> = _favoriteTopics.asStateFlow()
    
    // Lưu trữ danh sách gốc để phục vụ tìm kiếm
    private var originalTopics: List<VocabularyTopic> = emptyList()
    
    init {
        loadTopics()
    }
    
    /**
     * Tải danh sách chủ đề từ vựng từ API
     */
    fun loadTopics() {
        viewModelScope.launch {
            _topics.value = Resource.Loading()
            
            try {
                // Lấy danh sách chủ đề
                val topicsResult = vocabularyRepository.getVocabularyTopics()
                
                topicsResult.fold(
                    onSuccess = { topicResponses ->
                        var uiTopics = topicResponses.toUiTopicList()
                        
                        // Lấy số lượng từ vựng theo chủ đề
                        val countResult = vocabularyRepository.getVocabularyCountByTopics()
                        
                        countResult.fold(
                            onSuccess = { countResponses ->
                                // Cập nhật số lượng từ vựng cho các chủ đề
                                uiTopics = uiTopics.updateWordCount(countResponses)
                                originalTopics = uiTopics
                                _topics.value = Resource.Success(uiTopics)
                                
                                // Cập nhật danh sách chủ đề yêu thích
                                _favoriteTopics.value = uiTopics.filter { it.isFavorite }.take(2)
                            },
                            onFailure = { exception ->
                                // Nếu không lấy được số lượng từ vựng, vẫn hiển thị danh sách chủ đề
                                originalTopics = uiTopics
                                _topics.value = Resource.Success(uiTopics)
                                _favoriteTopics.value = uiTopics.take(2).map { it.copy(isFavorite = true) }
                            }
                        )
                    },
                    onFailure = { exception ->
                        _topics.value = Resource.Error(exception.message ?: "Không thể tải danh sách chủ đề từ vựng")
                    }
                )
            } catch (e: Exception) {
                _topics.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
    }
    
    /**
     * Tìm kiếm chủ đề từ vựng theo từ khóa
     * Hỗ trợ tìm kiếm không phân biệt chữ hoa/thường và dấu
     */
    fun searchTopics(query: String) {
        if (query.isBlank()) {
            _topics.value = Resource.Success(originalTopics)
            return
        }
        
        // Chuyển query thành chữ thường không dấu để so sánh
        val normalizedQuery = query.removeAccent()
        
        val filteredTopics = originalTopics.filter { topic ->
            topic.name.removeAccent().contains(normalizedQuery) || 
            topic.category.removeAccent().contains(normalizedQuery)
        }
        
        _topics.value = Resource.Success(filteredTopics)
    }
    
    /**
     * Hủy tìm kiếm và khôi phục danh sách ban đầu
     */
    fun clearSearch() {
        _topics.value = Resource.Success(originalTopics)
    }
    
    /**
     * Đánh dấu/bỏ đánh dấu chủ đề yêu thích
     */
    fun toggleFavorite(topicId: String) {
        val currentTopics = originalTopics.toMutableList()
        val index = currentTopics.indexOfFirst { it.id == topicId }
        
        if (index >= 0) {
            val topic = currentTopics[index]
            val updatedTopic = topic.copy(isFavorite = !topic.isFavorite)
            currentTopics[index] = updatedTopic
            
            originalTopics = currentTopics
            
            // Cập nhật danh sách hiển thị hiện tại (có thể đang ở chế độ tìm kiếm)
            val displayedTopics = (_topics.value as? Resource.Success)?.data ?: emptyList()
            val displayIndex = displayedTopics.indexOfFirst { it.id == topicId }
            
            if (displayIndex >= 0) {
                val updatedDisplayList = displayedTopics.toMutableList()
                updatedDisplayList[displayIndex] = updatedTopic
                _topics.value = Resource.Success(updatedDisplayList)
            }
            
            // Cập nhật danh sách yêu thích
            _favoriteTopics.value = currentTopics.filter { it.isFavorite }.take(2)
        }
    }
} 