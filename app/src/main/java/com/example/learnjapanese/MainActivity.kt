package com.example.learnjapanese

import BangChuCaiScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.learnjapanese.navigation.NavGraph
import com.example.learnjapanese.screens.DashboardScreen
import com.example.learnjapanese.screens.call.CallListScreen
import com.example.learnjapanese.screens.call.CallScreen
import com.example.learnjapanese.screens.chat.ChatListScreen
import com.example.learnjapanese.screens.chat.TextChatScreen
import com.example.learnjapanese.screens.chat.VoiceChatScreen
import com.example.learnjapanese.screens.grammar.GrammarDetailScreen
import com.example.learnjapanese.screens.grammar.GrammarQuizScreen
import com.example.learnjapanese.screens.grammar.GrammarScreen
import com.example.learnjapanese.screens.profile.EditProfileScreen
import com.example.learnjapanese.screens.profile.FriendsScreen
import com.example.learnjapanese.screens.profile.NotificationsScreen
import com.example.learnjapanese.screens.profile.ProfileScreen
import com.example.learnjapanese.screens.profile.SettingsScreen
import com.example.learnjapanese.screens.reading.ReadingDetailScreen
import com.example.learnjapanese.screens.reading.ReadingListScreen
import com.example.learnjapanese.screens.vocabulary.VocabularyDetailScreen
import com.example.learnjapanese.screens.vocabulary.VocabularyFlashcardScreen
import com.example.learnjapanese.screens.vocabulary.VocabularyQuizScreen
import com.example.learnjapanese.screens.vocabulary.VocabularyScreen
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnJapaneseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard"
                    ) {
                        composable("dashboard") {
                            DashboardScreen(
                                onNavigateToVocabulary = {
                                    navController.navigate("vocabulary")
                                },
                                onNavigateToGrammar = {
                                    navController.navigate("grammar")
                                },
                                onNavigateToAlphabet = {  
                                    navController.navigate("alphabet")
                                },
                                onNavigateToReading = {
                                    navController.navigate("reading")
                                },
                                onNavigateToAccount = {
                                    navController.navigate("profile")
                                },
                                onNavigateToChat = {
                                    navController.navigate("chat")
                                }
                            )
                        }
                        
                        // Thêm route mới cho màn hình Alphabet
                        composable("alphabet") {
                            BangChuCaiScreen(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("vocabulary") {
                            VocabularyScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onTopicClick = { topicId ->
                                    navController.navigate("vocabulary/detail/$topicId")
                                }
                            )
                        }
                        
                        composable(
                            route = "vocabulary/detail/{topicId}",
                            arguments = listOf(
                                navArgument("topicId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val topicId = backStackEntry.arguments?.getString("topicId") ?: "1"
                            VocabularyDetailScreen(
                                topicId = topicId,
                                onBack = {
                                    navController.popBackStack()
                                },
                                 onStartFlashcards = { id ->
                                    navController.navigate("vocabulary/flashcards/$id")
                                },
                                onStartQuiz = { id ->
                                    navController.navigate("vocabulary/quiz/$id")
                                }
                            )
                        }
                        
                        composable(
                            route = "vocabulary/flashcards/{topicId}",
                            arguments = listOf(
                                navArgument("topicId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val topicId = backStackEntry.arguments?.getString("topicId") ?: "1"
                            VocabularyFlashcardScreen(
                                topicId = topicId,
                                onBack = {
                                    navController.popBackStack()
                                },
                                onComplete = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        composable(
                            route = "vocabulary/quiz/{topicId}",
                            arguments = listOf(
                                navArgument("topicId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val topicId = backStackEntry.arguments?.getString("topicId") ?: "1"
                            VocabularyQuizScreen(
                                topicId = topicId,
                                onBack = {
                                    navController.popBackStack()
                                },
                                onComplete = { correct, total ->
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        composable("grammar") {
                            GrammarScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onGrammarClick = { grammarId ->
                                    navController.navigate("grammar/detail/$grammarId")
                                },
                                onStartQuiz = { grammarIds ->
                                    val idsParam = grammarIds.joinToString(",")
                                    navController.navigate("grammar/quiz?ids=$idsParam")
                                }
                            )
                        }
                        
                        composable(
                            route = "grammar/detail/{grammarId}",
                            arguments = listOf(
                                navArgument("grammarId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val grammarId = backStackEntry.arguments?.getString("grammarId") ?: "g1"
                            GrammarDetailScreen(
                                grammarId = grammarId,
                                onBack = {
                                    navController.popBackStack()
                                },
                                onStartQuiz = { id ->
                                    navController.navigate("grammar/quiz?ids=$id")
                                }
                            )
                        }
                        
                        composable(
                            route = "grammar/quiz?ids={ids}",
                            arguments = listOf(
                                navArgument("ids") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val idsString = backStackEntry.arguments?.getString("ids") ?: "g1"
                            val grammarIds = idsString.split(",")
                            GrammarQuizScreen(
                                grammarIds = grammarIds,
                                onBack = {
                                    navController.popBackStack()
                                },
                                onComplete = { correct, total ->
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        composable("reading") {
                            ReadingListScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onReadingClick = { readingId ->
                                    navController.navigate("reading/detail/$readingId")
                                }
                            )
                        }
                        
                        composable(
                            route = "reading/detail/{readingId}",
                            arguments = listOf(
                                navArgument("readingId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val readingId = backStackEntry.arguments?.getString("readingId") ?: "1"
                            ReadingDetailScreen(
                                readingId = readingId,
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        composable("chat") {
                            ChatListScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onStartTextChat = {
                                    navController.navigate("chat/text")
                                },
                                onStartVoiceChat = {
                                    navController.navigate("chat/voice")
                                },
                                onHistoryChatClick = { chatId ->
                                    navController.navigate("chat/text/$chatId")
                                }
                            )
                        }
                        
                        composable("chat/text") {
                            TextChatScreen(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        composable(
                            route = "chat/text/{chatId}",
                            arguments = listOf(
                                navArgument("chatId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val chatId = backStackEntry.arguments?.getString("chatId")
                            TextChatScreen(
                                chatId = chatId,
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        composable("chat/voice") {
                            VoiceChatScreen(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        composable("profile") {
                            ProfileScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onEditProfile = {
                                    navController.navigate("profile/edit")
                                },
                                onFindFriends = {
                                    navController.navigate("profile/friends")
                                },
                                onNotifications = {
                                    navController.navigate("profile/notifications")
                                },
                                onSettings = {
                                    navController.navigate("profile/settings")
                                }
                            )
                        }
                        
                        composable("profile/edit") {
                            EditProfileScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onSave = { fullName, email, phone, bio, level ->
                                    // Xử lý lưu thông tin cá nhân
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        composable("profile/friends") {
                            FriendsScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onUserClick = { userId ->
                                    // Hiển thị profile của người dùng khác
                                    navController.navigate("profile/user/$userId")
                                }
                            )
                        }
                        
                        composable("profile/notifications") {
                            NotificationsScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onNotificationClick = { notificationId ->
                                    // Xử lý khi nhấn vào thông báo
                                },
                                onUserClick = { userId ->
                                    // Hiển thị profile của người gửi yêu cầu kết bạn
                                    navController.navigate("profile/user/$userId")
                                }
                            )
                        }
                        
                        composable("profile/settings") {
                            SettingsScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onAccountSettings = {
                                    navController.navigate("profile/edit")
                                },
                                onLanguageSettings = {
                                    // Sẽ thêm sau
                                },
                                onNotificationSettings = {
                                    // Sẽ thêm sau
                                },
                                onLogout = {
                                    // Xử lý đăng xuất và quay về màn hình đăng nhập (nếu có)
                                    navController.navigate("dashboard") {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                }
                            )
                        }
                        
                        composable(
                            route = "profile/user/{userId}",
                            arguments = listOf(
                                navArgument("userId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val userId = backStackEntry.arguments?.getString("userId") ?: "u1"
                            // Màn hình xem profile người dùng khác - có thể dùng chung ProfileScreen với flag
                            ProfileScreen(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    LearnJapaneseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DashboardScreen()
        }
    }
}