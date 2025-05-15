package com.example.learnjapanese.screens.vocabulary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.data.model.VocabularyTopic
import com.example.learnjapanese.utils.Resource
import com.example.learnjapanese.components.AppBottomNavigation
import com.example.learnjapanese.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreen(
    onBack: () -> Unit = {},
    onTopicClick: (String) -> Unit = {},
    onNavigate: (String) -> Unit = {},
    viewModel: VocabularyViewModel = hiltViewModel()
) {
    // Lấy state từ ViewModel
    val topicsResource by viewModel.topics.collectAsState()
    val favoriteTopics by viewModel.favoriteTopics.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    
    // Effect để search khi query thay đổi
    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty() && !isSearchActive) {
            // Không thực hiện tìm kiếm nếu người dùng chưa nhấn vào ô tìm kiếm
            return@LaunchedEffect
        }
        viewModel.searchTopics(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            placeholder = {
                                Text(
                                    "Tìm kiếm chủ đề...",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search icon",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Xóa tìm kiếm",
                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    viewModel.searchTopics(searchQuery)
                                    focusManager.clearFocus()
                                }
                            )
                        )
                        
                        // Tự động yêu cầu focus khi chuyển sang chế độ tìm kiếm
                        LaunchedEffect(isSearchActive) {
                            if (isSearchActive) {
                                focusRequester.requestFocus()
                            }
                        }
                    } else {
                        Text(
                            "Chủ đề từ vựng",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (isSearchActive) {
                            isSearchActive = false
                            searchQuery = ""
                            viewModel.clearSearch()
                            focusManager.clearFocus()
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = if (isSearchActive) "Hủy tìm kiếm" else "Quay lại"
                        )
                    }
                },
                actions = {
                    if (!isSearchActive) {
                        IconButton(onClick = { 
                            isSearchActive = true 
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Tìm kiếm"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        bottomBar = {
            AppBottomNavigation(
                currentRoute = Screen.Vocabulary.route,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Hiển thị hộp tìm kiếm nếu không ở chế độ tìm kiếm active
            if (!isSearchActive) {
                item {
                    // Search Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { isSearchActive = true }
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search icon",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                "Tìm kiếm chủ đề từ vựng...",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            
            // Hiển thị thanh trạng thái tìm kiếm nếu có từ khóa
            if (searchQuery.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Kết quả cho \"$searchQuery\"",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Button(
                            onClick = { 
                                searchQuery = ""
                                viewModel.clearSearch()
                                focusManager.clearFocus()
                                if (isSearchActive) {
                                    isSearchActive = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("Hủy")
                        }
                    }
                }
            }

            // Favorite Topics Section (chỉ hiển thị khi không tìm kiếm)
            if (searchQuery.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Chủ đề yêu thích",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        if (favoriteTopics.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (favoriteTopics.isNotEmpty()) {
                                    FavoriteTopicCard(
                                        topic = favoriteTopics[0],
                                        modifier = Modifier.weight(1f),
                                        onClick = { onTopicClick(favoriteTopics[0].id) }
                                    )
                                    
                                    if (favoriteTopics.size > 1) {
                                        FavoriteTopicCard(
                                            topic = favoriteTopics[1],
                                            modifier = Modifier.weight(1f),
                                            onClick = { onTopicClick(favoriteTopics[1].id) }
                                        )
                                    } else {
                                        // Spacer để giữ cân đối nếu chỉ có 1 chủ đề yêu thích
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        } else {
                            // Hiển thị thông báo nếu không có chủ đề yêu thích
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Bạn chưa có chủ đề yêu thích",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // All Topics header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    Text(
                        text = if (searchQuery.isEmpty()) "Tất cả chủ đề" else "Kết quả tìm kiếm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            
            // Hiển thị danh sách chủ đề
            when (topicsResource) {
                is Resource.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                
                is Resource.Success -> {
                    val topics = (topicsResource as Resource.Success<List<VocabularyTopic>>).data
                    if (topics?.isNotEmpty() == true) {
                        items(topics) { topic ->
                            TopicCard(
                                topic = topic,
                                onClick = { onTopicClick(topic.id) },
                                onFavoriteClick = { viewModel.toggleFavorite(topic.id) },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = if (searchQuery.isEmpty()) 
                                            "Không có chủ đề nào" 
                                        else 
                                            "Không tìm thấy chủ đề \"$searchQuery\"",
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                    
                                    if (searchQuery.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = { 
                                                searchQuery = ""
                                                viewModel.clearSearch()
                                                if (isSearchActive) {
                                                    isSearchActive = false
                                                    focusManager.clearFocus()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Text("Hiển thị tất cả chủ đề")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                is Resource.Error -> {
                    item {
                        val errorMessage = (topicsResource as Resource.Error<List<VocabularyTopic>>).message
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Không thể tải danh sách chủ đề",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = errorMessage ?: "Đã xảy ra lỗi không xác định",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Hiển thị card chủ đề yêu thích
@Composable
fun FavoriteTopicCard(
    topic: VocabularyTopic,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = topic.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = topic.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "${topic.totalWords} từ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Progress
//                Column(modifier = Modifier.weight(1f)) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "Tiến độ:",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//                        )
//                        Text(
//                            text = "${(topic.progress * 100).toInt()}%",
//                            style = MaterialTheme.typography.bodySmall.copy(
//                                fontWeight = FontWeight.Bold
//                            ),
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    LinearProgressIndicator(
//                        progress = { topic.progress },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(4.dp)
//                            .clip(CircleShape),
//                        color = MaterialTheme.colorScheme.primary,
//                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                // Arrow icon
//                Icon(
//                    imageVector = Icons.Default.ArrowForward,
//                    contentDescription = "Go to topic",
//                    tint = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.size(20.dp)
//                )
//            }
        }
    }
}

// Hiển thị card chủ đề thông thường
@Composable
fun TopicCard(
    topic: VocabularyTopic,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = if (topic.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Favorite",
                    tint = if (topic.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onFavoriteClick() }
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "${topic.totalWords} từ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Progress
//                Column(modifier = Modifier.weight(1f)) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "Tiến độ:",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//                        )
//                        Text(
//                            text = "${(topic.progress * 100).toInt()}%",
//                            style = MaterialTheme.typography.bodySmall.copy(
//                                fontWeight = FontWeight.Bold
//                            ),
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    LinearProgressIndicator(
//                        progress = { topic.progress },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(4.dp)
//                            .clip(CircleShape),
//                        color = MaterialTheme.colorScheme.primary,
//                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                // Go icon
//                Icon(
//                    imageVector = Icons.Default.KeyboardArrowRight,
//                    contentDescription = "Go to topic",
//                    tint = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VocabularyScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Để preview hoạt động, không sử dụng hiltViewModel() vì không có Hilt trong preview
            // Thay vào đó chúng ta cần tạo mock dữ liệu và ViewModel
            VocabularyScreen()
        }
    }
} 