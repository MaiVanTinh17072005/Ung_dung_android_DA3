package com.example.learnjapanese

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learnjapanese.ui.screens.changepassword.ChangePasswordScreen
import com.example.learnjapanese.ui.screens.forgotpassword.ForgotPasswordScreen
import com.example.learnjapanese.ui.screens.login.LoginScreen
import com.example.learnjapanese.ui.screens.register.RegisterScreen
import com.example.learnjapanese.ui.screens.welcome.WelcomeScreen
import com.example.learnjapanese.ui.theme.LearnJapaneseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnJapaneseTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onContinueClick = { navController.navigate("login") }
            )
        }
        
        composable("login") {
            LoginScreen(
                onRegisterClick = { navController.navigate("register") },
                onForgotPasswordClick = { navController.navigate("forgot_password") },
                onLoginSuccess = { /* Handle login success */ }
            )
        }
        composable("register") {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate("login") }
            )
        }
        
        composable("forgot_password") {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onSubmitOtp = { navController.navigate("change_password") }
            )
        }
        
        composable("change_password") {
            ChangePasswordScreen(
                onBackClick = { navController.popBackStack() },
                onPasswordChanged = { navController.navigate("login") }
            )
        }
    }
}