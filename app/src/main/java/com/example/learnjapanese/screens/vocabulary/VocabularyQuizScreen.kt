package com.example.learnjapanese.screens.vocabulary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.data.model.QuizQuestionType
import com.example.learnjapanese.data.model.VocabularyTopic
import com.example.learnjapanese.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyQuizScreen(
    topicId: String,
    onBack: () -> Unit = {},
    onComplete: (Int, Int) -> Unit = { _, _ -> },
    viewModel: VocabularyQuizViewModel = hiltViewModel()
) {
    // Lấy state từ ViewModel
    val topicDetailResource by viewModel.topicDetail.collectAsState()
    val quizQuestionsResource by viewModel.quizQuestions.collectAsState()
    val quizResult by viewModel.quizResult.collectAsState()
    
    // State cho quiz
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableIntStateOf(-1) }
    var answerSubmitted by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    
    // State cho dialog hoàn thành
    var showCompletionDialog by remember { mutableStateOf(false) }
    
    // Gửi kết quả khi hoàn thành quiz
    LaunchedEffect(quizResult) {
        quizResult?.let { result ->
            showCompletionDialog = true
        }
    }
    
    val getCurrentQuestion = {
        when (val resource = quizQuestionsResource) {
            is Resource.Success -> {
                val questions = resource.data
                if (questions != null && currentQuestionIndex < questions.size) {
                    questions[currentQuestionIndex]
                } else null
            }
            else -> null
        }
    }
    
    // Check if answer is correct
    val isCurrentAnswerCorrect = {
        val question = getCurrentQuestion()
        question != null && selectedAnswerIndex == question.correctAnswerIndex
    }
    
    // Functions to handle user interactions
    val selectAnswer = { index: Int ->
        if (!answerSubmitted) {
            selectedAnswerIndex = index
        }
    }
    
    val submitAnswer = {
        if (selectedAnswerIndex >= 0 && !answerSubmitted) {
            answerSubmitted = true
            if (isCurrentAnswerCorrect()) {
                correctAnswers++
            }
        }
    }
    
    val moveToNextQuestion = {
        when (val resource = quizQuestionsResource) {
            is Resource.Success -> {
                val questions = resource.data
                if (questions != null && currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                    selectedAnswerIndex = -1
                    answerSubmitted = false
                } else {
                    // Quiz completed - gửi kết quả lên server
                    viewModel.completeQuiz(correctAnswers, questions?.size ?: 0)
                }
            }
            else -> { /* Không làm gì nếu dữ liệu đang tải hoặc lỗi */ }
        }
    }
    
    // Dialog hoàn thành quiz
    if (showCompletionDialog && quizResult != null) {
        QuizCompletionDialog(
            quizResult = quizResult!!,
            onDismiss = {
                showCompletionDialog = false
                viewModel.clearQuizResult()
                onComplete(quizResult!!.correctAnswers, quizResult!!.totalQuestions)
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (topicDetailResource) {
                        is Resource.Success -> {
                            val topic = (topicDetailResource as Resource.Success<VocabularyTopic>).data
                            Text(
                                text = "Kiểm tra: ${topic?.name ?: ""}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        else -> {
                            Text(
                                "Kiểm tra từ vựng",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val questionsResource = quizQuestionsResource) {
                is Resource.Loading -> {
                    // Hiển thị loading
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                is Resource.Success -> {
                    val questions = questionsResource.data
                    
                    if (questions?.isEmpty() != false) {
                        // Hiển thị thông báo nếu không có câu hỏi
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Không đủ từ vựng để tạo bài kiểm tra",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = onBack,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("Quay lại")
                                }
                            }
                        }
                    } else {
                        val currentQuestion = questions[currentQuestionIndex]
                        
                        // Sử dụng LazyColumn với một item để cho phép cuộn nếu màn hình nhỏ
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            // Quiz progress
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Câu hỏi ${currentQuestionIndex + 1} / ${questions?.size ?: 0}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "Đúng: $correctAnswers",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    LinearProgressIndicator(
                                        progress = { (currentQuestionIndex + 1).toFloat() / (questions?.size ?: 1) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    )
                                }
                            }
                            
                            // Question
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = if (currentQuestion.type == QuizQuestionType.JAPANESE_TO_MEANING) 
                                                "Nghĩa của từ này là gì?" 
                                            else 
                                                "Từ tiếng Nhật nào có nghĩa sau?",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                        
                                        Spacer(modifier = Modifier.height(16.dp))
                                        
                                        if (currentQuestion.type == QuizQuestionType.JAPANESE_TO_MEANING) {
                                            // Hiển thị từ tiếng Nhật và yêu cầu chọn nghĩa
                                            Text(
                                                text = currentQuestion.questionWord.word,
                                                style = MaterialTheme.typography.headlineMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center
                                            )
                                            
                                            Text(
                                                text = currentQuestion.questionWord.reading,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                                textAlign = TextAlign.Center
                                            )
                                        } else {
                                            // Hiển thị nghĩa và yêu cầu chọn từ tiếng Nhật
                                            Text(
                                                text = currentQuestion.questionWord.meaning,
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        
                                        IconButton(
                                            onClick = {},
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                                .size(40.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.VolumeUp,
                                                contentDescription = "Nghe phát âm",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            
                            // Answer options
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    currentQuestion.answers.forEachIndexed { index, answer ->
                                        val isSelected = selectedAnswerIndex == index
                                        val isCorrect = index == currentQuestion.correctAnswerIndex
                                        val showResult = answerSubmitted
                                        
                                        val backgroundColor = when {
                                            !showResult && isSelected -> MaterialTheme.colorScheme.primaryContainer
                                            showResult && isCorrect -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                            showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                                            else -> MaterialTheme.colorScheme.surface
                                        }
                                        
                                        val borderColor = when {
                                            !showResult && isSelected -> MaterialTheme.colorScheme.primary
                                            showResult && isCorrect -> MaterialTheme.colorScheme.primary
                                            showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                        }
                                        
                                        val textColor = when {
                                            !showResult && isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                                            showResult && isCorrect -> MaterialTheme.colorScheme.primary
                                            showResult && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                        
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .border(
                                                    width = 2.dp,
                                                    color = borderColor,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable(enabled = !answerSubmitted) {
                                                    selectAnswer(index)
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = backgroundColor
                                            ),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (showResult) {
                                                    Icon(
                                                        imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                                        contentDescription = null,
                                                        tint = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                }
                                                
                                                Column(modifier = Modifier.weight(1f)) {
                                                    if (currentQuestion.type == QuizQuestionType.JAPANESE_TO_MEANING) {
                                                        // Hiển thị nghĩa
                                                        Text(
                                                            text = answer.meaning,
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            color = textColor
                                                        )
                                                    } else {
                                                        // Hiển thị từ tiếng Nhật và cách đọc
                                                        Text(
                                                            text = answer.word,
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            color = textColor,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        Text(
                                                            text = answer.reading,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = textColor.copy(alpha = 0.7f)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            
                            // Controls
                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 16.dp)
                                ) {
                                    if (!answerSubmitted) {
                                        // Show submit button if an answer is selected
                                        Button(
                                            onClick = submitAnswer,
                                            enabled = selectedAnswerIndex >= 0,
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                            )
                                        ) {
                                            Text("Kiểm tra")
                                        }
                                    } else {
                                        // Show continue button after submitting
                                        Button(
                                            onClick = moveToNextQuestion,
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (currentQuestionIndex == questions.size - 1)
                                                    MaterialTheme.colorScheme.tertiary
                                                else
                                                    MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text(
                                                text = if (currentQuestionIndex == questions.size - 1)
                                                    "Hoàn thành"
                                                else
                                                    "Câu tiếp theo"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                is Resource.Error -> {
                    // Hiển thị lỗi
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Không thể tải câu hỏi",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = questionsResource.message ?: "Đã xảy ra lỗi không xác định",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onBack,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Quay lại")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizCompletionDialog(
    quizResult: VocabularyQuizViewModel.QuizResult,
    onDismiss: () -> Unit
) {
    val correctRatio = quizResult.correctAnswers.toFloat() / quizResult.totalQuestions
    
    // Chọn thông điệp động viên dựa trên tỷ lệ trả lời đúng
    val motivationalMessage = when {
        correctRatio >= 0.9 -> "Tuyệt vời! Bạn thật xuất sắc!"
        correctRatio >= 0.8 -> "Rất giỏi! Bạn đã làm rất tốt!"
        correctRatio >= 0.7 -> "Khá tốt! Bạn đang tiến bộ rõ rệt!"
        correctRatio >= 0.6 -> "Cố gắng hơn nữa, bạn sắp thành công rồi!"
        correctRatio >= 0.5 -> "Bạn đã hoàn thành một nửa chặng đường, hãy tiếp tục!"
        correctRatio >= 0.4 -> "Đừng nản lòng, hãy tiếp tục luyện tập!"
        correctRatio >= 0.3 -> "Mỗi lần thất bại là một bài học quý giá!"
        else -> "Hãy kiên nhẫn và tiếp tục học, thành công sẽ đến!"
    }
    
    // Màu gradient dựa trên kết quả
    val gradientColors = when {
        correctRatio >= 0.8 -> listOf(Color(0xFF4CAF50), Color(0xFF8BC34A)) // Xanh lá
        correctRatio >= 0.6 -> listOf(Color(0xFF03A9F4), Color(0xFF00BCD4)) // Xanh dương
        correctRatio >= 0.4 -> listOf(Color(0xFFFFC107), Color(0xFFFF9800)) // Cam vàng
        else -> listOf(Color(0xFFFF5722), Color(0xFFF44336)) // Đỏ cam
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon thành tích
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = gradientColors
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Thông điệp động viên
                Text(
                    text = motivationalMessage,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Kết quả
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${quizResult.correctAnswers}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "/",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${quizResult.totalQuestions}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Hiển thị điểm số
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${quizResult.score}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Nút xác nhận
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VocabularyQuizScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Để preview hoạt động, không sử dụng hiltViewModel() vì không có Hilt trong preview
            VocabularyQuizScreen(topicId = "1")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCompletionDialogPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            QuizCompletionDialog(
                quizResult = VocabularyQuizViewModel.QuizResult(
                    correctAnswers = 8,
                    totalQuestions = 10,
                    score = 80,
                    success = true
                ),
                onDismiss = {}
            )
        }
    }
} 