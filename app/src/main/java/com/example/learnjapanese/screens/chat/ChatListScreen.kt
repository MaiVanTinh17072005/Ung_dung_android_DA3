package com.example.learnjapanese.screens.chat

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.learnjapanese.components.AppBottomNavigation
import com.example.learnjapanese.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onBack: () -> Unit = {},
    onStartTextChat: () -> Unit = {},
    onStartVoiceChat: () -> Unit = {},
    onHistoryChatClick: (String) -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Trò chuyện mới", "Lịch sử")
    
    // Giả lập dữ liệu lịch sử trò chuyện
    val chatHistory = remember {
        listOf(
            ChatHistoryItem(
                id = "c1",
                title = "Học ngữ pháp N5",
                lastMessage = "Các mẫu câu thông dụng với て-form",
                timestamp = System.currentTimeMillis() - 3600000, // 1 giờ trước
                isVoiceChat = false
            ),
            ChatHistoryItem(
                id = "c2",
                title = "Luyện phát âm",
                lastMessage = "Cách phát âm đúng âm R và L",
                timestamp = System.currentTimeMillis() - 86400000, // 1 ngày trước
                isVoiceChat = true
            ),
            ChatHistoryItem(
                id = "c3",
                title = "Từ vựng mua sắm",
                lastMessage = "Các từ vựng cần thiết khi đi mua sắm",
                timestamp = System.currentTimeMillis() - 259200000, // 3 ngày trước
                isVoiceChat = false
            ),
            ChatHistoryItem(
                id = "c4",
                title = "Luyện nói theo tình huống",
                lastMessage = "Tình huống đặt phòng khách sạn",
                timestamp = System.currentTimeMillis() - 604800000, // 1 tuần trước
                isVoiceChat = true
            )
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Trò chuyện với AI",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Trở về trang chủ"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Mở tìm kiếm */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Tìm kiếm"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        bottomBar = {
            AppBottomNavigation(
                currentRoute = Screen.Chat.route,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { 
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                )
                            ) 
                        },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            if (index == 0) {
                                Icon(Icons.Default.Chat, contentDescription = null)
                            } else {
                                Icon(Icons.Default.History, contentDescription = null)
                            }
                        }
                    )
                }
            }
            
            // Nội dung theo tab
            when (selectedTabIndex) {
                0 -> NewChatOptions(
                    onStartTextChat = onStartTextChat,
                    onStartVoiceChat = onStartVoiceChat
                )
                1 -> ChatHistoryList(
                    chatHistory = chatHistory,
                    onChatClick = onHistoryChatClick
                )
            }
        }
    }
}

@Composable
fun NewChatOptions(
    onStartTextChat: () -> Unit = {},
    onStartVoiceChat: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Biểu tượng AI Assistant
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChatBubble,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Chọn phương thức trò chuyện",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Bạn có thể trò chuyện bằng tin nhắn văn bản hoặc trò chuyện bằng giọng nói",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Thẻ trò chuyện bằng văn bản
        ChatOptionCard(
            title = "Trò chuyện bằng văn bản",
            description = "Gửi tin nhắn văn bản và nhận phản hồi từ AI hỗ trợ học tiếng Nhật",
            icon = Icons.Default.Chat,
            backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            iconTint = MaterialTheme.colorScheme.primary,
            onClick = onStartTextChat
        )
        
        // Thẻ trò chuyện bằng giọng nói
        ChatOptionCard(
            title = "Trò chuyện bằng giọng nói",
            description = "Trò chuyện trực tiếp bằng giọng nói với AI để luyện phát âm và giao tiếp",
            icon = Icons.Default.Mic,
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
            iconTint = MaterialTheme.colorScheme.secondary,
            onClick = onStartVoiceChat
        )
    }
}

@Composable
fun ChatOptionCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ChatHistoryList(
    chatHistory: List<ChatHistoryItem>,
    onChatClick: (String) -> Unit = {}
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(chatHistory) { chat ->
            ChatHistoryItemCard(
                chat = chat,
                onClick = { onChatClick(chat.id) }
            )
        }
        
        // Khoảng cách dưới cùng
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ChatHistoryItemCard(
    chat: ChatHistoryItem,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon cho loại trò chuyện
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (chat.isVoiceChat) 
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                        else 
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (chat.isVoiceChat) Icons.Default.Mic else Icons.Default.Chat,
                    contentDescription = if (chat.isVoiceChat) "Trò chuyện bằng giọng nói" else "Trò chuyện bằng văn bản",
                    tint = if (chat.isVoiceChat) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Thông tin cuộc trò chuyện
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chat.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = formatChatHistoryDate(chat.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Nút xóa
            IconButton(
                onClick = { /* TODO: Xóa lịch sử trò chuyện */ },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa lịch sử trò chuyện",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Hàm format thời gian trò chuyện (đổi tên để tránh xung đột)
fun formatChatHistoryDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60 * 60 * 1000 -> "Vừa xong"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} giờ trước"
        diff < 48 * 60 * 60 * 1000 -> "Hôm qua"
        diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)} ngày trước"
        else -> {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            format.format(Date(timestamp))
        }
    }
}

// Lớp dữ liệu cho lịch sử trò chuyện
data class ChatHistoryItem(
    val id: String,
    val title: String,
    val lastMessage: String,
    val timestamp: Long,
    val isVoiceChat: Boolean
)

@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChatListScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewChatOptionsPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NewChatOptions()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatHistoryListPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val chatHistory = listOf(
                ChatHistoryItem(
                    id = "c1",
                    title = "Học ngữ pháp N5",
                    lastMessage = "Các mẫu câu thông dụng với て-form",
                    timestamp = System.currentTimeMillis() - 3600000,
                    isVoiceChat = false
                ),
                ChatHistoryItem(
                    id = "c2",
                    title = "Luyện phát âm",
                    lastMessage = "Cách phát âm đúng âm R và L",
                    timestamp = System.currentTimeMillis() - 86400000,
                    isVoiceChat = true
                )
            )
            ChatHistoryList(chatHistory = chatHistory)
        }
    }
} 