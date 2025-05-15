package com.example.learnjapanese.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.learnjapanese.R
import com.example.learnjapanese.data.model.ReadingResponse
import com.example.learnjapanese.data.model.FeaturedBanner
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import com.example.learnjapanese.components.AppBottomNavigation
import com.example.learnjapanese.data.model.VocabularySearchItem
import com.example.learnjapanese.navigation.Screen
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.platform.LocalContext
import com.example.learnjapanese.utils.TextToSpeechUtil
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.window.DialogProperties

// Data class cho các mục trong bottom navigation
data class BottomNavItem(
    val title: String,
    val icon: Any, // Có thể là ImageVector hoặc Painter
    val selectedIcon: Any
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToVocabulary: () -> Unit = {},
    onNavigateToGrammar: () -> Unit = {},
    onNavigateToAlphabet: () -> Unit = {},
    onNavigateToKatakana: () -> Unit = {},
    onNavigateToReading: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
    onNavigateToChat: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    Log.d("DashboardScreen", "DashboardScreen composable started")
    
    // Collect states from ViewModel
    val latestReadings by viewModel.latestReadings.collectAsState()
    val featuredBanner by viewModel.featuredBanner.collectAsState()
    val learningFeatures by viewModel.learningFeatures.collectAsState()
    
    // Collect search states
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    
    Log.d("DashboardScreen", "States collected from ViewModel")
    
    var selectedNavItem by remember { mutableStateOf(4) } // Mặc định là trang chủ

    // Xử lý điều hướng
    val handleNavigation = { index: Int ->
        selectedNavItem = index
        when (index) {
            0 -> onNavigateToVocabulary()
            1 -> onNavigateToGrammar()
            2 -> onNavigateToReading() // Đọc hiểu
            3 -> onNavigateToChat() // Trò chuyện
            4 -> {} // Trang chủ (mặc định)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Học Tiếng Nhật",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Thông báo"
                        )
                    }
                    IconButton(onClick = { onNavigateToAccount() }) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                                .clickable { onNavigateToAccount() }
                        ) {
                            Text(
                                "A",
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
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
                currentRoute = Screen.Dashboard.route,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 16.dp)
            ) {
                Log.d("DashboardScreen", "Rendering search bar, isSearchActive = $isSearchActive")
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { 
                            Log.d("DashboardScreen", "Search bar clicked, setting isSearchActive to true")
                            viewModel.setSearchActive(true) 
                        },
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
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Tìm kiếm từ vựng...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            // Dialog tìm kiếm khi người dùng nhấn vào thanh tìm kiếm
            if (isSearchActive) {
                Log.d("DashboardScreen", "Showing search dialog")
                
                Dialog(
                    onDismissRequest = { 
                        Log.d("DashboardScreen", "Dialog dismissed, closing search")
                        viewModel.setSearchActive(false) 
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                        usePlatformDefaultWidth = false
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.92f) // Chiếm 92% chiều rộng màn hình
                            .padding(12.dp)
                            .heightIn(min = 100.dp, max = 550.dp), // Tăng chiều cao tối đa
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            // Tiêu đề
                            Text(
                                text = "Tìm kiếm từ vựng",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 20.dp) // Tăng khoảng cách
                            )
                            
                            // Thanh tìm kiếm
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                placeholder = { Text("Nhập từ khóa tìm kiếm...") },
                                leadingIcon = { 
                                    Icon(
                                        imageVector = Icons.Default.Search, 
                                        contentDescription = "Search",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(28.dp) // Tăng kích thước icon
                                    ) 
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    cursorColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(16.dp),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyLarge, // Tăng cỡ chữ
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp)) // Tăng khoảng cách
                            
                            // Kết quả tìm kiếm
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp) // Tăng chiều cao kết quả
                            ) {
                                when {
                                    isSearching -> {
                                        // Hiển thị đang tìm kiếm
                                        Log.d("DashboardScreen", "Showing search progress indicator")
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(48.dp), // Tăng kích thước
                                                    strokeWidth = 4.dp, // Tăng độ dày
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                Spacer(modifier = Modifier.height(24.dp)) // Tăng khoảng cách
                                                Text(
                                                    text = "Đang tìm kiếm...",
                                                    style = MaterialTheme.typography.titleMedium, // Tăng cỡ chữ
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                )
                                            }
                                        }
                                    }
                                    searchQuery.isNotBlank() && searchResults.isEmpty() -> {
                                        // Hiển thị không có kết quả
                                        Log.d("DashboardScreen", "No search results found for query: $searchQuery")
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.padding(horizontal = 20.dp) // Thêm padding ngang
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(64.dp), // Tăng kích thước
                                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                                )
                                                Spacer(modifier = Modifier.height(24.dp)) // Tăng khoảng cách
                                                Text(
                                                    text = "Không tìm thấy từ vựng nào",
                                                    style = MaterialTheme.typography.headlineSmall, // Tăng cỡ chữ
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                    fontWeight = FontWeight.Medium,
                                                    textAlign = TextAlign.Center
                                                )
                                                Spacer(modifier = Modifier.height(12.dp)) // Tăng khoảng cách
                                                Text(
                                                    text = "Thử tìm với từ khóa khác",
                                                    style = MaterialTheme.typography.titleMedium, // Tăng cỡ chữ
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                    searchResults.isNotEmpty() -> {
                                        // Hiển thị danh sách kết quả
                                        Log.d("DashboardScreen", "Showing ${searchResults.size} search results")
                                        Column(
                                            modifier = Modifier.padding(horizontal = 4.dp) // Thêm padding ngang
                                        ) {
                                            Text(
                                                text = "Kết quả tìm kiếm (${searchResults.size})",
                                                style = MaterialTheme.typography.titleLarge, // Tăng cỡ chữ
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(bottom = 16.dp, start = 8.dp) // Tăng padding
                                            )
                                            LazyColumn(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.spacedBy(16.dp), // Tăng khoảng cách giữa các kết quả
                                                contentPadding = PaddingValues(bottom = 16.dp) // Thêm padding dưới cùng
                                            ) {
                                                items(searchResults) { item ->
                                                    Log.d("DashboardScreen", "Rendering search result item: ${item.word}")
                                                    VocabularySearchResultItem(item = item)
                                                }
                                            }
                                        }
                                    }
                                    searchQuery.isBlank() -> {
                                        // Hiển thị gợi ý khi chưa nhập từ khóa
                                        Log.d("DashboardScreen", "Showing search hint")
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.padding(horizontal = 32.dp) // Tăng padding ngang
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_sound),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(86.dp), // Tăng kích thước icon
                                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                                )
                                                Spacer(modifier = Modifier.height(24.dp))
                                                Text(
                                                    text = "Tìm kiếm từ vựng Nhật - Việt",
                                                    style = MaterialTheme.typography.headlineSmall, // Tăng cỡ chữ
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                                    textAlign = TextAlign.Center
                                                )
                                                Spacer(modifier = Modifier.height(16.dp))
                                                Text(
                                                    text = "Nhập từ khóa để bắt đầu tìm kiếm",
                                                    style = MaterialTheme.typography.titleMedium, // Tăng cỡ chữ
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                                    textAlign = TextAlign.Center
                                                )
                                                Spacer(modifier = Modifier.height(40.dp)) // Tăng khoảng cách
                                                Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                                    ),
                                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                                                    shape = RoundedCornerShape(16.dp) // Tăng bo tròn góc
                                                ) {
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

            // Bài đọc mới nhất
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                LatestReadingsCard(
                    readings = latestReadings,
                    onReadingClick = { readingId ->
                        // Navigate to reading detail screen
                        Log.d("DashboardScreen", "Navigating to reading detail with ID: $readingId")
                        onNavigate("reading_detail/$readingId")
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Banner quảng cáo tính năng nổi bật
            featuredBanner?.let { banner ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    FeatureBanner(
                        banner = banner,
                        onNavigateToChat = {
                            Log.d("DashboardScreen", "Banner tính năng nổi bật được nhấn, điều hướng đến màn hình Chat")
                            onNavigateToChat()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Phần bảng chữ cái - thay thế phần "Bắt đầu học"
            AlphabetFeatureSection(
                onNavigateToAlphabet = onNavigateToAlphabet,
                onNavigateToKatakana = { onNavigate("alphabet_katakana") }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AlphabetFeatureSection(onNavigateToAlphabet: () -> Unit, onNavigateToKatakana: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Bảng chữ cái Nhật Bản",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Hiển thị hệ thống Hiragana và Katakana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hiragana
            AlphabetTypeCard(
                title = "Hiragana",
                description = "Chữ cái cơ bản dùng cho từ và ngữ pháp Nhật",
                exampleChar = "ひらがな",
                modifier = Modifier.weight(1f),
                onClick = onNavigateToAlphabet
            )
            
            // Katakana
            AlphabetTypeCard(
                title = "Katakana",
                description = "Chữ cái dùng cho từ vựng nước ngoài",
                exampleChar = "カタカナ",
                modifier = Modifier.weight(1f),
                onClick = onNavigateToKatakana
            )
        }
    }
}

@Composable
fun AlphabetExampleCard(character: String, romaji: String) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = character,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = romaji,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun AlphabetTypeCard(
    title: String,
    description: String,
    exampleChar: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Text(
                text = exampleChar,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun LatestReadingsCard(readings: List<ReadingResponse>, onReadingClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Bài đọc mới nhất",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (readings.isEmpty()) {
                // Show loading or empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Đang tải bài đọc...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                // Show readings
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    readings.forEach { reading ->
                        ReadingItem(
                            reading = reading,
                            onClick = { onReadingClick(reading.reading_id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReadingItem(reading: ReadingResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Image section
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            ) {
                if (!reading.background_image_url.isNullOrEmpty()) {
                    // Use AsyncImage instead of Image with rememberAsyncImagePainter for better loading
                    AsyncImage(
                        model = reading.background_image_url,
                        contentDescription = "Reading thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        // Add a placeholder while loading
                        error = painterResource(id = R.drawable.ic_reading),
                        placeholder = painterResource(id = R.drawable.ic_reading)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            
            // Content section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = reading.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = reading.short_intro,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Level badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = reading.level,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // Estimated time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${reading.estimated_time} phút",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureBanner(banner: FeaturedBanner, onNavigateToChat: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable {
                Log.d("FeatureBanner", "Banner clicked, navigating to Chat Screen")
                onNavigateToChat()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = banner.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = banner.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .width(120.dp)
                        .height(36.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Khám phá",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxHeight()
                    .padding(end = 16.dp)
            ) {
                banner.imageUrl?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Banner Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.ic_reading),
                        placeholder = painterResource(id = R.drawable.ic_reading)
                    )
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubble, 
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun VocabularySearchResultItem(item: VocabularySearchItem) {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp), // Thêm padding dọc
        shape = RoundedCornerShape(20.dp), // Tăng bo tròn góc
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp // Thêm đổ bóng nhẹ
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp) // Tăng padding nội dung
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.word,
                        style = MaterialTheme.typography.titleLarge, // Tăng cỡ chữ
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(12.dp)) // Tăng khoảng cách
                    Text(
                        text = item.reading,
                        style = MaterialTheme.typography.titleMedium, // Tăng cỡ chữ
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                // Nút phát âm
                IconButton(
                    onClick = { 
                        // Phát âm từ vựng
                        TextToSpeechUtil.speak(context, item.word)
                    },
                    modifier = Modifier
                        .size(44.dp) // Tăng kích thước nút
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sound),
                        contentDescription = "Phát âm",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp) // Tăng kích thước icon
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp)) // Tăng khoảng cách
            
            // Nghĩa tiếng Việt
            Text(
                text = item.meaning,
                style = MaterialTheme.typography.bodyLarge, // Tăng cỡ chữ
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(vertical = 4.dp) // Thêm padding dọc
            )
            
            if (!item.example_sentence.isNullOrBlank() && !item.example_meaning.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp)) // Tăng khoảng cách
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                Spacer(modifier = Modifier.height(16.dp)) // Tăng khoảng cách
                
                // Ví dụ
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp) // Tăng kích thước badge
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ex",
                            style = MaterialTheme.typography.labelMedium, // Tăng cỡ chữ
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp)) // Tăng khoảng cách
                    
                    Column(
                        modifier = Modifier.weight(1f) // Đảm bảo vừa với không gian còn lại
                    ) {
                        // Ví dụ tiếng Nhật và nút phát âm
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.example_sentence,
                                style = MaterialTheme.typography.bodyLarge, // Tăng cỡ chữ
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                modifier = Modifier.weight(1f)
                            )
                            
                            // Nút phát âm ví dụ
                            IconButton(
                                onClick = { 
                                    // Phát âm câu ví dụ
                                    TextToSpeechUtil.speak(context, item.example_sentence)
                                },
                                modifier = Modifier
                                    .size(36.dp) // Tăng kích thước nút
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sound),
                                    contentDescription = "Phát âm ví dụ",
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp) // Tăng kích thước icon
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp)) // Tăng khoảng cách
                        
                        // Nghĩa ví dụ
                        Text(
                            text = item.example_meaning,
                            style = MaterialTheme.typography.bodyMedium, // Tăng cỡ chữ
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

// Danh sách các mục trong bottom navigation
@Composable
fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem("Từ vựng", Icons.Outlined.Book, Icons.Filled.Book),
        BottomNavItem("Ngữ pháp", Icons.Outlined.Edit, Icons.Filled.Edit),
        BottomNavItem(
            "Đọc hiểu",
            painterResource(id = R.drawable.ic_reading),
            painterResource(id = R.drawable.ic_reading_filled)
        ),
        BottomNavItem("Trò chuyện", Icons.Outlined.ChatBubble, Icons.Filled.ChatBubble),
        BottomNavItem("Trang chủ", Icons.Outlined.Home, Icons.Filled.Home)
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DashboardScreen()
        }
    }
}