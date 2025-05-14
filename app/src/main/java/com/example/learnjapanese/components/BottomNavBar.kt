package com.example.learnjapanese.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.learnjapanese.R
import com.example.learnjapanese.navigation.Screen

// Data class cho các mục trong bottom navigation
private data class BottomNavItem(
    val title: String,
    val icon: Any, // Có thể là ImageVector hoặc Painter
    val selectedIcon: Any,
    val route: String
)

// Danh sách các mục trong bottom navigation
@Composable
private fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem("Từ vựng", Icons.Outlined.Book, Icons.Filled.Book, Screen.Vocabulary.route),
        BottomNavItem("Ngữ pháp", Icons.Outlined.Edit, Icons.Filled.Edit, Screen.Grammar.route),
        BottomNavItem(
            "Đọc hiểu",
            painterResource(id = R.drawable.ic_reading),
            painterResource(id = R.drawable.ic_reading_filled),
            Screen.Reading.route
        ),
        BottomNavItem("Trò chuyện", Icons.Outlined.ChatBubble, Icons.Filled.ChatBubble, Screen.Chat.route),
        BottomNavItem("Trang chủ", Icons.Outlined.Home, Icons.Filled.Home, Screen.Dashboard.route)
    )
}

@Composable
fun AppBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val bottomNavItems = getBottomNavItems()
    val selectedNavIndex = when(currentRoute) {
        Screen.Vocabulary.route -> 0
        Screen.Grammar.route -> 1
        Screen.Reading.route -> 2
        Screen.Chat.route -> 3
        Screen.Dashboard.route -> 4
        else -> 4 // Mặc định là Dashboard
    }
    
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
                        when (val icon = if (selectedNavIndex == index) item.selectedIcon else item.icon) {
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
                                fontWeight = if (selectedNavIndex == index) FontWeight.Bold else FontWeight.Normal
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        ) 
                    },
                    selected = selectedNavIndex == index,
                    onClick = { onNavigate(item.route) },
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
                selected = selectedNavIndex == 4,
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
                        when (val icon = if (selectedNavIndex == actualIndex) item.selectedIcon else item.icon) {
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
                                fontWeight = if (selectedNavIndex == actualIndex) FontWeight.Bold else FontWeight.Normal
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        ) 
                    },
                    selected = selectedNavIndex == actualIndex,
                    onClick = { onNavigate(item.route) },
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
            onClick = { onNavigate(Screen.Dashboard.route) },
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
                imageVector = if (selectedNavIndex == 4) Icons.Filled.Home else Icons.Outlined.Home,
                contentDescription = "Trang chủ",
                modifier = Modifier.size(28.dp)
            )
        }
    }
} 