package com.example.learnjapanese.screens.vocabulary

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.data.model.VocabularyTopic
import com.example.learnjapanese.data.model.VocabularyWord
import com.example.learnjapanese.data.model.getSampleTopics
import com.example.learnjapanese.data.model.getSampleWords

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyFlashcardScreen(
    topicId: String,
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {}
) {
    // Giả định lấy dữ liệu từ ViewModel thực tế
    val topic = remember { getSampleTopics().find { it.id == topicId } ?: getSampleTopics().first() }
    val words = remember { getSampleWords(topicId) }
    
    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var markedAsKnown by remember { mutableStateOf(false) }
    
    // Animation for card flip
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "card rotation"
    )
    
    // Function to move to next card
    val moveToNextCard = {
        if (currentCardIndex < words.size - 1) {
            currentCardIndex++
            isFlipped = false
            markedAsKnown = false
        } else {
            // End of deck
            onComplete()
        }
    }
    
    // Function to move to previous card
    val moveToPrevCard = {
        if (currentCardIndex > 0) {
            currentCardIndex--
            isFlipped = false
            markedAsKnown = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topic.name,
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
                        text = "${currentCardIndex + 1}/${words.size}",
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
            if (words.isNotEmpty() && currentCardIndex < words.size) {
                val word = words[currentCardIndex]
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    // Flashcard
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 8 * density
                            },
                        onClick = { isFlipped = !isFlipped },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        // Front of card (Japanese word)
                        if (!isFlipped) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = word.word,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = word.reading,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
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
                        
                        // Back of card (Meaning and example)
                        if (isFlipped) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer { rotationY = 180f } // Counter-rotate content
                                    .padding(24.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Nghĩa",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = word.meaning,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Divider(
                                        modifier = Modifier
                                            .width(80.dp),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    if (word.exampleSentence != null) {
                                        Text(
                                            text = "Ví dụ",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        
                                        Spacer(modifier = Modifier.height(8.dp))
                                        
                                        Text(
                                            text = word.exampleSentence,
                                            style = MaterialTheme.typography.bodyLarge,
                                            textAlign = TextAlign.Center
                                        )
                                        
                                        Spacer(modifier = Modifier.height(4.dp))
                                        
                                        Text(
                                            text = word.exampleSentenceTranslation ?: "",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Navigation buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous button
                    IconButton(
                        onClick = { if (currentCardIndex > 0) moveToPrevCard() },
                        enabled = currentCardIndex > 0,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Thẻ trước",
                            tint = if (currentCardIndex > 0) 
                                MaterialTheme.colorScheme.onSurfaceVariant 
                              else 
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    // Answer buttons (only show when card is flipped)
                    if (isFlipped) {
                        // "Don't know" button
                        Button(
                            onClick = moveToNextCard,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Chưa nhớ")
                        }
                        
                        // "Know it" button
                        Button(
                            onClick = {
                                markedAsKnown = true
                                moveToNextCard()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Đã nhớ")
                        }
                    } else {
                        // When not flipped, just show next button
                        Button(
                            onClick = { isFlipped = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Lật thẻ")
                        }
                    }
                    
                    // Next button
                    IconButton(
                        onClick = { if (currentCardIndex < words.size - 1) moveToNextCard() },
                        enabled = currentCardIndex < words.size - 1,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Thẻ sau",
                            tint = if (currentCardIndex < words.size - 1) 
                                MaterialTheme.colorScheme.onSurfaceVariant 
                              else 
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                
                if (currentCardIndex == words.size - 1 && isFlipped) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = onComplete,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Hoàn thành")
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
            VocabularyFlashcardScreen(topicId = "1")
        }
    }
} 