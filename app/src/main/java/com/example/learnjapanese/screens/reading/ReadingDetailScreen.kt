package com.example.learnjapanese.screens.reading

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReadingDetailScreen(
    readingId: String,
    viewModel: ReadingViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val readingDetail by viewModel.currentReading.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val scrollState = rememberScrollState()
    var selectedSentence by remember { mutableStateOf<Sentence?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // MediaPlayer cho phát âm
    var isSpeaking by remember { mutableStateOf(false) }
    val mediaPlayer = remember { MediaPlayer() }
    
    // Cấu hình MediaPlayer
    LaunchedEffect(Unit) {
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        
        // Đặt listener để cập nhật trạng thái khi hoàn thành phát
        mediaPlayer.setOnCompletionListener {
            isSpeaking = false
        }
        
        mediaPlayer.setOnPreparedListener {
            it.start()
            isSpeaking = true
        }
        
        mediaPlayer.setOnErrorListener { _, _, _ ->
            isSpeaking = false
            true // Đã xử lý lỗi
        }
    }
    
    // Xử lý khi màn hình bị hủy để giải phóng MediaPlayer
    DisposableEffect(Unit) {
        onDispose {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.release()
        }
    }
    
    // Load the reading when the screen is displayed
    LaunchedEffect(readingId) {
        viewModel.loadReadingDetail(readingId)
    }
    
    // Hiển thị thông báo lỗi nếu có
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(message = it)
            viewModel.clearError()
        }
    }
    
    // Hàm phát âm sử dụng Google Translate TTS
    val speakText = { text: String ->
        if (isSpeaking) {
            // Nếu đang phát thì dừng lại
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.reset()
            isSpeaking = false
        } else {
            try {
                // Mã hóa văn bản để đưa vào URL
                val encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString())
                
                // Tạo URL Google Translate TTS
                val ttsUrl = "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=ja&q=$encodedText"
                
                // Reset và chuẩn bị MediaPlayer với URL
                mediaPlayer.reset()
                mediaPlayer.setDataSource(context, Uri.parse(ttsUrl))
                
                // Chuẩn bị bất đồng bộ
                mediaPlayer.prepareAsync()
            } catch (e: Exception) {
                // Xử lý lỗi
                isSpeaking = false
            }
        }
    }
    
    // Handle tap outside the popup to dismiss
    val dismissPopup = {
        selectedSentence = null
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    readingDetail?.let {
                        Text(
                            it.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1
                        )
                    } ?: Text("Đang tải...")
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
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8F5E6))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { dismissPopup() }
                    )
                }
        ) {
            if (isLoading) {
                // Hiển thị loading khi đang tải dữ liệu
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (readingDetail == null) {
                // Hiển thị thông báo khi không có dữ liệu
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không thể tải bài đọc", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    // Header
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Level and info
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Level
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = readingDetail!!.level,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                                
                                // Word count
                                Text(
                                    text = "${readingDetail!!.wordCount} từ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Title
                            Text(
                                text = readingDetail!!.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Summary
                            Text(
                                text = readingDetail!!.summary,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontStyle = FontStyle.Italic
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                    
                    // Content with page-like style
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 32.dp)
                        ) {
                            // Tách nội dung thành các câu
                            readingDetail!!.sentences.forEachIndexed { index, sentence ->
                                ReadingSentence(
                                    sentence = sentence,
                                    onLongClick = {
                                        selectedSentence = sentence
                                    }
                                )
                                
                                // Add spacing between paragraphs
                                if (sentence.text.endsWith(".") || sentence.text.endsWith("。")) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                } else {
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                    
                    // Space at bottom for floating action button
                    Spacer(modifier = Modifier.height(80.dp))
                }
                
                // Sentence popup when a sentence is selected
                AnimatedVisibility(
                    visible = selectedSentence != null,
                    enter = fadeIn(animationSpec = tween(200)) + 
                            slideInVertically(animationSpec = tween(300), initialOffsetY = { it / 2 }),
                    exit = fadeOut(animationSpec = tween(200)) + 
                            slideOutVertically(animationSpec = tween(300), targetOffsetY = { it / 2 })
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { dismissPopup() }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        selectedSentence?.let { sentence ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .wrapContentSize()
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = { /* Do nothing to prevent closing */ }
                                        )
                                    },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 8.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp)
                                ) {
                                    // Close button
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.TopEnd
                                    ) {
                                        IconButton(
                                            onClick = dismissPopup,
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Đóng",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    // Original Japanese text
                                    Text(
                                        text = sentence.text,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Vietnamese translation
                                    Text(
                                        text = sentence.translation,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontStyle = FontStyle.Italic
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    Spacer(modifier = Modifier.height(24.dp))
                                    
                                    // Pronunciation button
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        IconButton(
                                            onClick = { speakText(sentence.text) },
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(32.dp))
                                                .background(MaterialTheme.colorScheme.primary)
                                                .size(48.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isSpeaking) Icons.Default.VolumeUp else Icons.Default.PlayArrow,
                                                contentDescription = if (isSpeaking) "Dừng phát âm" else "Phát âm",
                                                tint = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReadingSentence(
    sentence: Sentence,
    onLongClick: () -> Unit
) {
    Text(
        text = sentence.text,
        style = MaterialTheme.typography.bodyLarge.copy(
            lineHeight = 28.sp,
            letterSpacing = 0.3.sp
        ),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { },
                onLongClick = onLongClick
            )
    )
}

@Preview(showBackground = true)
@Composable
fun ReadingDetailScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ReadingDetailScreen(readingId = "1")
        }
    }
} 