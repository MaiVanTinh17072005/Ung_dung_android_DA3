package com.example.learnjapanese.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Vocabulary : Screen("vocabulary")
    object Grammar : Screen("grammar")
    object Call : Screen("call")
    object AI : Screen("ai_conversation")
    object Profile : Screen("profile")
} 