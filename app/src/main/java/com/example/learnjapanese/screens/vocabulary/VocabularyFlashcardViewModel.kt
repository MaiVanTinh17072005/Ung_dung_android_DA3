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
 * ViewModel cho màn hình flashcard từ vựng
 */
@HiltViewModel
class VocabularyFlashcardViewModel @Inject constructor(
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
    
    // State các từ đã được đánh dấu là đã học
    private val _learnedWords = MutableStateFlow<Set<String>>(setOf())
    val learnedWords: StateFlow<Set<String>> = _learnedWords.asStateFlow()
    
    // State của việc hoàn thành
    private val _completionStatus = MutableStateFlow<Resource<Boolean>?>(null)
    val completionStatus: StateFlow<Resource<Boolean>?> = _completionStatus.asStateFlow()
    
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
            
            try {
                vocabularyRepository.getVocabularyTopics().fold(
                    onSuccess = { topicResponses ->
                        val topicResponse = topicResponses.find { it.topicId == topicId }
                            ?: topicResponses.flatMap { it.children ?: emptyList() }.find { it.topicId == topicId }
                        
                        if (topicResponse != null) {
                            val topic = topicResponse.toUiTopic()
                            _topicDetail.value = Resource.Success(topic)
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
        val currentLearned = _learnedWords.value.toMutableSet()
        currentLearned.add(wordId)
        _learnedWords.value = currentLearned
        
        // Cập nhật trạng thái đã học trong danh sách từ vựng
        val currentWords = (_words.value as? Resource.Success)?.data?.toMutableList() ?: return
        val index = currentWords.indexOfFirst { it.id == wordId }
        
        if (index >= 0) {
            val word = currentWords[index]
            val updatedWord = word.copy(isLearned = true)
            currentWords[index] = updatedWord
            
            _words.value = Resource.Success(currentWords)
            
            // Tính toán lại tiến độ
            updateProgress(currentWords)
        }
    }
    
    /**
     * Cập nhật tiến độ học
     */
    private fun updateProgress(words: List<VocabularyWord>) {
        val learnedCount = words.count { it.isLearned }
        val progress = if (words.isNotEmpty()) learnedCount.toFloat() / words.size else 0f
        
        // Cập nhật lại thông tin chủ đề
        (_topicDetail.value as? Resource.Success)?.data?.let { currentTopic ->
            _topicDetail.value = Resource.Success(
                currentTopic.copy(
                    progress = progress,
                    words = words
                )
            )
        }
    }
    
    /**
     * Hoàn thành phiên học flashcard
     */
    fun completeFlashcardSession() {
        val currentWords = (_words.value as? Resource.Success)?.data ?: return
        val learnedCount = _learnedWords.value.size
        val progress = if (currentWords.isNotEmpty()) learnedCount.toFloat() / currentWords.size else 0f
        val scorePercent = (progress * 100).toInt()
        
        viewModelScope.launch {
            _completionStatus.value = Resource.Loading()
            
            try {
                // Gửi kết quả lên server
                // Giả sử ID người dùng là "u1" - trong thực tế cần lấy từ nguồn khác (ví dụ: UserRepository)
                vocabularyRepository.completeVocabularyTopic(topicId, "u1", scorePercent).fold(
                    onSuccess = { response ->
                        _completionStatus.value = Resource.Success(response.success)
                    },
                    onFailure = { exception ->
                        _completionStatus.value = Resource.Error(exception.message ?: "Không thể hoàn thành phiên học")
                    }
                )
            } catch (e: Exception) {
                _completionStatus.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
    }
    
    /**
     * Reset trạng thái hoàn thành
     */
    fun resetCompletionStatus() {
        _completionStatus.value = null
    }
} 