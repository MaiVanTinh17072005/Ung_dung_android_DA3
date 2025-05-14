package com.example.learnjapanese.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object ChangePassword : Screen("change_password")
    object Dashboard : Screen("dashboard")
    object Vocabulary : Screen("vocabulary")
    object Grammar : Screen("grammar")
    object Reading : Screen("reading")
    object Alphabet : Screen("alphabet")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
} 