package com.example.learnjapanese.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.learnjapanese.R
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onNavigateToVocabulary: () -> Unit = {},
    onNavigateToGrammar: () -> Unit = {},
    onNavigateToReading: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
    onNavigateToChat: () -> Unit = {}
) {
    // Collect states from ViewModel
    val learningStats by viewModel.learningStats.collectAsState()
    val featuredBanner by viewModel.featuredBanner.collectAsState()
    val learningFeatures by viewModel.learningFeatures.collectAsState()
    
    var selectedNavItem by remember { mutableStateOf(4) } // Mặc định là trang chủ
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Tăng chiều cao để chứa FAB
            ) {
                // Thanh điều hướng
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .align(Alignment.BottomCenter)
                        .shadow(5.dp)
                        .zIndex(0f)
                ) {
                    val bottomNavItems = getBottomNavItems()
                    val navItems = listOf(
                        bottomNavItems[0],
                        bottomNavItems[1],
                        // Để trống ở giữa cho nút Home
                        bottomNavItems[2],
                        bottomNavItems[3]
                    )
                    
                    // 2 mục đầu tiên
                    navItems.subList(0, 2).forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { 
                                when (val icon = if (selectedNavItem == index) item.selectedIcon else item.icon) {
                                    is ImageVector -> Icon(
                                        imageVector = icon,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    else -> Icon(
                                        painter = icon as androidx.compose.ui.graphics.painter.Painter,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            label = { 
                                Text(
                                    item.title, 
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (selectedNavItem == index) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                ) 
                            },
                            selected = selectedNavItem == index,
                            onClick = { handleNavigation(index) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.surface,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                    
                    // Mục giữa (trống)
                    NavigationBarItem(
                        icon = { 
                            Spacer(modifier = Modifier.size(24.dp))
                        },
                        label = { Spacer(modifier = Modifier.height(12.dp)) },
                        selected = selectedNavItem == 4,
                        onClick = { },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    
                    // 2 mục cuối
                    navItems.subList(2, 4).forEachIndexed { index, item ->
                        val actualIndex = index + 2
                        NavigationBarItem(
                            icon = { 
                                when (val icon = if (selectedNavItem == actualIndex) item.selectedIcon else item.icon) {
                                    is ImageVector -> Icon(
                                        imageVector = icon,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    else -> Icon(
                                        painter = icon as androidx.compose.ui.graphics.painter.Painter,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            label = { 
                                Text(
                                    item.title, 
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (selectedNavItem == actualIndex) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                ) 
                            },
                            selected = selectedNavItem == actualIndex,
                            onClick = { handleNavigation(actualIndex) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.surface,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
                
                // Nút Trang chủ nổi ở giữa
                FloatingActionButton(
                    onClick = { handleNavigation(4) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (-16).dp)
                        .zIndex(1f)
                ) {
                    Icon(
                        imageVector = if (selectedNavItem == 4) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Trang chủ",
                        modifier = Modifier.size(28.dp)
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
                .verticalScroll(rememberScrollState())
        ) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { isSearchActive = true },
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
                            "Tìm kiếm chức năng...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            if (isSearchActive) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { isSearchActive = false },
                    active = isSearchActive,
                    onActiveChange = { isSearchActive = it },
                    placeholder = { Text("Tìm kiếm chức năng...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Kết quả tìm kiếm
                }
            }

            // Thống kê tiến độ học tập
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                LearningStatsCard(learningStats)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Banner quảng cáo tính năng nổi bật
            featuredBanner?.let { banner ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    FeatureBanner(banner)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Danh sách tính năng nhanh
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Bắt đầu học",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Grid của các tính năng
            if (learningFeatures.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .height((learningFeatures.size * 95).dp)
                        .fillMaxWidth(),
                    userScrollEnabled = false
                ) {
                    items(learningFeatures) { feature ->
                        QuickFeatureCard(
                            title = feature.title,
                            progress = feature.progress
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LearningStatsCard(stats: LearningStats) {
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
                text = "Tiến độ học tập",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    value = "${stats.wordsLearntToday}", 
                    label = "Từ mới hôm nay", 
                    accentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                StatItem(
                    value = "${stats.currentStreak}", 
                    label = "Ngày liên tiếp", 
                    accentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                StatItem(
                    value = "${stats.accuracy}%", 
                    label = "Độ chính xác", 
                    accentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String, accentColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(accentColor)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun FeatureBanner(banner: FeaturedBanner) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
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
                        .height(36.dp)
                        .clickable { /* TODO: Handle click */ },
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
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Banner Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
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
fun QuickFeatureCard(title: String, progress: Float, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(percent = 25))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
        }
    }
}

// Data class cho các mục trong bottom navigation
private data class BottomNavItem(
    val title: String,
    val icon: Any, // Có thể là ImageVector hoặc Painter
    val selectedIcon: Any
)

// Danh sách các mục trong bottom navigation
@Composable
private fun getBottomNavItems(): List<BottomNavItem> {
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