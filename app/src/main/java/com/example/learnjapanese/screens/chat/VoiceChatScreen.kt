package com.example.learnjapanese.screens.chat

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceChatScreen(
    onBack: () -> Unit = {}
) {
    // Các trạng thái
    var isListening by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }
    var voiceMessages by remember { mutableStateOf(listOf<VoiceMessage>()) }
    var speechRate by remember { mutableFloatStateOf(1.0f) }
    val listState = rememberLazyListState()
    
    // Trạng thái cho hiệu ứng phát âm thanh
    var pendingUserMessage by remember { mutableStateOf<String?>(null) }

    // LaunchedEffect để xử lý tin nhắn mới
    pendingUserMessage?.let { message ->
        LaunchedEffect(message) {
            delay(2000)
            
            val botResponse = "こんにちは！日本語の練習をしましょう。どんなことを練習したいですか？"
            val botTranslation = "Xin chào! Hãy cùng luyện tập tiếng Nhật. Bạn muốn luyện tập điều gì?"
            
            // Thêm phản hồi của bot
            voiceMessages = voiceMessages + VoiceMessage(
                id = "a${voiceMessages.size + 1}",
                content = botResponse,
                translation = botTranslation,
                isFromUser = false,
                timestamp = System.currentTimeMillis()
            )
            
            // Giả lập AI đọc xong
            delay(3000)
            isSpeaking = false
            pendingUserMessage = null
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trò chuyện bằng giọng nói",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Trở về"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Thiết lập */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Thiết lập"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        floatingActionButton = {
            MicButton(
                isListening = isListening,
                onClick = {
                    isListening = !isListening
                    
                    if (isListening) {
                        // Giả lập người dùng đang nói
                        // Trong triển khai thực tế, đây sẽ là nơi bắt đầu ghi âm
                    } else {
                        // Giả lập đã nhận diện xong
                        val userMessageText = "Xin chào, tôi muốn luyện tập tiếng Nhật"
                        
                        // Thêm tin nhắn người dùng
                        voiceMessages = voiceMessages + VoiceMessage(
                            id = "u${voiceMessages.size + 1}",
                            content = userMessageText,
                            isFromUser = true,
                            timestamp = System.currentTimeMillis()
                        )
                        
                        // Giả lập phản hồi từ AI
                        isSpeaking = true
                        
                        // Kích hoạt LaunchedEffect qua biến trạng thái
                        pendingUserMessage = userMessageText
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Nội dung chính
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Kiểm soát tốc độ giọng nói
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "Tốc độ giọng nói",
                                style = MaterialTheme.typography.titleMedium
                            )
                            
                            Spacer(modifier = Modifier.weight(1f))
                            
                            Text(
                                text = "${speechRate}x",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Slider(
                            value = speechRate,
                            onValueChange = { speechRate = it },
                            valueRange = 0.5f..2.0f,
                            steps = 5,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            )
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("0.5x", style = MaterialTheme.typography.bodySmall)
                            Text("1.0x", style = MaterialTheme.typography.bodySmall)
                            Text("1.5x", style = MaterialTheme.typography.bodySmall)
                            Text("2.0x", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                
                // Trạng thái hiện tại
                if (voiceMessages.isEmpty()) {
                    // Hướng dẫn khi chưa có tin nhắn
                    VoiceChatIntro()
                } else {
                    // Danh sách tin nhắn giọng nói
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(voiceMessages) { message ->
                            VoiceMessageItem(message = message)
                        }
                        
                        // Khoảng cách dưới cùng
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                    
                    // Cuộn đến tin nhắn cuối cùng
                    LaunchedEffect(voiceMessages.size) {
                        if (voiceMessages.isNotEmpty()) {
                            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
                        }
                    }
                }
                
                // Hiển thị trạng thái nếu đang nghe hoặc đang nói
                if (isListening || isSpeaking) {
                    VoiceStatusCard(
                        isListening = isListening,
                        isSpeaking = isSpeaking
                    )
                }
            }
        }
    }
}

@Composable
fun VoiceChatIntro() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Tiêu đề
        Text(
            text = "Trò chuyện với AI bằng giọng nói",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Mô tả
        Text(
            text = "Nhấn vào nút microphone để bắt đầu nói. AI sẽ phản hồi lại bằng giọng nói và hỗ trợ bạn luyện tập tiếng Nhật.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Các tính năng
        FeatureItem(
            icon = Icons.Default.Translate,
            title = "Hỗ trợ song ngữ",
            description = "Hiển thị cả tiếng Nhật và bản dịch tiếng Việt"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        FeatureItem(
            icon = Icons.Default.GraphicEq,
            title = "Phát âm chính xác",
            description = "Giúp bạn luyện tập phát âm với giọng bản xứ"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        FeatureItem(
            icon = Icons.Default.Hearing,
            title = "Luyện nghe hiểu",
            description = "Cải thiện kỹ năng nghe hiểu tiếng Nhật"
        )
    }
}

@Composable
fun FeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun VoiceMessageItem(message: VoiceMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
    ) {
        // Avatar và người gửi
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!message.isFromUser) {
                // Avatar AI
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AI",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "AI Trợ lý",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(
                    text = "Bạn",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Avatar người dùng
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "B",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
        
        // Nội dung tin nhắn
        Card(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .align(if (message.isFromUser) Alignment.End else Alignment.Start),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser)
                    MaterialTheme.colorScheme.secondary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (message.isFromUser)
                        MaterialTheme.colorScheme.onSecondary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Hiển thị bản dịch nếu có
                if (!message.isFromUser && message.translation != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Divider(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = message.translation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
        
        // Thời gian
        Text(
            text = formatVoiceMessageTime(message.timestamp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            modifier = Modifier.align(if (message.isFromUser) Alignment.End else Alignment.Start)
        )
    }
}

@Composable
fun VoiceStatusCard(
    isListening: Boolean,
    isSpeaking: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isListening)
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isListening) {
                // Đang nghe
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = "Đang nghe...",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Visualizer
                AudioVisualizer(
                    modifier = Modifier
                        .width(80.dp)
                        .height(32.dp)
                )
            } else if (isSpeaking) {
                // Đang nói
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = "AI đang nói...",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Loading
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun MicButton(
    isListening: Boolean,
    onClick: () -> Unit
) {
    val animatedSize by animateFloatAsState(
        targetValue = if (isListening) 1.2f else 1f,
        animationSpec = tween(300),
        label = "size"
    )
    
    // Tạo hiệu ứng nhấp nháy bên ngoài khối drawBehind
    val infiniteTransition = rememberInfiniteTransition("pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    // Lấy màu sắc trước khi vào drawBehind
    val errorColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
    val borderColor = if (isListening) MaterialTheme.colorScheme.error.copy(alpha = 0.2f) else Color.Transparent
    val bgColor = if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
    val iconTint = MaterialTheme.colorScheme.onSecondary
    
    Box(
        modifier = Modifier
            .size(72.dp * animatedSize)
            .clip(CircleShape)
            .background(bgColor)
            .border(
                width = if (isListening) 2.dp else 0.dp,
                color = borderColor,
                shape = CircleShape
            )
            .drawBehind {
                if (isListening) {
                    // Sử dụng màu đã lấy từ bên ngoài
                    drawCircle(
                        color = errorColor,
                        radius = size.minDimension / 2 * scale,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(72.dp * animatedSize)
        ) {
            Icon(
                imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                contentDescription = if (isListening) "Kết thúc" else "Bắt đầu nói",
                tint = iconTint,
                modifier = Modifier.size(32.dp * animatedSize)
            )
        }
    }
}

@Composable
fun AudioVisualizer(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition("visualizer")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )
    
    // Lấy màu trước khi vào Canvas
    val lineColor = MaterialTheme.colorScheme.secondary
    
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val lineCount = 5
        val lineWidth = 4.dp.toPx()
        val gapWidth = (canvasWidth - lineCount * lineWidth) / (lineCount - 1)
        
        for (i in 0 until lineCount) {
            val x = i * (lineWidth + gapWidth)
            val randomOffset = Random.nextFloat() * 0.5f + 0.5f
            val amplitude = (sin(time * 10 + i) + 1) / 2 * randomOffset
            val height = canvasHeight * amplitude
            
            drawLine(
                start = Offset(x + lineWidth / 2, canvasHeight - height),
                end = Offset(x + lineWidth / 2, canvasHeight),
                color = lineColor, // Sử dụng biến đã lấy từ ngoài
                strokeWidth = lineWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

fun formatVoiceMessageTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60 * 1000 -> "Vừa xong"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} phút trước"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} giờ trước"
        else -> {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            format.format(Date(timestamp))
        }
    }
}

data class VoiceMessage(
    val id: String,
    val content: String,
    val translation: String? = null,
    val isFromUser: Boolean,
    val timestamp: Long
)

@Preview(showBackground = true)
@Composable
fun VoiceChatScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            VoiceChatScreen()
        }
    }
} 