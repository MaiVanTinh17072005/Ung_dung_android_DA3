package com.example.learnjapanese.screens.call

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.learnjapanese.data.model.CallSession
import com.example.learnjapanese.data.model.CallState
import com.example.learnjapanese.data.model.Contact
import com.example.learnjapanese.data.model.ContactGroup
import com.example.learnjapanese.data.model.getContactGroups
import com.example.learnjapanese.data.model.getRecentCalls
import com.example.learnjapanese.data.model.getSampleContacts
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallListScreen(
    onBack: () -> Unit = {},
    onContactClick: (String) -> Unit = {},
    onStartCall: (String, Boolean) -> Unit = { _, _ -> }
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Danh bạ", "Gần đây")
    
    val contactGroups = remember { getContactGroups() }
    val recentCalls = remember { getRecentCalls() }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Gọi điện",
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
                    
                    IconButton(onClick = { /* TODO: Thêm liên hệ mới */ }) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Thêm liên hệ"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        floatingActionButton = {
            if (selectedTabIndex == 0) {
                FloatingActionButton(
                    onClick = { /* TODO: Mở bàn phím quay số */ },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Quay số"
                    )
                }
            }
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
                                Icon(Icons.Default.Call, contentDescription = null)
                            } else {
                                Icon(Icons.Default.History, contentDescription = null)
                            }
                        }
                    )
                }
            }
            
            // Nội dung theo tab
            when (selectedTabIndex) {
                0 -> ContactsList(
                    contactGroups = contactGroups,
                    onContactClick = onContactClick,
                    onStartCall = onStartCall
                )
                1 -> RecentCallsList(
                    recentCalls = recentCalls,
                    onCallBack = { contactId, isVideo ->
                        onStartCall(contactId, isVideo)
                    }
                )
            }
        }
    }
}

@Composable
fun ContactsList(
    contactGroups: List<ContactGroup>,
    onContactClick: (String) -> Unit = {},
    onStartCall: (String, Boolean) -> Unit = { _, _ -> }
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        contactGroups.forEach { group ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "(${group.contacts.size})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Lọc",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            items(group.contacts) { contact ->
                ContactItem(
                    contact = contact,
                    onClick = { onContactClick(contact.id) },
                    onCallClick = { onStartCall(contact.id, false) },
                    onVideoCallClick = { onStartCall(contact.id, true) }
                )
            }
            
            if (group != contactGroups.last()) {
                item {
                    Divider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp
                    )
                }
            }
        }
        
        // Khoảng cách dưới cùng
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ContactItem(
    contact: Contact,
    onClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onVideoCallClick: () -> Unit = {}
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
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .border(
                        width = if (contact.isOnline) 2.dp else 0.dp,
                        color = if (contact.isOnline) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.first().toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                // Trạng thái online
                if (contact.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                            .align(Alignment.BottomEnd)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Thông tin liên hệ
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    if (contact.isFavorite) {
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = contact.username ?: contact.phoneNumber ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = contact.lastSeen ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (contact.isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Nút gọi
            IconButton(
                onClick = onCallClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Gọi điện",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Nút gọi video
            IconButton(
                onClick = onVideoCallClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Gọi video",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun RecentCallsList(
    recentCalls: List<CallSession>,
    onCallBack: (String, Boolean) -> Unit = { _, _ -> }
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(recentCalls) { call ->
            RecentCallItem(
                call = call,
                onCallBack = { onCallBack(call.contact.id, false) },
                onVideoCallBack = { onCallBack(call.contact.id, true) }
            )
        }
        
        // Khoảng cách dưới cùng
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun RecentCallItem(
    call: CallSession,
    onCallBack: () -> Unit = {},
    onVideoCallBack: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon cuộc gọi vào/ra
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (call.isIncoming) 
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                        else 
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (call.isIncoming) Icons.Default.CallReceived else Icons.Default.CallMade,
                    contentDescription = if (call.isIncoming) "Cuộc gọi đến" else "Cuộc gọi đi",
                    tint = if (call.isIncoming) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Thông tin cuộc gọi
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = call.contact.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (call.isIncoming) {
                        Text(
                            text = "Cuộc gọi đến",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    } else {
                        Text(
                            text = "Cuộc gọi đi",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    Text(
                        text = " • ${formatCallDuration(call.duration ?: 0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = call.startTime?.let { formatCallDate(it) } ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            
            // Nút gọi lại
            IconButton(
                onClick = onCallBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Gọi lại",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Nút gọi video
            IconButton(
                onClick = onVideoCallBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Gọi video",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Hàm format thời gian
fun formatCallDuration(durationMillis: Long): String {
    val seconds = (durationMillis / 1000).toInt()
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    
    return if (minutes > 0) {
        "$minutes phút ${remainingSeconds}s"
    } else {
        "${remainingSeconds}s"
    }
}

// Hàm format ngày gọi
fun formatCallDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60 * 60 * 1000 -> "Vừa xong"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} giờ trước"
        diff < 48 * 60 * 60 * 1000 -> "Hôm qua"
        else -> {
            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            format.format(Date(timestamp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CallListScreenPreview() {
    LearnJapaneseTheme {
        CallListScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    LearnJapaneseTheme {
        ContactItem(
            contact = Contact(
                name = "Tanaka Yuki",
                username = "tanaka_sensei",
                isOnline = true,
                isFavorite = true,
                lastSeen = "Đang hoạt động"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecentCallItemPreview() {
    LearnJapaneseTheme {
        RecentCallItem(
            call = CallSession(
                contact = Contact(
                    name = "Sato Airi",
                    username = "airi_sato"
                ),
                startTime = System.currentTimeMillis() - 3600000,
                endTime = System.currentTimeMillis() - 3550000,
                state = CallState.ENDED,
                duration = 600000,
                isIncoming = true
            )
        )
    }
} 