package com.example.learnjapanese.screens.vocabulary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.VocabularyTopic
import com.example.learnjapanese.data.model.VocabularyWord
import com.example.learnjapanese.data.repository.VocabularyRepository
import com.example.learnjapanese.utils.Resource
import com.example.learnjapanese.utils.toUiTopic
import com.example.learnjapanese.utils.toUiWordList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho màn hình chi tiết chủ đề từ vựng
 */
@HiltViewModel
class VocabularyDetailViewModel @Inject constructor(
    private val vocabularyRepository: VocabularyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // ID của chủ đề được truyền vào từ navigation
    private val topicId: String = savedStateHandle.get<String>("topicId") ?: ""
    
    // State của thông tin chủ đề
    private val _topicDetail = MutableStateFlow<Resource<VocabularyTopic>>(Resource.Loading())
    val topicDetail: StateFlow<Resource<VocabularyTopic>> = _topicDetail.asStateFlow()
    
    // State của danh sách từ vựng
    private val _words = MutableStateFlow<Resource<List<VocabularyWord>>>(Resource.Loading())
    val words: StateFlow<Resource<List<VocabularyWord>>> = _words.asStateFlow()
    
    // State đánh dấu yêu thích
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    
    init {
        loadTopicDetail()
        loadWords()
    }
    
    /**
     * Tải thông tin chi tiết của chủ đề
     */
    private fun loadTopicDetail() {
        viewModelScope.launch {
            _topicDetail.value = Resource.Loading()
            
            // Lấy danh sách chủ đề để tìm chủ đề với ID hiện tại
            // Trong thực tế, nên có API riêng để lấy chi tiết 1 chủ đề
            try {
                vocabularyRepository.getVocabularyTopics().fold(
                    onSuccess = { topicResponses ->
                        // Tìm chủ đề hiện tại trong danh sách
                        val topicResponse = topicResponses.find { it.topicId == topicId }
                            ?: topicResponses.flatMap { it.children ?: emptyList() }.find { it.topicId == topicId }
                        
                        if (topicResponse != null) {
                            val topic = topicResponse.toUiTopic()
                            _topicDetail.value = Resource.Success(topic)
                            _isFavorite.value = false // Mặc định chưa đánh dấu yêu thích
                        } else {
                            _topicDetail.value = Resource.Error("Không tìm thấy chủ đề")
                        }
                    },
                    onFailure = { exception ->
                        _topicDetail.value = Resource.Error(exception.message ?: "Không thể tải thông tin chủ đề")
                    }
                )
            } catch (e: Exception) {
                _topicDetail.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
    }
    
    /**
     * Tải danh sách từ vựng thuộc chủ đề
     */
    fun loadWords() {
        viewModelScope.launch {
            _words.value = Resource.Loading()
            
            try {
                vocabularyRepository.getVocabularyWords(topicId).fold(
                    onSuccess = { wordResponses ->
                        val words = wordResponses.toUiWordList()
                        _words.value = Resource.Success(words)
                        
                        // Cập nhật tổng số từ trong chủ đề
                        (_topicDetail.value as? Resource.Success)?.data?.let { currentTopic ->
                            _topicDetail.value = Resource.Success(
                                currentTopic.copy(
                                    totalWords = words.size,
                                    words = words
                                )
                            )
                        }
                    },
                    onFailure = { exception ->
                        _words.value = Resource.Error(exception.message ?: "Không thể tải danh sách từ vựng")
                    }
                )
            } catch (e: Exception) {
                _words.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
    }
    
    /**
     * Đánh dấu từ vựng đã học
     */
    fun markWordAsLearned(wordId: String) {
        val currentWords = (_words.value as? Resource.Success)?.data?.toMutableList() ?: return
        val index = currentWords.indexOfFirst { it.id == wordId }
        
        if (index >= 0) {
            val word = currentWords[index]
            val updatedWord = word.copy(isLearned = true)
            currentWords[index] = updatedWord
            
            _words.value = Resource.Success(currentWords)
            
            // Tính toán lại tiến độ
            val learnedCount = currentWords.count { it.isLearned }
            val progress = if (currentWords.isNotEmpty()) learnedCount.toFloat() / currentWords.size else 0f
            
            // Cập nhật lại thông tin chủ đề
            (_topicDetail.value as? Resource.Success)?.data?.let { currentTopic ->
                _topicDetail.value = Resource.Success(
                    currentTopic.copy(
                        progress = progress,
                        words = currentWords
                    )
                )
            }
        }
    }
    
    /**
     * Đánh dấu/bỏ đánh dấu chủ đề yêu thích
     */
    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
        
        // Cập nhật lại thông tin chủ đề
        (_topicDetail.value as? Resource.Success)?.data?.let { currentTopic ->
            _topicDetail.value = Resource.Success(
                currentTopic.copy(
                    isFavorite = _isFavorite.value
                )
            )
        }
        
        // TODO: Cập nhật trạng thái yêu thích lên server hoặc lưu vào local database
    }
} 