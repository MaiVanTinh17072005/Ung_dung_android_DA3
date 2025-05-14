package com.example.learnjapanese.screens.call

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnjapanese.data.model.CallSession
import com.example.learnjapanese.data.model.CallState
import com.example.learnjapanese.data.model.Contact
import com.example.learnjapanese.data.model.getContactById
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import kotlinx.coroutines.delay

@Composable
fun CallScreen(
    contactId: String,
    isVideoCall: Boolean = false,
    onEndCall: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val contact = remember { getContactById(contactId) ?: Contact(name = "Unknown") }
    
    var callState by remember { mutableStateOf(CallState.RINGING) }
    var callDuration by remember { mutableLongStateOf(0L) }
    var isCallConnected by remember { mutableStateOf(false) }
    
    var isMicEnabled by remember { mutableStateOf(true) }
    var isSpeakerEnabled by remember { mutableStateOf(false) }
    var isVideoEnabled by remember { mutableStateOf(isVideoCall) }
    var isFrontCamera by remember { mutableStateOf(true) }

    // Khi cuộc gọi kết nối, bắt đầu tính thời gian
    LaunchedEffect(callState) {
        if (callState == CallState.RINGING) {
            delay(3000) // Giả lập thời gian đổ chuông
            callState = CallState.CONNECTING
        } else if (callState == CallState.CONNECTING) {
            delay(2000) // Giả lập thời gian kết nối
            callState = CallState.CONNECTED
            isCallConnected = true
        }
    }
    
    // Đếm thời gian cuộc gọi
    LaunchedEffect(isCallConnected) {
        if (isCallConnected) {
            while (true) {
                delay(1000)
                callDuration += 1
            }
        }
    }
    
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Nút trở lại
            IconButton(
                onClick = { 
                    if (callState == CallState.CONNECTED) {
                        callState = CallState.ENDED
                    }
                    onNavigateBack() 
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Trở lại",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Hiển thị video hoặc hình đại diện
            if (isVideoCall && isVideoEnabled) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    // Giả lập màn hình video
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(64.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Video call with ${contact.name}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                    
                    // Hiển thị preview camera của mình
                    Card(
                        modifier = Modifier
                            .size(width = 120.dp, height = 180.dp)
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            } else {
                // Giao diện cuộc gọi âm thanh
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Avatar lớn
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = contact.name.first().toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // Hiệu ứng pulse khi đang đổ chuông
                    if (callState == CallState.RINGING || callState == CallState.CONNECTING) {
                        RingingEffect(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(16.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Tên người gọi
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Trạng thái cuộc gọi
                    Text(
                        text = when (callState) {
                            CallState.RINGING -> "Đang gọi..."
                            CallState.CONNECTING -> "Đang kết nối..."
                            CallState.CONNECTED -> formatDuration(callDuration)
                            CallState.ENDED -> "Cuộc gọi đã kết thúc"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    
                    if (callState == CallState.CONNECTED && !isVideoCall) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(1000)),
                            exit = fadeOut(animationSpec = tween(1000))
                        ) {
                            Text(
                                text = if (isMicEnabled) "Microphone đang bật" else "Microphone đã tắt",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isMicEnabled) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            // Điều khiển cuộc gọi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Nút mic
                        CallControlButton(
                            onClick = { isMicEnabled = !isMicEnabled },
                            icon = if (isMicEnabled) Icons.Default.Mic else Icons.Default.MicOff,
                            contentDescription = if (isMicEnabled) "Tắt micro" else "Bật micro",
                            tint = if (isMicEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            backgroundColor = if (isMicEnabled) 
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) 
                            else 
                                MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                        )
                        
                        // Nút loa ngoài
                        CallControlButton(
                            onClick = { isSpeakerEnabled = !isSpeakerEnabled },
                            icon = if (isSpeakerEnabled) Icons.Default.Speaker else Icons.Default.VolumeOff,
                            contentDescription = if (isSpeakerEnabled) "Tắt loa ngoài" else "Bật loa ngoài",
                            tint = if (isSpeakerEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            backgroundColor = if (isSpeakerEnabled) 
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.05f)
                        )
                        
                        // Nút kết thúc cuộc gọi
                        CallControlButton(
                            onClick = { 
                                callState = CallState.ENDED
                                onEndCall()
                            },
                            icon = Icons.Default.CallEnd,
                            contentDescription = "Kết thúc cuộc gọi",
                            size = 64.dp,
                            tint = Color.White,
                            backgroundColor = Color.Red
                        )
                        
                        // Nút camera (chỉ cho video call)
                        if (isVideoCall) {
                            CallControlButton(
                                onClick = { isVideoEnabled = !isVideoEnabled },
                                icon = if (isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                                contentDescription = if (isVideoEnabled) "Tắt camera" else "Bật camera",
                                tint = if (isVideoEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                backgroundColor = if (isVideoEnabled) 
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.05f)
                            )
                        } else {
                            // Nút chuyển sang video
                            CallControlButton(
                                onClick = { isVideoEnabled = true },
                                icon = Icons.Default.Videocam,
                                contentDescription = "Chuyển sang gọi video",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.05f)
                            )
                        }
                        
                        // Nút chuyển camera (chỉ cho video call khi đã bật camera)
                        if (isVideoCall && isVideoEnabled) {
                            CallControlButton(
                                onClick = { isFrontCamera = !isFrontCamera },
                                icon = Icons.Default.CameraAlt,
                                contentDescription = "Chuyển camera",
                                tint = MaterialTheme.colorScheme.primary,
                                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CallControlButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String?,
    size: androidx.compose.ui.unit.Dp = 48.dp,
    tint: Color,
    backgroundColor: Color
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(size / 2)
        )
    }
}

@Composable
fun RingingEffect(
    modifier: Modifier = Modifier
) {
    // Tạo 3 vòng pulse với độ trễ khác nhau
    Box(modifier = modifier) {
        val infiniteTransition = rememberInfiniteTransition(label = "ring")
        
        val rings = listOf(0, 1, 2)
        rings.forEach { index ->
            val delay = index * 1000
            
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 3000,
                        delayMillis = delay,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "scale"
            )
            
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 3000,
                        delayMillis = delay,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "alpha"
            )
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
                    .scale(scale)
                    .alpha(alpha)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            )
        }
    }
}

// Hàm định dạng thời gian cuộc gọi
fun formatDuration(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    
    return if (h > 0) {
        String.format("%d:%02d:%02d", h, m, s)
    } else {
        String.format("%02d:%02d", m, s)
    }
}

@Preview(showBackground = true)
@Composable
fun CallScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val contact = Contact(name = "Tanaka Yuki", username = "tanaka_sensei", isOnline = true)
            val mockContactId = "c1"
            CallScreen(contactId = mockContactId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoCallScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val mockContactId = "c1"
            CallScreen(contactId = mockContactId, isVideoCall = true)
        }
    }
} 