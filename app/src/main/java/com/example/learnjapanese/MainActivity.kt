package com.example.learnjapanese

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
                                    // Sẽ thêm sau
                                },
                                onNavigateToReading = {
                                    // Sẽ thêm sau
                                },
                                onNavigateToAccount = {
                                    // Sẽ thêm sau
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