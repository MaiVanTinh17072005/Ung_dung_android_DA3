package com.example.learnjapanese.screens.grammar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnjapanese.data.model.GrammarItem
import com.example.learnjapanese.data.model.GrammarQuizQuestion
import com.example.learnjapanese.data.model.QuizGenerationRequest
import com.example.learnjapanese.data.repository.GrammarRepository
import com.example.learnjapanese.utils.Resource
import com.example.learnjapanese.utils.toUiModelList
import com.example.learnjapanese.utils.toUiModelList as toQuizUiModelList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho màn hình quiz ngữ pháp
 */
@HiltViewModel
class GrammarQuizViewModel @Inject constructor(
    private val grammarRepository: GrammarRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // Lấy danh sách ID ngữ pháp từ navigation arguments
    private val grammarIdsString: String = checkNotNull(savedStateHandle["ids"])
    private val grammarIds: List<String> = grammarIdsString.split(",")
    
    // State của danh sách ngữ pháp cho quiz
    private val _grammarItems = MutableStateFlow<Resource<List<GrammarItem>>>(Resource.Loading())
    val grammarItems: StateFlow<Resource<List<GrammarItem>>> = _grammarItems.asStateFlow()
    
    // State của các câu hỏi quiz
    private val _quizQuestions = MutableStateFlow<Resource<List<GrammarQuizQuestion>>>(Resource.Loading())
    val quizQuestions: StateFlow<Resource<List<GrammarQuizQuestion>>> = _quizQuestions.asStateFlow()
    
    // State theo dõi câu hỏi hiện tại
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()
    
    // State theo dõi số câu trả lời đúng
    private val _correctAnswers = MutableStateFlow(0)
    val correctAnswers: StateFlow<Int> = _correctAnswers.asStateFlow()
    
    // State theo dõi tiến độ làm quiz
    private val _isQuizCompleted = MutableStateFlow(false)
    val isQuizCompleted: StateFlow<Boolean> = _isQuizCompleted.asStateFlow()
    
    init {
        loadGrammarItems()
    }
    
    /**
     * Tải danh sách ngữ pháp theo ID và tạo câu hỏi quiz
     */
    private fun loadGrammarItems() {
        viewModelScope.launch {
            _grammarItems.value = Resource.Loading()
            
            try {
                val result = grammarRepository.getGrammarBatch(grammarIds)
                
                result.fold(
                    onSuccess = { grammarResponses ->
                        // Chuyển đổi từ model API sang model UI
                        val uiGrammarItems = grammarResponses.toUiModelList()
                        _grammarItems.value = Resource.Success(uiGrammarItems)
                        
                        // Tạo câu hỏi quiz từ API thay vì tạo locally
                        generateQuizQuestionsFromApi()
                    },
                    onFailure = { exception ->
                        _grammarItems.value = Resource.Error(exception.message ?: "Không thể tải danh sách ngữ pháp")
                        _quizQuestions.value = Resource.Error(exception.message ?: "Không thể tạo câu hỏi quiz")
                    }
                )
            } catch (e: Exception) {
                _grammarItems.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
                _quizQuestions.value = Resource.Error(e.message ?: "Đã xảy ra lỗi không xác định")
            }
        }
    }
    
    /**
     * Tạo danh sách câu hỏi quiz từ API sử dụng AI
     */
    private fun generateQuizQuestionsFromApi() {
        viewModelScope.launch {
            _quizQuestions.value = Resource.Loading()
            
            try {
                // Chuẩn bị request để tạo quiz
                val request = QuizGenerationRequest(
                    grammarIds = grammarIds,
                    questionCount = 10,
                    questionTypes = listOf(
                        "MEANING_TO_PATTERN", 
                        "PATTERN_TO_MEANING", 
                        "COMPLETE_SENTENCE", 
                        "ERROR_CORRECTION"
                    ),
                    difficulty = "MEDIUM"
                )
                
                // Gọi API tạo quiz
                val result = grammarRepository.generateQuiz(request)
                
                result.fold(
                    onSuccess = { quizResponse ->
                        // Chuyển đổi từ model API sang model UI
                        val uiQuestions = quizResponse.questions.toQuizUiModelList()
                        _quizQuestions.value = Resource.Success(uiQuestions)
                    },
                    onFailure = { exception ->
                        _quizQuestions.value = Resource.Error(exception.message ?: "Không thể tạo câu hỏi quiz từ API")
                        // Fallback: tạo câu hỏi locally nếu API thất bại
                        fallbackToLocalQuizGeneration()
                    }
                )
            } catch (e: Exception) {
                _quizQuestions.value = Resource.Error(e.message ?: "Đã xảy ra lỗi khi tạo câu hỏi quiz")
                // Fallback: tạo câu hỏi locally nếu có exception
                fallbackToLocalQuizGeneration()
            }
        }
    }
    
    /**
     * Tạo câu hỏi locally trong trường hợp API thất bại
     */
    private fun fallbackToLocalQuizGeneration() {
        val currentGrammarItems = (_grammarItems.value as? Resource.Success)?.data ?: return
        
        if (currentGrammarItems.isEmpty()) {
            _quizQuestions.value = Resource.Error("Không có ngữ pháp để tạo câu hỏi")
            return
        }
        
        val questions = mutableListOf<GrammarQuizQuestion>()
        
        // Tạo câu hỏi kiểu "Chọn nghĩa đúng"
        currentGrammarItems.forEach { grammar ->
            // Tạo các lựa chọn bao gồm nghĩa đúng và các nghĩa sai từ các mục khác
            val correctAnswer = grammar.summary
            val wrongAnswers = currentGrammarItems
                .filter { it.id != grammar.id }
                .map { it.summary }
                .shuffled()
                .take(3)
            
            // Kết hợp đáp án đúng và sai
            val allAnswers = (wrongAnswers + correctAnswer).shuffled()
            val correctIndex = allAnswers.indexOf(correctAnswer)
            
            // Tạo câu hỏi
            questions.add(
                GrammarQuizQuestion(
                    questionId = grammar.id,
                    questionText = "Cấu trúc \"${grammar.title}\" có nghĩa là gì?",
                    answers = allAnswers,
                    correctAnswerIndex = correctIndex,
                    explanation = grammar.explanation ?: "Không có giải thích",
                    grammarPattern = grammar.title
                )
            )
        }
        
        // Tạo câu hỏi kiểu "Chọn cấu trúc đúng"
        currentGrammarItems.forEach { grammar ->
            // Tạo các lựa chọn bao gồm cấu trúc đúng và các cấu trúc sai từ các mục khác
            val correctAnswer = grammar.title
            val wrongAnswers = currentGrammarItems
                .filter { it.id != grammar.id }
                .map { it.title }
                .shuffled()
                .take(3)
            
            // Kết hợp đáp án đúng và sai
            val allAnswers = (wrongAnswers + correctAnswer).shuffled()
            val correctIndex = allAnswers.indexOf(correctAnswer)
            
            // Tạo câu hỏi
            questions.add(
                GrammarQuizQuestion(
                    questionId = grammar.id + "_reverse",
                    questionText = "Ý nghĩa \"${grammar.summary}\" tương ứng với cấu trúc nào?",
                    answers = allAnswers,
                    correctAnswerIndex = correctIndex,
                    explanation = grammar.explanation ?: "Không có giải thích",
                    grammarPattern = grammar.title
                )
            )
        }
        
        // Xáo trộn và cập nhật state
        _quizQuestions.value = Resource.Success(questions.shuffled())
    }
    
    /**
     * Chuyển đến câu hỏi tiếp theo
     */
    fun nextQuestion() {
        val currentQuestions = (_quizQuestions.value as? Resource.Success)?.data ?: return
        val currentIndex = _currentQuestionIndex.value
        
        if (currentIndex < currentQuestions.size - 1) {
            _currentQuestionIndex.value = currentIndex + 1
        } else {
            _isQuizCompleted.value = true
        }
    }
    
    /**
     * Kiểm tra câu trả lời và cập nhật điểm
     */
    fun checkAnswer(selectedAnswerIndex: Int): Boolean {
        val currentQuestions = (_quizQuestions.value as? Resource.Success)?.data ?: return false
        val currentIndex = _currentQuestionIndex.value
        
        if (currentIndex >= currentQuestions.size) return false
        
        val correctIndex = currentQuestions[currentIndex].correctAnswerIndex
        val isCorrect = selectedAnswerIndex == correctIndex
        
        if (isCorrect) {
            _correctAnswers.value = _correctAnswers.value + 1
        }
        
        return isCorrect
    }
    
    /**
     * Reset quiz để bắt đầu lại
     */
    fun resetQuiz() {
        _currentQuestionIndex.value = 0
        _correctAnswers.value = 0
        _isQuizCompleted.value = false
        
        // Xáo trộn lại các câu hỏi
        val currentQuestions = (_quizQuestions.value as? Resource.Success)?.data ?: return
        _quizQuestions.value = Resource.Success(currentQuestions.shuffled())
    }
} 