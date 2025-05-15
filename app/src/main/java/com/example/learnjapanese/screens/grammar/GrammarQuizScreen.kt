package com.example.learnjapanese.screens.grammar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrammarQuizScreen(
    grammarIds: List<String>,
    onBack: () -> Unit = {},
    onComplete: (Int, Int) -> Unit = { _, _ -> },
    viewModel: GrammarQuizViewModel = hiltViewModel()
) {
    // Lấy state từ ViewModel
    val quizQuestionsResource by viewModel.quizQuestions.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val correctAnswers by viewModel.correctAnswers.collectAsState()
    val isQuizCompleted by viewModel.isQuizCompleted.collectAsState()
    
    // State cho UI
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    
    // Hiển thị màn hình kết quả khi hoàn thành bài kiểm tra
    if (isQuizCompleted) {
        when (val resource = quizQuestionsResource) {
            is Resource.Success -> {
                val totalQuestions = resource.data?.size ?: 0
                GrammarQuizResultScreen(
                    correctAnswers = correctAnswers,
                    totalQuestions = totalQuestions,
                    onBackToGrammar = {
                        onComplete(correctAnswers, totalQuestions)
                        onBack()
                    }
                )
            }
            else -> {
                // Nếu không có dữ liệu, quay về màn hình trước
                LaunchedEffect(Unit) {
                    onBack()
                }
            }
        }
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kiểm tra ngữ pháp",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
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
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val resource = quizQuestionsResource) {
                is Resource.Success -> {
                    val questions = resource.data
                    
                    if (questions.isNullOrEmpty()) {
                        // Hiển thị thông báo nếu không có câu hỏi
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Không có câu hỏi cho ngữ pháp này",
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
                    } else {
                        // Tiến độ
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
                                    text = "Câu hỏi ${currentQuestionIndex + 1} / ${questions.size}",
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
                                progress = { (currentQuestionIndex + 1).toFloat() / questions.size },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            )
                        }
                        
                        // Nội dung câu hỏi
                        if (currentQuestionIndex < questions.size) {
                            val currentQuestion = questions[currentQuestionIndex]
                            
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Câu hỏi
                                item {
                                    QuestionCard(
                                        question = currentQuestion.questionText,
                                        grammarPattern = currentQuestion.grammarPattern
                                    )
                                }
                                
                                // Các lựa chọn
                                item {
                                    AnswerOptions(
                                        options = currentQuestion.answers,
                                        selectedOptionIndex = selectedOptionIndex,
                                        isAnswerSubmitted = isAnswerSubmitted,
                                        correctOptionIndex = currentQuestion.correctAnswerIndex,
                                        onOptionSelected = { index ->
                                            if (!isAnswerSubmitted) {
                                                selectedOptionIndex = index
                                            }
                                        }
                                    )
                                }
                                
                                // Giải thích khi đã trả lời
                                item {
                                    AnimatedVisibility(
                                        visible = isAnswerSubmitted,
                                        enter = fadeIn(animationSpec = tween(300)) +
                                                expandVertically(animationSpec = tween(300)),
                                        exit = fadeOut(animationSpec = tween(300)) +
                                                shrinkVertically(animationSpec = tween(300))
                                    ) {
                                        ExplanationCard(
                                            explanation = currentQuestion.explanation,
                                            isCorrect = selectedOptionIndex == currentQuestion.correctAnswerIndex
                                        )
                                    }
                                }
                            }
                            
                            // Thanh điều hướng
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                if (!isAnswerSubmitted) {
                                    // Nút kiểm tra
                                    Button(
                                        onClick = {
                                            isAnswerSubmitted = true
                                            val isCorrect = viewModel.checkAnswer(selectedOptionIndex)
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = selectedOptionIndex >= 0,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                        )
                                    ) {
                                        Text("Kiểm tra")
                                    }
                                } else {
                                    // Nút tiếp theo
                                    Button(
                                        onClick = {
                                            // Reset state cho câu hỏi tiếp theo
                                            selectedOptionIndex = -1
                                            isAnswerSubmitted = false
                                            
                                            // Chuyển đến câu hỏi tiếp theo
                                            viewModel.nextQuestion()
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text(
                                            text = if (currentQuestionIndex < questions.size - 1) "Tiếp theo" else "Hoàn thành",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                is Resource.Loading -> {
                    // Hiển thị loading
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Đang tạo câu hỏi...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                is Resource.Error -> {
                    // Hiển thị lỗi
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Đã xảy ra lỗi",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = resource.message ?: "Không thể tạo câu hỏi kiểm tra",
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

@Composable
fun QuestionCard(
    question: String,
    grammarPattern: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            if (grammarPattern.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = grammarPattern,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun AnswerOptions(
    options: List<String>,
    selectedOptionIndex: Int,
    isAnswerSubmitted: Boolean,
    correctOptionIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEachIndexed { index, option ->
            // Xác định màu sắc dựa trên trạng thái
            val backgroundColor = when {
                isAnswerSubmitted && index == correctOptionIndex -> MaterialTheme.colorScheme.primaryContainer
                isAnswerSubmitted && index == selectedOptionIndex -> MaterialTheme.colorScheme.errorContainer
                selectedOptionIndex == index -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.surface
            }
            
            val borderColor = when {
                isAnswerSubmitted && index == correctOptionIndex -> MaterialTheme.colorScheme.primary
                isAnswerSubmitted && index == selectedOptionIndex -> MaterialTheme.colorScheme.error
                selectedOptionIndex == index -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            }
            
            val textColor = when {
                isAnswerSubmitted && index == correctOptionIndex -> MaterialTheme.colorScheme.onPrimaryContainer
                isAnswerSubmitted && index == selectedOptionIndex -> MaterialTheme.colorScheme.onErrorContainer
                selectedOptionIndex == index -> MaterialTheme.colorScheme.onPrimaryContainer
                else -> MaterialTheme.colorScheme.onSurface
            }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isAnswerSubmitted) { onOptionSelected(index) }
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (isAnswerSubmitted) {
                        if (index == correctOptionIndex) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Đáp án đúng",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else if (index == selectedOptionIndex) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Đáp án sai",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExplanationCard(explanation: String, isCorrect: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect) 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            else 
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = if (isCorrect) "Chính xác!" else "Sai rồi!",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isCorrect) 
                    MaterialTheme.colorScheme.primary
                else 
                    MaterialTheme.colorScheme.error
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCorrect) 
                    MaterialTheme.colorScheme.onPrimaryContainer
                else 
                    MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun GrammarQuizResultScreen(
    correctAnswers: Int,
    totalQuestions: Int,
    onBackToGrammar: () -> Unit
) {
    val percentage = (correctAnswers.toFloat() / totalQuestions) * 100
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hoàn thành!",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$correctAnswers/$totalQuestions",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Text(
                    text = "Đúng ${percentage.toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = when {
                percentage >= 80 -> "Xuất sắc! Bạn đã nắm vững ngữ pháp này."
                percentage >= 60 -> "Tốt! Bạn đã hiểu phần lớn ngữ pháp."
                percentage >= 40 -> "Được! Hãy ôn luyện thêm một chút."
                else -> "Cần cố gắng hơn. Hãy xem lại ngữ pháp này nhé!"
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onBackToGrammar,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Quay lại")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GrammarQuizScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GrammarQuizScreen(
                grammarIds = listOf("g1", "g2", "g3")
            )
        }
    }
} 