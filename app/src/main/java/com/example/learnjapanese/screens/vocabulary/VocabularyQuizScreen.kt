package com.example.learnjapanese.screens.vocabulary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.data.model.QuizQuestion
import com.example.learnjapanese.data.model.QuizQuestionType
import com.example.learnjapanese.data.model.VocabularyWord
import com.example.learnjapanese.data.model.getSampleTopics
import com.example.learnjapanese.data.model.getSampleWords
import com.example.learnjapanese.data.model.generateQuizQuestions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyQuizScreen(
    topicId: String,
    onBack: () -> Unit = {},
    onComplete: (Int, Int) -> Unit = { _, _ -> }
) {
    // Giả định lấy dữ liệu từ ViewModel thực tế
    val topic = remember { getSampleTopics().find { it.id == topicId } ?: getSampleTopics().first() }
    val allWords = remember { getSampleWords(topicId) }
    
    // Generate quiz questions from vocabulary words
    val quizQuestions = remember {
        generateQuizQuestions(allWords, 5) // Tạo 5 câu hỏi
    }
    
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableIntStateOf(-1) }
    var answerSubmitted by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    
    // Get current question
    val currentQuestion = if (currentQuestionIndex < quizQuestions.size) {
        quizQuestions[currentQuestionIndex]
    } else null
    
    // Check if answer is correct
    val isCurrentAnswerCorrect = selectedAnswerIndex == currentQuestion?.correctAnswerIndex
    
    // Functions to handle user interactions
    val selectAnswer = { index: Int ->
        if (!answerSubmitted) {
            selectedAnswerIndex = index
        }
    }
    
    val submitAnswer = {
        if (selectedAnswerIndex >= 0 && !answerSubmitted) {
            answerSubmitted = true
            if (isCurrentAnswerCorrect) {
                correctAnswers++
            }
        }
    }
    
    val moveToNextQuestion = {
        if (currentQuestionIndex < quizQuestions.size - 1) {
            currentQuestionIndex++
            selectedAnswerIndex = -1
            answerSubmitted = false
        } else {
            // Quiz completed
            onComplete(correctAnswers, quizQuestions.size)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kiểm tra: ${topic.name}",
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
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Quiz progress
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
                        text = "Câu hỏi ${currentQuestionIndex + 1} / ${quizQuestions.size}",
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
                    progress = { (currentQuestionIndex + 1).toFloat() / quizQuestions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Quiz content
            currentQuestion?.let { question ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Question
                    item {
                        when (question.type) {
                            QuizQuestionType.JAPANESE_TO_MEANING -> {
                                // Japanese to meaning question
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Từ này có nghĩa là gì?",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                        
                                        Spacer(modifier = Modifier.height(16.dp))
                                        
                                        Text(
                                            text = question.questionWord.word,
                                            style = MaterialTheme.typography.headlineLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                        
                                        Spacer(modifier = Modifier.height(8.dp))
                                        
                                        Text(
                                            text = question.questionWord.reading,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        
                                        Spacer(modifier = Modifier.height(16.dp))
                                        
                                        IconButton(
                                            onClick = { /* Play pronunciation */ },
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                                    shape = CircleShape
                                                )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.VolumeUp,
                                                contentDescription = "Phát âm",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            QuizQuestionType.MEANING_TO_JAPANESE -> {
                                // Meaning to Japanese question
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Chọn từ tiếng Nhật đúng với nghĩa",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                        
                                        Spacer(modifier = Modifier.height(16.dp))
                                        
                                        Text(
                                            text = "\"${question.questionWord.meaning}\"",
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Answer options
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            question.answers.forEachIndexed { index, answer ->
                                val isSelected = index == selectedAnswerIndex
                                val isCorrect = index == question.correctAnswerIndex
                                
                                val backgroundColor = when {
                                    !answerSubmitted -> if (isSelected) 
                                        MaterialTheme.colorScheme.primaryContainer 
                                    else 
                                        MaterialTheme.colorScheme.surface
                                    isCorrect -> MaterialTheme.colorScheme.primaryContainer
                                    isSelected -> MaterialTheme.colorScheme.errorContainer
                                    else -> MaterialTheme.colorScheme.surface
                                }
                                
                                val borderColor = when {
                                    !answerSubmitted && isSelected -> MaterialTheme.colorScheme.primary
                                    answerSubmitted && isCorrect -> MaterialTheme.colorScheme.primary
                                    answerSubmitted && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                }
                                
                                val textColor = when {
                                    !answerSubmitted -> if (isSelected) 
                                        MaterialTheme.colorScheme.onPrimaryContainer 
                                    else 
                                        MaterialTheme.colorScheme.onSurface
                                    isCorrect -> MaterialTheme.colorScheme.onPrimaryContainer
                                    isSelected -> MaterialTheme.colorScheme.onErrorContainer
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                                
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(enabled = !answerSubmitted) {
                                            selectAnswer(index)
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = backgroundColor
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = 1.dp,
                                        color = borderColor
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = if (isSelected) 2.dp else 0.dp
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        if (question.type == QuizQuestionType.JAPANESE_TO_MEANING) {
                                            // Display meaning
                                            Text(
                                                text = answer.meaning,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                ),
                                                color = textColor,
                                                modifier = Modifier.weight(1f)
                                            )
                                        } else {
                                            // Display Japanese word
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = answer.word,
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                    ),
                                                    color = textColor
                                                )
                                                
                                                Text(
                                                    text = answer.reading,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = textColor.copy(alpha = 0.7f)
                                                )
                                            }
                                        }
                                        
                                        // Show check or X mark if answer submitted
                                        if (answerSubmitted) {
                                            if (isCorrect) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Đúng",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            } else if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Sai",
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
                    
                    // Explanation (only shown after answer submitted)
                    item {
                        AnimatedVisibility(
                            visible = answerSubmitted,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isCurrentAnswerCorrect) 
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) 
                                    else 
                                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isCurrentAnswerCorrect) 
                                                Icons.Default.Check 
                                            else 
                                                Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (isCurrentAnswerCorrect) 
                                                MaterialTheme.colorScheme.primary 
                                            else 
                                                MaterialTheme.colorScheme.error
                                        )
                                        
                                        Spacer(modifier = Modifier.width(8.dp))
                                        
                                        Text(
                                            text = if (isCurrentAnswerCorrect) "Chính xác!" else "Chưa đúng!",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (isCurrentAnswerCorrect) 
                                                MaterialTheme.colorScheme.primary 
                                            else 
                                                MaterialTheme.colorScheme.error
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    // Show correct answer if wrong
                                    if (!isCurrentAnswerCorrect) {
                                        Text(
                                            text = "Đáp án đúng: ${
                                                if (question.type == QuizQuestionType.JAPANESE_TO_MEANING)
                                                    question.answers[question.correctAnswerIndex].meaning
                                                else
                                                    "${question.answers[question.correctAnswerIndex].word} (${question.answers[question.correctAnswerIndex].reading})"
                                            }",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    
                                    if (question.questionWord.exampleSentence != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        
                                        Text(
                                            text = "Ví dụ: ${question.questionWord.exampleSentence}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        
                                        Text(
                                            text = question.questionWord.exampleSentenceTranslation ?: "",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Spacing at the bottom to avoid overlap with buttons
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
            
            // Bottom buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                if (!answerSubmitted) {
                    Button(
                        onClick = submitAnswer,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedAnswerIndex >= 0,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    ) {
                        Text("Kiểm tra")
                    }
                } else {
                    Button(
                        onClick = moveToNextQuestion,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentQuestionIndex < quizQuestions.size - 1)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        if (currentQuestionIndex < quizQuestions.size - 1) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Câu tiếp theo")
                        } else {
                            Text("Hoàn thành")
                        }
                    }
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
            VocabularyQuizScreen(topicId = "1")
        }
    }
} 