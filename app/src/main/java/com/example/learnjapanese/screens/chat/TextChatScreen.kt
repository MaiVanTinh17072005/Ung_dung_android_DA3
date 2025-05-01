package com.example.learnjapanese.screens.chat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextChatScreen(
    chatId: String? = null,
    onBack: () -> Unit = {}
) {
    // Giả lập tin nhắn
    var messageList by remember { mutableStateOf(getSampleMessages(chatId)) }
    var inputMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    
    // Thêm biến state để theo dõi tin nhắn mới cần phản hồi
    var latestUserMessage by remember { mutableStateOf<ChatMessage?>(null) }
    
    // Xử lý phản hồi AI khi có tin nhắn mới
    latestUserMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            delay((1000..2000).random().toLong())
            
            // Tạo phản hồi giả lập
            val botResponse = when {
                userMessage.content.contains("xin chào", ignoreCase = true) -> 
                    "Xin chào! Tôi là trợ lý AI giúp bạn học tiếng Nhật. Bạn cần tôi giúp gì hôm nay?"
                userMessage.content.contains("từ vựng", ignoreCase = true) -> 
                    "Bạn muốn học từ vựng về chủ đề gì? Tôi có thể giúp bạn với các chủ đề như: gia đình, thực phẩm, du lịch, công việc, v.v."
                userMessage.content.contains("ngữ pháp", ignoreCase = true) -> 
                    "Tiếng Nhật có nhiều điểm ngữ pháp thú vị. Bạn đang học ở trình độ nào? N5, N4, hay cao hơn?"
                userMessage.content.contains("こんにちは", ignoreCase = true) -> 
                    "こんにちは！お元気ですか？(Xin chào! Bạn khỏe không?)"
                userMessage.content.contains("ありがとう", ignoreCase = true) -> 
                    "どういたしまして！(Không có gì!)"
                else -> 
                    "Tôi hiểu rồi. Bạn có thể hỏi tôi về từ vựng, ngữ pháp, cách phát âm hoặc bất kỳ điều gì liên quan đến tiếng Nhật. Tôi sẽ cố gắng giúp đỡ bạn!"
            }
            
            // Thêm phản hồi của bot
            val aiMessage = ChatMessage(
                id = "a${messageList.size + 1}",
                content = botResponse,
                isFromUser = false,
                timestamp = System.currentTimeMillis()
            )
            messageList = messageList + aiMessage
            isLoading = false
            
            // Reset latestUserMessage để không phản hồi lại tin nhắn cũ
            latestUserMessage = null
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (chatId != null) "Tiếp tục trò chuyện" else "Trò chuyện mới",
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
                    IconButton(onClick = { /* TODO: Hiển thị thông tin */ }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Thông tin"
                        )
                    }
                    
                    IconButton(onClick = { /* TODO: Hiển thị menu thêm */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Thêm tùy chọn"
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
            // Danh sách tin nhắn
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Hiển thị thông báo chào mừng nếu là cuộc trò chuyện mới
                if (messageList.isEmpty() && !isLoading) {
                    item {
                        WelcomeMessage()
                    }
                }
                
                // Hiển thị danh sách tin nhắn
                items(messageList) { message ->
                    MessageItem(message = message)
                }
                
                // Hiển thị đang nhập nếu đang chờ phản hồi
                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Avatar AI
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "AI",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            // Hiệu ứng đang nhập
                            Card(
                                shape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Đang nhập",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    
                                    Spacer(modifier = Modifier.width(8.dp))
                                    
                                    // Thay thế LoadingDots() bằng CircularProgressIndicator
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Khoảng cách dưới cùng
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            
            // Cuộn đến tin nhắn cuối cùng
            LaunchedEffect(messageList.size, isLoading) {
                if (messageList.isNotEmpty() || isLoading) {
                    listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
                }
            }
            
            // Trường nhập tin nhắn
            MessageInputField(
                value = inputMessage,
                onValueChange = { inputMessage = it },
                onSend = {
                    if (inputMessage.isNotBlank()) {
                        // Thêm tin nhắn người dùng
                        val userMessage = ChatMessage(
                            id = "u${messageList.size + 1}",
                            content = inputMessage,
                            isFromUser = true,
                            timestamp = System.currentTimeMillis()
                        )
                        messageList = messageList + userMessage
                        
                        // Xóa input và hiển thị loading
                        inputMessage = ""
                        isLoading = true
                        
                        // Đặt tin nhắn mới để LaunchedEffect xử lý
                        latestUserMessage = userMessage
                    }
                }
            )
        }
    }
}

@Composable
fun WelcomeMessage() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Xin chào! Tôi là trợ lý AI học tiếng Nhật",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Bạn có thể hỏi tôi về từ vựng, ngữ pháp, cách phát âm, hoặc trò chuyện bằng tiếng Nhật để luyện tập.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Một số gợi ý:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            SuggestionChip("Dạy tôi từ vựng về gia đình")
            SuggestionChip("Giải thích ngữ pháp て-form")
            SuggestionChip("Cách phát âm âm \"つ\" và \"づ\"")
            SuggestionChip("Xin chào bằng tiếng Nhật là gì?")
        }
    }
}

@Composable
fun SuggestionChip(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            // Avatar AI
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .align(Alignment.Top),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AI",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        // Nội dung tin nhắn
        Column(
            horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = if (message.isFromUser) 16.dp else 0.dp,
                    topEnd = if (message.isFromUser) 0.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.isFromUser) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (message.isFromUser) 
                        MaterialTheme.colorScheme.onPrimary 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(2.dp))
            
            // Thời gian
            Text(
                text = formatMessageTime(message.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
        
        if (message.isFromUser) {
            Spacer(modifier = Modifier.width(8.dp))
            
            // Avatar người dùng (hiển thị chữ đầu tiên)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.Top),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "U",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        // Avatar AI
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AI",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Loading indicator
        Card(
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đang nhập",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Nhập tin nhắn...")
                },
                trailingIcon = {
                    if (value.isNotEmpty()) {
                        IconButton(onClick = { onValueChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Xóa"
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() }),
                maxLines = 5,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(24.dp)
            )
            
            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (value.isNotEmpty())
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Gửi",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

// Đổi tên hàm để tránh xung đột
fun formatMessageTime(timestamp: Long): String {
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

data class ChatMessage(
    val id: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long
)

fun getSampleMessages(chatId: String?): List<ChatMessage> {
    return if (chatId == null) {
        // Cuộc trò chuyện mới - không có tin nhắn
        emptyList()
    } else {
        // Giả lập tin nhắn cho cuộc trò chuyện đã có
        when (chatId) {
            "c1" -> listOf(
                ChatMessage(
                    id = "u1",
                    content = "Tôi muốn học về mẫu câu với て-form",
                    isFromUser = true,
                    timestamp = System.currentTimeMillis() - 3700000
                ),
                ChatMessage(
                    id = "a1",
                    content = "て-form là một hình thức quan trọng trong tiếng Nhật. Nó được sử dụng trong nhiều tình huống khác nhau như:\n\n1. Kết nối nhiều hành động\n2. Yêu cầu/Đề nghị\n3. Xin phép\n4. Diễn tả trạng thái\n\nBạn muốn tìm hiểu về cách nào?",
                    isFromUser = false,
                    timestamp = System.currentTimeMillis() - 3680000
                ),
                ChatMessage(
                    id = "u2",
                    content = "Tôi muốn học cách kết nối nhiều hành động",
                    isFromUser = true,
                    timestamp = System.currentTimeMillis() - 3600000
                ),
                ChatMessage(
                    id = "a2",
                    content = "Để kết nối nhiều hành động, chúng ta sử dụng động từ ở dạng て-form rồi thêm động từ tiếp theo. Ví dụ:\n\n朝ごはんを食べて、学校に行きます。\nAsagohan o tabete, gakkou ni ikimasu.\n(Tôi ăn sáng, rồi đi học.)\n\nテレビを見て、寝ました。\nTerebi o mite, nemashita.\n(Tôi xem TV, rồi đi ngủ.)",
                    isFromUser = false,
                    timestamp = System.currentTimeMillis() - 3580000
                )
            )
            "c2" -> listOf(
                ChatMessage(
                    id = "u1",
                    content = "Tôi gặp khó khăn khi phát âm âm R và L trong tiếng Nhật",
                    isFromUser = true,
                    timestamp = System.currentTimeMillis() - 90000000
                ),
                ChatMessage(
                    id = "a1",
                    content = "Trong tiếng Nhật, âm \"R\" (ら、り、る、れ、ろ) được phát âm khác với cả âm R và L trong tiếng Anh. Đó là âm đánh lưỡi, gần giống với âm \"d\" trong từ \"ready\" của tiếng Anh.\n\nĐể luyện tập, hãy thử lặp lại: らりるれろ (ra-ri-ru-re-ro) nhiều lần.",
                    isFromUser = false,
                    timestamp = System.currentTimeMillis() - 89900000
                )
            )
            else -> emptyList()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextChatScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TextChatScreen()
        }
    }
} 