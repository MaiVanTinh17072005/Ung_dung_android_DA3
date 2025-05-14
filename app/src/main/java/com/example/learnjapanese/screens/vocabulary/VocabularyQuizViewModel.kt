package com.example.learnjapanese.screens.vocabulary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.QuizQuestion
import com.example.learnjapanese.data.model.QuizQuestionType
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
import kotlin.random.Random

/**
 * ViewModel cho màn hình quiz từ vựng
 */
@HiltViewModel
class VocabularyQuizViewModel @Inject constructor(
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
    
    // State của danh sách câu hỏi
    private val _quizQuestions = MutableStateFlow<Resource<List<QuizQuestion>>>(Resource.Loading())
    val quizQuestions: StateFlow<Resource<List<QuizQuestion>>> = _quizQuestions.asStateFlow()
    
    // State của kết quả quiz
    private val _quizResult = MutableStateFlow<QuizResult?>(null)
    val quizResult: StateFlow<QuizResult?> = _quizResult.asStateFlow()
    
    init {
        loadWords()
    }
    
    /**
     * Tải danh sách từ vựng thuộc chủ đề
     */
    private fun loadWords() {
        viewModelScope.launch {
            _words.value = Resource.Loading()
            
            try {
                vocabularyRepository.getVocabularyWords(topicId).fold(
                    onSuccess = { wordResponses ->
                        val words = wordResponses.toUiWordList()
                        _words.value = Resource.Success(words)
                        
                        // Tạo câu hỏi quiz
                        generateQuizQuestions(words)
                        
                        // Tải thông tin chủ đề
                        loadTopicDetail()
                    },
                    onFailure = { exception ->
                        _words.value = Resource.Error(exception.message ?: "Không thể tải danh sách từ vựng")
                        _quizQuestions.value = Resource.Error(exception.message ?: "Không thể tải câu hỏi")
                    }
                )
            } catch (e: Exception) {
                _words.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
                _quizQuestions.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
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
     * Tạo danh sách câu hỏi quiz từ danh sách từ vựng
     */
    private fun generateQuizQuestions(words: List<VocabularyWord>) {
        if (words.size < 4) {
            _quizQuestions.value = Resource.Error("Cần ít nhất 4 từ để tạo quiz")
            return
        }
        
        val questions = mutableListOf<QuizQuestion>()
        val availableWords = words.toMutableList()
        
        // Sử dụng tất cả các từ vựng để tạo câu hỏi
        while (availableWords.isNotEmpty()) {
            // Chọn loại câu hỏi
            val questionType = if (Random.nextBoolean()) 
                QuizQuestionType.JAPANESE_TO_MEANING
            else 
                QuizQuestionType.MEANING_TO_JAPANESE
            
            // Chọn từ cho câu hỏi
            val wordIndex = Random.nextInt(availableWords.size)
            val questionWord = availableWords.removeAt(wordIndex)
            
            // Tạo các lựa chọn
            val wrongOptions = words.filter { it.id != questionWord.id }.shuffled().take(3)
            val allOptions = (wrongOptions + questionWord).shuffled()
            
            // Tìm vị trí đáp án đúng
            val correctAnswerIndex = allOptions.indexOf(questionWord)
            
            questions.add(
                QuizQuestion(
                    questionWord = questionWord,
                    answers = allOptions,
                    correctAnswerIndex = correctAnswerIndex,
                    type = questionType
                )
            )
        }
        
        _quizQuestions.value = Resource.Success(questions)
    }
    
    /**
     * Hoàn thành quiz và gửi kết quả lên server
     */
    fun completeQuiz(correctAnswers: Int, totalQuestions: Int) {
        val score = (correctAnswers.toFloat() / totalQuestions * 100).toInt()
        
        viewModelScope.launch {
            try {
                // Gửi kết quả lên server
                // Giả sử ID người dùng là "u1" - trong thực tế cần lấy từ nguồn khác (ví dụ: UserRepository)
                vocabularyRepository.completeVocabularyTopic(topicId, "u1", score).fold(
                    onSuccess = { response ->
                        _quizResult.value = QuizResult(
                            correctAnswers = correctAnswers,
                            totalQuestions = totalQuestions,
                            score = score,
                            success = response.success
                        )
                    },
                    onFailure = { exception ->
                        _quizResult.value = QuizResult(
                            correctAnswers = correctAnswers,
                            totalQuestions = totalQuestions,
                            score = score,
                            success = false,
                            errorMessage = exception.message
                        )
                    }
                )
            } catch (e: Exception) {
                _quizResult.value = QuizResult(
                    correctAnswers = correctAnswers,
                    totalQuestions = totalQuestions,
                    score = score,
                    success = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    /**
     * Clear kết quả quiz khi rời khỏi màn hình
     */
    fun clearQuizResult() {
        _quizResult.value = null
    }
    
    /**
     * Lớp chứa kết quả quiz
     */
    data class QuizResult(
        val correctAnswers: Int,
        val totalQuestions: Int,
        val score: Int,
        val success: Boolean,
        val errorMessage: String? = null
    )
} 