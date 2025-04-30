package com.example.learnjapanese.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learnjapanese.screens.DashboardScreen
import com.example.learnjapanese.screens.DashboardViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(viewModel = viewModel)
        }
        
        // Các màn hình khác sẽ được thêm vào đây khi cần
        composable(Screen.Vocabulary.route) {
            // VocabularyScreen()
        }
        
        composable(Screen.Grammar.route) {
            // GrammarScreen()
        }
        
        composable(Screen.Call.route) {
            // CallScreen()
        }
        
        composable(Screen.AI.route) {
            // AIConversationScreen()
        }
        
        composable(Screen.Profile.route) {
            // ProfileScreen()
        }
    }
} 