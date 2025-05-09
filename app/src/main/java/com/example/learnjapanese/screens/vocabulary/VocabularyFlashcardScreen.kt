package com.example.learnjapanese.screens.vocabulary

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.data.model.VocabularyTopic
import com.example.learnjapanese.data.model.VocabularyWord
import com.example.learnjapanese.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyFlashcardScreen(
    topicId: String,
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {},
    viewModel: VocabularyFlashcardViewModel = hiltViewModel()
) {
    // Lấy state từ ViewModel
    val topicDetailResource by viewModel.topicDetail.collectAsState()
    val wordsResource by viewModel.words.collectAsState()
    val learnedWords by viewModel.learnedWords.collectAsState()
    val completionStatus by viewModel.completionStatus.collectAsState()
    
    // State cho flashcard
    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    
    // Animation for card flip
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "card rotation"
    )
    
    // Gửi kết quả khi hoàn thành và quay lại
    LaunchedEffect(completionStatus) {
        if (completionStatus is Resource.Success) {
            onComplete()
        }
    }
    
    // Function to move to next card
    val moveToNextCard = {
        when (val resource = wordsResource) {
            is Resource.Success -> {
                val words = resource.data
                if (words != null && currentCardIndex < words.size - 1) {
                    currentCardIndex++
                    isFlipped = false
                } else {
                    // End of deck - gửi kết quả lên server
                    viewModel.completeFlashcardSession()
                }
            }
            else -> { /* Không làm gì nếu dữ liệu đang tải hoặc lỗi */ }
        }
    }
    
    // Function to move to previous card
    val moveToPrevCard = {
        if (currentCardIndex > 0) {
            currentCardIndex--
            isFlipped = false
        }
    }
    
    // Function to mark current word as learned
    val markCurrentWordAsLearned = {
        when (val resource = wordsResource) {
            is Resource.Success -> {
                val words = resource.data
                if (words != null && currentCardIndex < words.size) {
                    val wordId = words[currentCardIndex].id
                    viewModel.markWordAsLearned(wordId)
                }
            }
            else -> { /* Không làm gì nếu dữ liệu đang tải hoặc lỗi */ }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (topicDetailResource) {
                        is Resource.Success -> {
                            val topic = (topicDetailResource as Resource.Success<VocabularyTopic>).data
                            Text(
                                text = topic?.name ?: "Flashcard",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        else -> {
                            Text(
                                "Flashcard",
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
            when (val resource = wordsResource) {
                is Resource.Loading -> {
                    // Hiển thị loading
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                is Resource.Success -> {
                    val words = resource.data
                    
                    if (words?.isEmpty() != false) {
                        // Hiển thị thông báo nếu không có từ vựng
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Không có từ vựng nào trong chủ đề này",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        // Progress indicator
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
                                    text = "Tiến độ:",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${currentCardIndex + 1}/${words?.size ?: 0}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            LinearProgressIndicator(
                                progress = { (currentCardIndex + 1).toFloat() / (words?.size ?: 1) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            )
                        }
                        
                        // Instruction
                        if (!isFlipped) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Chạm vào thẻ để xem nghĩa",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Flashcard
                        if (words != null && currentCardIndex < words.size) {
                            val currentWord = words[currentCardIndex]
                            val isCurrentWordLearned = learnedWords.contains(currentWord.id) || currentWord.isLearned
                            
                            // Flashcard
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                                    .padding(horizontal = 16.dp)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            rotationY = rotation
                                            cameraDistance = 8 * density
                                        }
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { isFlipped = !isFlipped },
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    if (rotation < 90f) {
                                        // Front of card (Japanese word)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(24.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = currentWord.word,
                                                    style = MaterialTheme.typography.displayMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center
                                                )
                                                
                                                Spacer(modifier = Modifier.height(16.dp))
                                                
                                                Text(
                                                    text = currentWord.reading,
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    textAlign = TextAlign.Center,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                                )
                                                
                                                Spacer(modifier = Modifier.height(24.dp))
                                                
                                                IconButton(
                                                    onClick = { /* Play pronunciation */ },
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                                        .size(48.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.VolumeUp,
                                                        contentDescription = "Nghe phát âm",
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        // Back of card (Meaning and example)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .graphicsLayer { rotationY = 180f } // Counter-rotate
                                                .padding(24.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = currentWord.meaning,
                                                    style = MaterialTheme.typography.headlineMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                
                                                if (!currentWord.exampleSentence.isNullOrEmpty()) {
                                                    Spacer(modifier = Modifier.height(24.dp))
                                                    
                                                    Divider(
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.7f)
                                                            .padding(vertical = 8.dp),
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                                                    )
                                                    
                                                    Spacer(modifier = Modifier.height(16.dp))
                                                    
                                                    Text(
                                                        text = currentWord.exampleSentence ?: "",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        textAlign = TextAlign.Center,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    
                                                    if (!currentWord.exampleSentenceTranslation.isNullOrEmpty()) {
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        
                                                        Text(
                                                            text = currentWord.exampleSentenceTranslation ?: "",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            textAlign = TextAlign.Center,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Navigation and action buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Previous button
                                Button(
                                    onClick = moveToPrevCard,
                                    enabled = currentCardIndex > 0,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowLeft,
                                        contentDescription = "Previous"
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Trước")
                                }
                                
                                // Mark as learned button
                                Button(
                                    onClick = markCurrentWordAsLearned,
                                    enabled = !isCurrentWordLearned,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isCurrentWordLearned) 
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                        else 
                                            MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Đã học"
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = if (isCurrentWordLearned) "Đã học" else "Đánh dấu đã học")
                                }
                                
                                // Next button
                                Button(
                                    onClick = moveToNextCard,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (words != null && currentCardIndex == words.size - 1)
                                            MaterialTheme.colorScheme.tertiary
                                        else
                                            MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    Text(if (words != null && currentCardIndex == words.size - 1) "Hoàn thành" else "Tiếp")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Next"
                                    )
                                }
                            }
                        }
                    }
                }
                
                is Resource.Error -> {
                    // Hiển thị lỗi
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Không thể tải dữ liệu từ vựng",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = resource.message ?: "Đã xảy ra lỗi không xác định",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.loadWords() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Thử lại")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VocabularyFlashcardScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Để preview hoạt động, không sử dụng hiltViewModel() vì không có Hilt trong preview
            VocabularyFlashcardScreen(topicId = "1")
        }
    }
} 