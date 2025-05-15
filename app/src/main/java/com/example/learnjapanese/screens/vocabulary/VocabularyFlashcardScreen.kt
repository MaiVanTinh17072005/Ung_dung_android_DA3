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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.data.model.VocabularyTopic
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
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
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
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Không có từ vựng nào để học",
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
                        // Hiển thị thông tin tiến độ
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Thẻ ${currentCardIndex + 1} / ${words.size}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Đã học: ${learnedWords.size}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            LinearProgressIndicator(
                                progress = { (currentCardIndex + 1).toFloat() / words.size },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Flashcard
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Card front (Japanese)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .graphicsLayer {
                                        rotationY = rotation
                                        cameraDistance = 8 * density
                                    }
                                    .clickable { isFlipped = !isFlipped },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (!isFlipped) 
                                        MaterialTheme.colorScheme.primaryContainer 
                                    else 
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                if (!isFlipped) {
                                    // Front side - Japanese
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = words[currentCardIndex].word,
                                                style = MaterialTheme.typography.headlineLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                textAlign = TextAlign.Center
                                            )
                                            
                                            Spacer(modifier = Modifier.height(16.dp))
                                            
                                            Text(
                                                text = words[currentCardIndex].reading,
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                                textAlign = TextAlign.Center
                                            )
                                            
                                            Spacer(modifier = Modifier.height(24.dp))
                                            
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Nhấn để xem nghĩa",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // Back side - Meaning
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .graphicsLayer { rotationY = 180f },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Text(
                                                text = "Nghĩa",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center
                                            )
                                            
                                            Spacer(modifier = Modifier.height(16.dp))
                                            
                                            Text(
                                                text = words[currentCardIndex].meaning,
                                                style = MaterialTheme.typography.headlineSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center
                                            )
                                            
                                            if (!words[currentCardIndex].exampleSentence.isNullOrEmpty()) {
                                                Spacer(modifier = Modifier.height(24.dp))
                                                
                                                Divider(
                                                    modifier = Modifier
                                                        .width(64.dp)
                                                        .padding(vertical = 8.dp),
                                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                                    thickness = 2.dp
                                                )
                                                
                                                Spacer(modifier = Modifier.height(8.dp))
                                                
                                                Text(
                                                    text = "Ví dụ",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    textAlign = TextAlign.Center
                                                )
                                                
                                                Spacer(modifier = Modifier.height(8.dp))
                                                
                                                // Hiển thị ví dụ
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Text(
                                                        text = words[currentCardIndex].exampleSentence ?: "",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        textAlign = TextAlign.Center
                                                    )
                                                    
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    
                                                    if (!words[currentCardIndex].exampleSentenceTranslation.isNullOrEmpty()) {
                                                        Text(
                                                            text = words[currentCardIndex].exampleSentenceTranslation ?: "",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            }
                                            
                                            Spacer(modifier = Modifier.height(24.dp))
                                            
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Nhấn để xem từ",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Controls
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Previous button - Disabled when on first card
                            IconButton(
                                onClick = moveToPrevCard,
                                enabled = currentCardIndex > 0,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(48.dp)
                                    .background(
                                        color = if (currentCardIndex > 0)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Thẻ trước",
                                    tint = if (currentCardIndex > 0)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                            
                            // Mark as learned button
                            Button(
                                onClick = markCurrentWordAsLearned,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (learnedWords.contains(words[currentCardIndex].id))
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                    else
                                        MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (learnedWords.contains(words[currentCardIndex].id))
                                        "Đã học"
                                    else
                                        "Đánh dấu đã học"
                                )
                            }
                            
                            // Next or Complete button
                            IconButton(
                                onClick = moveToNextCard,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(48.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = if (currentCardIndex < words.size - 1)
                                        "Thẻ tiếp theo"
                                    else
                                        "Hoàn thành",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
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