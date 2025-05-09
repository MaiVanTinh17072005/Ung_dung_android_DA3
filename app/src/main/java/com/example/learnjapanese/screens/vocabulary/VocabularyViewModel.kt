package com.example.learnjapanese.screens.vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.VocabularyTopic
import com.example.learnjapanese.data.repository.VocabularyRepository
import com.example.learnjapanese.utils.Resource
import com.example.learnjapanese.utils.toUiTopicList
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
                vocabularyRepository.getVocabularyTopics().fold(
                    onSuccess = { topicResponses ->
                        val uiTopics = topicResponses.toUiTopicList()
                        _topics.value = Resource.Success(uiTopics)
                        
                        // Hiện tại chưa có API cho chủ đề yêu thích nên giả định 2 chủ đề đầu là yêu thích
                        // Trong thực tế sẽ cần API riêng hoặc truy vấn local database để lấy
                        _favoriteTopics.value = uiTopics.take(2).map { it.copy(isFavorite = true) }
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
     */
    fun searchTopics(query: String) {
        val currentTopics = (_topics.value as? Resource.Success)?.data ?: return
        
        if (query.isBlank()) {
            _topics.value = Resource.Success(currentTopics)
            return
        }
        
        val filteredTopics = currentTopics.filter { topic ->
            topic.name.contains(query, ignoreCase = true) || 
            topic.category.contains(query, ignoreCase = true)
        }
        
        _topics.value = Resource.Success(filteredTopics)
    }
    
    /**
     * Đánh dấu/bỏ đánh dấu chủ đề yêu thích
     */
    fun toggleFavorite(topicId: String) {
        val currentTopics = (_topics.value as? Resource.Success)?.data?.toMutableList() ?: return
        val index = currentTopics.indexOfFirst { it.id == topicId }
        
        if (index >= 0) {
            val topic = currentTopics[index]
            val updatedTopic = topic.copy(isFavorite = !topic.isFavorite)
            currentTopics[index] = updatedTopic
            
            _topics.value = Resource.Success(currentTopics)
            
            // Cập nhật danh sách yêu thích
            _favoriteTopics.value = currentTopics.filter { it.isFavorite }.take(2)
        }
    }
} 