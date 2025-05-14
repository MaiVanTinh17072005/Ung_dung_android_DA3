package com.example.learnjapanese.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.learnjapanese.screens.DashboardScreen
import com.example.learnjapanese.screens.login.LoginScreen
import com.example.learnjapanese.screens.register.RegisterScreen
import com.example.learnjapanese.screens.welcome.WelcomeScreen
import com.example.learnjapanese.screens.forgotpassword.ForgotPasswordScreen
import com.example.learnjapanese.screens.changepassword.ChangePasswordScreen
import com.example.learnjapanese.screens.vocabulary.VocabularyScreen
import com.example.learnjapanese.screens.grammar.GrammarScreen
import com.example.learnjapanese.screens.reading.ReadingListScreen
import com.example.learnjapanese.screens.chat.ChatListScreen
import com.example.learnjapanese.screens.alphabet.BangChuCaiScreen as AlphabetScreen
import com.example.learnjapanese.screens.profile.ProfileScreen
import com.example.learnjapanese.screens.vocabulary.VocabularyDetailScreen
import com.example.learnjapanese.screens.vocabulary.VocabularyFlashcardScreen
import com.example.learnjapanese.screens.vocabulary.VocabularyQuizScreen
import com.example.learnjapanese.screens.grammar.GrammarDetailScreen
import com.example.learnjapanese.screens.grammar.GrammarQuizScreen
import com.example.learnjapanese.screens.reading.ReadingDetailScreen
import com.example.learnjapanese.screens.chat.TextChatScreen
import com.example.learnjapanese.screens.chat.VoiceChatScreen
import com.example.learnjapanese.screens.profile.EditProfileScreen
import com.example.learnjapanese.screens.profile.SettingsScreen
import com.example.learnjapanese.screens.profile.NotificationsScreen
import com.example.learnjapanese.screens.profile.FriendsScreen
import com.example.learnjapanese.data.CharacterType

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(route = Screen.Welcome.route) {
            Log.d("NavGraph", "Đang render màn hình Welcome")
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        launchSingleTop = true
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(route = Screen.Login.route) {
            Log.d("NavGraph", "Đang render màn hình Login")
            LoginScreen(
                onRegisterClick = {
                    Log.d("NavGraph", "Người dùng nhấn đăng ký, điều hướng đến Register")
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {
                    Log.d("NavGraph", "Người dùng nhấn quên mật khẩu, điều hướng đến ForgotPassword")
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onLoginSuccess = {
                    Log.d("NavGraph", "Login success callback triggered, navigating to Dashboard...")
                    try {
                        Log.d("NavGraph", "Chuẩn bị điều hướng đến Dashboard với route: ${Screen.Dashboard.route}")
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        }
                        Log.d("NavGraph", "Navigation to Dashboard completed successfully")
                    } catch (e: Exception) {
                        Log.e("NavGraph", "Error during navigation to Dashboard: ${e.message}", e)
                    }
                }
            )
        }
        
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSubmitOtp = {
                    // Navigate to change password screen
                    navController.navigate(Screen.ChangePassword.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = false }
                    }
                }
            )
        }
        
        composable(route = Screen.ChangePassword.route) {
            ChangePasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPasswordChanged = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.Dashboard.route) {
            Log.d("NavGraph", "Đang render màn hình Dashboard")
            DashboardScreen(
                onNavigateToVocabulary = {
                    navController.navigate(Screen.Vocabulary.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToGrammar = {
                    navController.navigate(Screen.Grammar.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAlphabet = {
                    navController.navigate(Screen.Alphabet.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToKatakana = {
                    navController.navigate("alphabet_katakana") {
                        launchSingleTop = true
                    }
                },
                onNavigateToReading = {
                    navController.navigate(Screen.Reading.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToChat = {
                    navController.navigate(Screen.Chat.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAccount = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Screen.Vocabulary.route) {
            Log.d("NavGraph", "Đang render màn hình Vocabulary")
            VocabularyScreen(
                onBack = {
                    navController.popBackStack()
                },
                onTopicClick = { topicId ->
                    navController.navigate("vocabulary_detail/$topicId")
                },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }

        composable(
            route = "vocabulary_detail/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
            VocabularyDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                onStartFlashcards = { topicId ->
                    navController.navigate("vocabulary_flashcard/$topicId")
                },
                onStartQuiz = { topicId ->
                    navController.navigate("vocabulary_quiz/$topicId")
                },
                topicId = topicId
            )
        }

        composable(
            route = "vocabulary_flashcard/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
            VocabularyFlashcardScreen(
                onBack = {
                    navController.popBackStack()
                },
                topicId = topicId
            )
        }

        composable(
            route = "vocabulary_quiz/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
            VocabularyQuizScreen(
                onBack = {
                    navController.popBackStack()
                },
                topicId = topicId
            )
        }

        composable(route = Screen.Grammar.route) {
            Log.d("NavGraph", "Đang render màn hình Grammar")
            GrammarScreen(
                onBack = {
                    navController.popBackStack()
                },
                onGrammarClick = { grammarId ->
                    navController.navigate("grammar_detail/$grammarId")
                },
                onStartQuiz = { grammarIds ->
                    val idsParam = grammarIds.joinToString(",")
                    navController.navigate("grammar_quiz?ids=$idsParam")
                },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }

        composable(
            route = "grammar_detail/{grammarId}",
            arguments = listOf(navArgument("grammarId") { type = NavType.StringType })
        ) { backStackEntry ->
            val grammarId = backStackEntry.arguments?.getString("grammarId") ?: ""
            GrammarDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                grammarId = grammarId
            )
        }

        composable(
            route = "grammar_quiz?ids={ids}",
            arguments = listOf(navArgument("ids") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val idsParam = backStackEntry.arguments?.getString("ids") ?: ""
            val grammarIds = idsParam.split(",").filter { it.isNotEmpty() }
            GrammarQuizScreen(
                onBack = {
                    navController.popBackStack()
                },
                grammarIds = grammarIds
            )
        }

        composable(route = Screen.Reading.route) {
            Log.d("NavGraph", "Đang render màn hình Reading")
            ReadingListScreen(
                onBack = {
                    navController.popBackStack()
                },
                onReadingClick = { readingId ->
                    navController.navigate("reading_detail/$readingId")
                },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }

        composable(
            route = "reading_detail/{readingId}",
            arguments = listOf(navArgument("readingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val readingId = backStackEntry.arguments?.getString("readingId") ?: ""
            ReadingDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                readingId = readingId
            )
        }

        composable(route = Screen.Alphabet.route) {
            Log.d("NavGraph", "Đang render màn hình Alphabet")
            AlphabetScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = "alphabet_katakana") {
            Log.d("NavGraph", "Đang render màn hình Alphabet với chế độ Katakana")
            AlphabetScreen(
                onBack = {
                    navController.popBackStack()
                },
                initialType = CharacterType.KATAKANA
            )
        }

        composable(route = Screen.Chat.route) {
            Log.d("NavGraph", "Đang render màn hình Chat")
            ChatListScreen(
                onBack = {
                    navController.popBackStack()
                },
                onStartTextChat = {
                    navController.navigate("text_chat")
                },
                onStartVoiceChat = {
                    navController.navigate("voice_chat")
                },
                onHistoryChatClick = { chatId ->
                    navController.navigate("text_chat/$chatId")
                },
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }

        composable(route = "text_chat") {
            TextChatScreen(
                chatId = null,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "text_chat/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            TextChatScreen(
                chatId = chatId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = "voice_chat") {
            VoiceChatScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "voice_chat/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            VoiceChatScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Profile.route) {
            Log.d("NavGraph", "Đang render màn hình Profile")
            ProfileScreen(
                onBack = {
                    navController.popBackStack()
                },
                onLogoutSuccess = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = "edit_profile") {
            EditProfileScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = "settings") {
            SettingsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = "notifications") {
            NotificationsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = "friends") {
            FriendsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 